package sn.ods.obi.application.services.interfaces.import_;

import org.springframework.web.multipart.MultipartFile;
import sn.ods.obi.presentation.dto.import_.DataImportDTO;
import sn.ods.obi.presentation.dto.import_.ImportPreviewDTO;

import java.util.List;

public interface DataImportService {
    /** Importe un fichier Excel (.xlsx, .xls) et stocke les données en base */
    DataImportDTO importExcel(MultipartFile file, Long tenantId, boolean firstRowHeaders);

    /** Importe un fichier CSV et stocke les données en base */
    DataImportDTO importCsv(MultipartFile file, Long tenantId, String csvDelimiter, String csvEncoding, boolean firstRowHeaders);

    /** Importe un fichier Excel ou CSV et stocke les données en base (détection auto par extension) */
    DataImportDTO importFile(MultipartFile file, Long tenantId, String csvDelimiter, String csvEncoding, boolean firstRowHeaders);

    /** Liste des imports par tenant */
    List<DataImportDTO> listByTenant(Long tenantId);

    /** Aperçu des données (premières lignes) */
    ImportPreviewDTO getPreview(Long importId, int limit);

    /** Colonnes de l'import */
    List<java.util.Map<String, String>> getColumns(Long importId);

    /** Supprimer un import et ses lignes */
    void deleteImport(Long importId);

    /** Valeur agrégée (SUM, AVG, COUNT, MIN, MAX) sur une colonne de l'import */
    Object getImportAggregate(Long importId, String columnName, String aggregation);
}
