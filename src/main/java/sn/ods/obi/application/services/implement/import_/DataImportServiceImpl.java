package sn.ods.obi.application.services.implement.import_;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.ods.obi.application.services.interfaces.import_.DataImportService;
import sn.ods.obi.domain.model.dataset.Dataset;
import sn.ods.obi.domain.model.import_.DataImport;
import sn.ods.obi.domain.model.import_.DataImportRow;
import sn.ods.obi.domain.repository.dataset.DatasetRepository;
import sn.ods.obi.domain.repository.import_.DataImportRepository;
import sn.ods.obi.domain.repository.import_.DataImportRowRepository;
import sn.ods.obi.presentation.dto.import_.DataImportDTO;
import sn.ods.obi.presentation.dto.import_.ImportPreviewDTO;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataImportServiceImpl implements DataImportService {

    private final DataImportRepository dataImportRepository;
    private final DataImportRowRepository dataImportRowRepository;
    private final DatasetRepository datasetRepository;

    @Override
    @Transactional
    public DataImportDTO importExcel(MultipartFile file, Long tenantId, boolean firstRowHeaders) {
        String fn = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        if (!fn.endsWith(".xlsx") && !fn.endsWith(".xls")) {
            throw new IllegalArgumentException("Format Excel attendu (.xlsx ou .xls). Fichier reçu : " + file.getOriginalFilename());
        }
        return importFile(file, tenantId, ";", "UTF-8", firstRowHeaders);
    }

    @Override
    @Transactional
    public DataImportDTO importCsv(MultipartFile file, Long tenantId, String csvDelimiter, String csvEncoding, boolean firstRowHeaders) {
        String fn = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        if (!fn.endsWith(".csv")) {
            throw new IllegalArgumentException("Format CSV attendu (.csv). Fichier reçu : " + file.getOriginalFilename());
        }
        return importFile(file, tenantId, csvDelimiter != null ? csvDelimiter : ";",
                csvEncoding != null ? csvEncoding : "UTF-8", firstRowHeaders);
    }

    @Override
    @Transactional
    public DataImportDTO importFile(MultipartFile file, Long tenantId, String csvDelimiter, String csvEncoding, boolean firstRowHeaders) {
        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
        String ext = filename.toLowerCase().endsWith(".csv") ? "csv" : "excel";

        List<Map<String, String>> columns = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();

        try {
            if ("csv".equals(ext)) {
                parseCsv(file, csvDelimiter, csvEncoding, firstRowHeaders, columns, rows);
            } else {
                parseExcel(file, firstRowHeaders, columns, rows);
            }
        } catch (Exception e) {
            log.error("Import failed: {}", e.getMessage());
            throw new IllegalArgumentException("Erreur lors du parsing: " + e.getMessage());
        }

        inferColumnTypes(columns, rows);

        if (rows.isEmpty() && columns.isEmpty()) {
            throw new IllegalArgumentException("Fichier vide ou sans données.");
        }

        DataImport imp = DataImport.builder()
                .nomFichier(filename)
                .typeFichier(ext)
                .tenantId(tenantId)
                .rowCount(rows.size())
                .columns(columns)
                .createdAt(LocalDateTime.now())
                .build();
        imp = dataImportRepository.save(imp);

        List<DataImportRow> toSave = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            toSave.add(DataImportRow.builder()
                    .importId(imp.getId())
                    .rowIndex(i)
                    .data(rows.get(i))
                    .build());
        }
        dataImportRowRepository.saveAll(toSave);

        return toDTO(imp);
    }

    private void parseCsv(MultipartFile file, String delim, String encoding, boolean firstRowHeaders,
                         List<Map<String, String>> columns, List<Map<String, Object>> rows) throws Exception {
        char delimiter = (delim != null && delim.length() > 0) ? delim.charAt(0) : ';';
        Charset charset = Charset.forName(encoding != null && !encoding.isBlank() ? encoding : "UTF-8");

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(delimiter)
                .setTrim(true)
                .build();

        try (var reader = new InputStreamReader(file.getInputStream(), charset);
             CSVParser parser = new CSVParser(reader, format)) {
            List<CSVRecord> records = parser.getRecords();
            if (records.isEmpty()) return;

            List<String> headerNames = new ArrayList<>();
            int startRow = 0;
            if (firstRowHeaders && !records.isEmpty()) {
                CSVRecord first = records.get(0);
                for (int i = 0; i < first.size(); i++) {
                    String h = first.get(i);
                    headerNames.add(h != null && !h.isBlank() ? h : "Col" + (i + 1));
                }
                startRow = 1;
            } else if (!records.isEmpty()) {
                int size = records.get(0).size();
                for (int i = 0; i < size; i++) headerNames.add("Col" + (i + 1));
            }

            for (String h : headerNames) {
                columns.add(Map.of("nom", h, "type", "texte"));
            }

            for (int r = startRow; r < records.size(); r++) {
                CSVRecord rec = records.get(r);
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 0; i < headerNames.size(); i++) {
                    String val = i < rec.size() ? rec.get(i) : "";
                    row.put(headerNames.get(i), val != null ? val : "");
                }
                rows.add(row);
            }
        }
    }

    private void parseExcel(MultipartFile file, boolean firstRowHeaders,
                           List<Map<String, String>> columns, List<Map<String, Object>> rows) throws Exception {
        String fn = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        boolean isXls = fn.endsWith(".xls");
        try (Workbook wb = isXls ? new HSSFWorkbook(file.getInputStream()) : new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) return;

            Iterator<Row> rowIter = sheet.iterator();
            if (!rowIter.hasNext()) return;

            Row headerRow = rowIter.next();
            List<String> headerNames = new ArrayList<>();
            int colCount = headerRow.getLastCellNum() > 0 ? headerRow.getLastCellNum() : 1;
            if (firstRowHeaders) {
                for (int i = 0; i < colCount; i++) {
                    String h = getCellString(headerRow.getCell(i));
                    headerNames.add(h != null && !h.isBlank() ? h : "Col" + (i + 1));
                }
            } else {
                for (int i = 0; i < colCount; i++) {
                    headerNames.add("Col" + (i + 1));
                }
            }
            if (!firstRowHeaders) {
                Map<String, Object> firstRow = new LinkedHashMap<>();
                for (int i = 0; i < headerNames.size(); i++) {
                    firstRow.put(headerNames.get(i), getCellString(headerRow.getCell(i)));
                }
                rows.add(firstRow);
            }

            for (String h : headerNames) {
                columns.add(Map.of("nom", h != null ? h : "Col", "type", "texte"));
            }

            while (rowIter.hasNext()) {
                Row r = rowIter.next();
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 0; i < headerNames.size(); i++) {
                    Cell cell = r.getCell(i);
                    Object val = getCellValue(cell);
                    row.put(headerNames.get(i), val != null ? val : "");
                }
                rows.add(row);
            }
        }
    }

    private static final int SAMPLE_SIZE = 200;
    private static final double RATIO_NUMERIC_STRICT = 0.8;
    private static final double RATIO_NUMERIC_RELAXED = 0.5;
    /** Formats après normalisation : entier, décimal, scientifique, .5 */
    private static final Pattern NUMERIC_PATTERN = Pattern.compile(
            "^-?(\\d+(\\.\\d+)?|\\d*\\.\\d+)([eE][+-]?\\d+)?$"
    );
    private static final Set<String> NUMERIC_COLUMN_HINTS = Set.of(
            "effectif", "nombre", "total", "montant", "prix", "quantité", "quantite",
            "somme", "valeur", "score", "taux", "pourcentage", "ca", "chiffre",
            "count", "amount", "price", "quantity", "sum", "value", "rate", "percent"
    );

    /** Normalise une chaîne avant test numérique (espaces insécables, virgule décimale, symboles). */
    private String normalizeForNumeric(String s) {
        if (s == null || s.isBlank()) return "";
        s = s.trim();
        s = s.replace("\u00A0", "").replace("\u202F", "").replace(" ", "");
        s = s.replace(",", ".");
        s = s.replaceAll("[€$%\\s]+$", "");
        return s;
    }

    /** Vérifie si une valeur (Number ou String) représente un nombre. */
    private boolean looksNumeric(Object val) {
        if (val instanceof Number) return true;
        String s = normalizeForNumeric(val.toString());
        if (s.isEmpty()) return false;
        if (NUMERIC_PATTERN.matcher(s).matches()) return true;
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Vrai si le nom de colonne suggère une quantité (Effectif, Montant, etc.). */
    private boolean columnNameSuggestsNumeric(String colName) {
        if (colName == null || colName.isBlank()) return false;
        String lower = colName.toLowerCase().replaceAll("[^a-zàâäéèêëïîôùûüç]", "");
        return NUMERIC_COLUMN_HINTS.stream().anyMatch(hint -> lower.contains(hint));
    }

    /** Infère le type (nombre/texte) de chaque colonne via échantillonnage et indice nom. */
    private void inferColumnTypes(List<Map<String, String>> columns, List<Map<String, Object>> rows) {
        if (columns.isEmpty() || rows.isEmpty()) return;
        int sampleSize = Math.min(SAMPLE_SIZE, rows.size());
        for (int colIdx = 0; colIdx < columns.size(); colIdx++) {
            String colName = columns.get(colIdx).get("nom");
            if (colName == null) continue;
            int numericCount = 0;
            int totalCount = 0;
            for (int r = 0; r < sampleSize; r++) {
                Object val = rows.get(r).get(colName);
                if (val == null || (val instanceof String s && s.isBlank())) continue;
                totalCount++;
                if (looksNumeric(val)) numericCount++;
            }
            double ratio = totalCount > 0 ? (double) numericCount / totalCount : 0;
            boolean strictOk = ratio >= RATIO_NUMERIC_STRICT;
            boolean relaxedOk = ratio >= RATIO_NUMERIC_RELAXED && columnNameSuggestsNumeric(colName);
            String type = (strictOk || relaxedOk) ? "nombre" : "texte";
            columns.set(colIdx, Map.of("nom", colName, "type", type));
        }
    }

    private String getCellString(Cell c) {
        if (c == null) return "";
        return switch (c.getCellType()) {
            case STRING -> c.getStringCellValue();
            case NUMERIC -> new DataFormatter().formatCellValue(c);
            case BOOLEAN -> String.valueOf(c.getBooleanCellValue());
            case FORMULA -> c.getCellFormula();
            default -> "";
        };
    }

    private Object getCellValue(Cell c) {
        if (c == null) return "";
        return switch (c.getCellType()) {
            case STRING -> c.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(c)) {
                    yield c.getLocalDateTimeCellValue().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
                double d = c.getNumericCellValue();
                yield (d == (long) d) ? (long) d : d;
            }
            case BOOLEAN -> c.getBooleanCellValue();
            case FORMULA -> c.getCellFormula();
            default -> "";
        };
    }

    @Override
    public List<DataImportDTO> listByTenant(Long tenantId) {
        return dataImportRepository.findByTenantIdOrderByCreatedAtDesc(tenantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ImportPreviewDTO getPreview(Long importId, int limit) {
        DataImport imp = dataImportRepository.findById(importId)
                .orElseThrow(() -> new IllegalArgumentException("Import non trouvé"));
        List<String> colNames = imp.getColumns() != null
                ? imp.getColumns().stream().map(m -> m.get("nom")).filter(Objects::nonNull).collect(Collectors.toList())
                : List.of();
        List<DataImportRow> rowList = dataImportRowRepository.findByImportIdOrderByRowIndexAsc(importId, PageRequest.of(0, limit > 0 ? limit : 100));
        List<Map<String, Object>> rows = rowList.stream().map(DataImportRow::getData).collect(Collectors.toList());
        return ImportPreviewDTO.builder()
                .importId(importId)
                .columns(colNames)
                .rows(rows)
                .build();
    }

    /** Retourne les colonnes avec types ré-inférés à la volée (pas les types persistés) pour cohérence avec l'algo actuel. */
    @Override
    public List<Map<String, String>> getColumns(Long importId) {
        DataImport imp = dataImportRepository.findById(importId)
                .orElseThrow(() -> new IllegalArgumentException("Import non trouvé"));
        List<Map<String, String>> columns = imp.getColumns() != null ? new ArrayList<>(imp.getColumns()) : new ArrayList<>();
        if (columns.isEmpty()) return List.of();
        List<Map<String, Object>> rows = dataImportRowRepository
                .findByImportIdOrderByRowIndexAsc(importId, PageRequest.of(0, SAMPLE_SIZE))
                .stream()
                .map(DataImportRow::getData)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!rows.isEmpty()) inferColumnTypes(columns, rows);
        return columns;
    }

    @Override
    public Object getImportAggregate(Long importId, String columnName, String aggregation) {
        DataImport imp = dataImportRepository.findById(importId)
                .orElseThrow(() -> new IllegalArgumentException("Import non trouvé"));
        List<DataImportRow> rows = dataImportRowRepository.findByImportIdOrderByRowIndexAsc(importId);
        String agg = (aggregation != null ? aggregation : "sum").toLowerCase();
        List<Double> nums = new ArrayList<>();
        int count = 0;
        for (DataImportRow row : rows) {
            Object val = row.getData() != null ? row.getData().get(columnName) : null;
            if (val == null) continue;
            if (val instanceof Number n) {
                nums.add(n.doubleValue());
                count++;
            } else {
                String s = val.toString().trim().replace(" ", "").replace(",", ".");
                if (!s.isEmpty() && s.matches("-?[0-9]+(\\.[0-9]+)?")) {
                    nums.add(Double.parseDouble(s));
                    count++;
                } else {
                    count++;
                }
            }
        }
        return switch (agg) {
            case "avg" -> nums.isEmpty() ? 0.0 : nums.stream().mapToDouble(Double::doubleValue).sum() / nums.size();
            case "count" -> count;
            case "min" -> nums.isEmpty() ? null : nums.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            case "max" -> nums.isEmpty() ? null : nums.stream().mapToDouble(Double::doubleValue).max().orElse(0);
            default -> nums.stream().mapToDouble(Double::doubleValue).sum();
        };
    }

    @Override
    @Transactional
    public void deleteImport(Long importId) {
        DataImport imp = dataImportRepository.findById(importId)
                .orElseThrow(() -> new IllegalArgumentException("Import non trouvé"));
        for (Dataset d : datasetRepository.findByImportId(importId)) {
            d.setImportId(null);
            datasetRepository.save(d);
        }
        dataImportRowRepository.deleteByImportId(importId);
        dataImportRepository.delete(imp);
    }

    private DataImportDTO toDTO(DataImport imp) {
        return DataImportDTO.builder()
                .id(imp.getId())
                .nomFichier(imp.getNomFichier())
                .typeFichier(imp.getTypeFichier())
                .tenantId(imp.getTenantId())
                .rowCount(imp.getRowCount())
                .columns(imp.getColumns())
                .createdAt(imp.getCreatedAt() != null ? imp.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null)
                .build();
    }
}
