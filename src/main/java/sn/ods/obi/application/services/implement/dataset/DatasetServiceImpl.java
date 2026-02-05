package sn.ods.obi.application.services.implement.dataset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ods.obi.domain.model.connexion.Connexion;
import sn.ods.obi.domain.model.dataset.Dataset;
import sn.ods.obi.domain.model.dataset.QueryParameter;
import sn.ods.obi.domain.model.dataset.SavedQuery;
import sn.ods.obi.domain.repository.connexion.ConnexionRepository;
import sn.ods.obi.domain.repository.dataset.DatasetRepository;
import sn.ods.obi.domain.repository.dataset.QueryParameterRepository;
import sn.ods.obi.domain.repository.dataset.SavedQueryRepository;
import sn.ods.obi.application.services.interfaces.dataset.DatasetService;
import sn.ods.obi.application.services.interfaces.import_.DataImportService;
import sn.ods.obi.presentation.dto.dataset.*;
import sn.ods.obi.presentation.dto.import_.ImportPreviewDTO;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatasetServiceImpl implements DatasetService {

    private final SavedQueryRepository savedQueryRepository;
    private final DatasetRepository datasetRepository;
    private final QueryParameterRepository queryParameterRepository;
    private final ConnexionRepository connexionRepository;
    private final DataImportService dataImportService;

    @Override
    public List<SavedQueryDTO> getSavedQueries(Long tenantId) {
        return savedQueryRepository.findByTenantId(tenantId).stream()
                .map(this::toQueryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SavedQueryDTO getQuery(Long id) {
        return savedQueryRepository.findById(id)
                .map(this::toQueryDTO)
                .orElseThrow(() -> new IllegalArgumentException("Requête non trouvée"));
    }

    @Override
    @Transactional
    public SavedQueryDTO saveQuery(SavedQueryDTO dto) {
        SavedQuery q = SavedQuery.builder()
                .nom(dto.getNom())
                .sql(dto.getSql())
                .connexionId(dto.getConnexionId())
                .tenantId(dto.getTenantId())
                .build();
        q = savedQueryRepository.save(q);
        return toQueryDTO(q);
    }

    @Override
    @Transactional
    public SavedQueryDTO updateQuery(Long id, SavedQueryDTO dto) {
        SavedQuery q = savedQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requête non trouvée"));
        if (dto.getNom() != null) q.setNom(dto.getNom());
        if (dto.getSql() != null) q.setSql(dto.getSql());
        if (dto.getConnexionId() != null) q.setConnexionId(dto.getConnexionId());
        q = savedQueryRepository.save(q);
        return toQueryDTO(q);
    }

    @Override
    @Transactional
    public void deleteQuery(Long id) {
        savedQueryRepository.deleteById(id);
    }

    @Override
    public List<DatasetDTO> getDatasets(Long tenantId) {
        return datasetRepository.findByTenantId(tenantId).stream()
                .map(this::toDatasetDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DatasetDTO createDataset(DatasetDTO dto) {
        Dataset d = Dataset.builder()
                .nom(dto.getName())
                .description(dto.getDescription())
                .sourceType(dto.getSourceType())
                .sourceLabel(dto.getSourceLabel())
                .queryId(dto.getQueryId())
                .connexionId(dto.getConnexionId())
                .importId(dto.getImportId())
                .tenantId(dto.getTenantId())
                .build();
        d = datasetRepository.save(d);
        return toDatasetDTO(d);
    }

    @Override
    @Transactional
    public DatasetDTO updateDataset(Long id, DatasetDTO dto) {
        Dataset d = datasetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dataset non trouvé"));
        if (dto.getName() != null) d.setNom(dto.getName());
        if (dto.getDescription() != null) d.setDescription(dto.getDescription());
        d = datasetRepository.save(d);
        return toDatasetDTO(d);
    }

    @Override
    @Transactional
    public void deleteDataset(Long id) {
        datasetRepository.deleteById(id);
    }

    @Override
    public List<QueryParameterDTO> getParams(Long queryId) {
        return queryParameterRepository.findByQueryId(queryId).stream()
                .map(p -> QueryParameterDTO.builder()
                        .id(p.getId())
                        .nom(p.getNom())
                        .type(p.getType())
                        .defaut(p.getDefaut())
                        .queryId(p.getQueryId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QueryParameterDTO addParam(QueryParameterDTO dto) {
        QueryParameter p = QueryParameter.builder()
                .nom(dto.getNom())
                .type(dto.getType() != null ? dto.getType() : "texte")
                .defaut(dto.getDefaut())
                .queryId(dto.getQueryId())
                .build();
        p = queryParameterRepository.save(p);
        return QueryParameterDTO.builder().id(p.getId()).nom(p.getNom()).type(p.getType()).defaut(p.getDefaut()).queryId(p.getQueryId()).build();
    }

    @Override
    public QueryResultDTO executeQuery(Long connexionId, String sql, Map<String, Object> params) {
        Connexion c = connexionRepository.findById(connexionId)
                .orElseThrow(() -> new IllegalArgumentException("Connexion non trouvée"));
        String connType = c.getType() != null ? c.getType().trim() : "";
        if (!connType.equalsIgnoreCase("PostgreSQL") && !connType.equalsIgnoreCase("Postgres")) {
            throw new IllegalArgumentException("Type de connexion non supporté pour l'exécution SQL : " + connType + ". PostgreSQL requis.");
        }
        String schema = c.getSchema() != null ? c.getSchema() : "public";
        String url = String.format("jdbc:postgresql://%s:%d/%s?currentSchema=%s",
                c.getHost(), c.getPort(), c.getDatabase(), schema);
        String pwd = c.getPassword() == null ? null : new String(Base64.getDecoder().decode(c.getPassword()), StandardCharsets.UTF_8);

        List<String> columns = new ArrayList<>();
        List<List<Object>> rows = new ArrayList<>();
        String execSql = sql;
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                execSql = execSql.replace(":" + e.getKey(), String.valueOf(e.getValue()));
            }
        }
        try (Connection conn = DriverManager.getConnection(url, c.getUsername(), pwd);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(execSql)) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                columns.add(meta.getColumnName(i));
            }
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getObject(i));
                }
                rows.add(row);
            }
        } catch (Exception e) {
            log.warn("Query execution failed: {}", e.getMessage());
            throw new RuntimeException("Erreur d'exécution SQL : " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()), e);
        }
        return QueryResultDTO.builder().columns(columns).rows(rows).build();
    }

    @Override
    public List<Map<String, String>> getDatasetColumnInfos(Long datasetId) {
        Dataset d = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset non trouvé"));
        if (d.getImportId() != null) {
            return dataImportService.getColumns(d.getImportId());
        }
        if (d.getQueryId() != null) {
            return getQueryColumnInfos(d.getQueryId());
        }
        return List.of();
    }

    /** Enlève le point-virgule final pour éviter une erreur de syntaxe dans SELECT * FROM (sql) _q */
    private String sqlForSubquery(String sql) {
        if (sql == null || sql.isBlank()) return sql;
        return sql.trim().replaceAll(";\\s*$", "");
    }

    /** Mappe le type SQL JDBC vers "nombre" ou "texte" pour choix agrégation (sum/avg vs count) */
    private String sqlTypeToAggregationType(int sqlType) {
        return switch (sqlType) {
            case Types.INTEGER, Types.BIGINT, Types.SMALLINT, Types.TINYINT,
                 Types.NUMERIC, Types.DECIMAL, Types.FLOAT, Types.DOUBLE, Types.REAL -> "nombre";
            default -> "texte";
        };
    }

    @Override
    public List<Map<String, String>> getQueryColumnInfos(Long queryId) {
        SavedQuery q = savedQueryRepository.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException("Requête non trouvée"));
        String sql = q.getSql();
        if (sql == null || sql.isBlank()) return List.of();
        String limitedSql = "SELECT * FROM (" + sqlForSubquery(sql) + ") _q LIMIT 0";
        return getColumnInfosFromQuery(q.getConnexionId(), limitedSql);
    }

    /** Exécute une requête LIMIT 0 et extrait nom + type de chaque colonne via ResultSetMetaData */
    private List<Map<String, String>> getColumnInfosFromQuery(Long connexionId, String sql) {
        Connexion c = connexionRepository.findById(connexionId)
                .orElseThrow(() -> new IllegalArgumentException("Connexion non trouvée"));
        String connType = c.getType() != null ? c.getType().trim() : "";
        if (!connType.equalsIgnoreCase("PostgreSQL") && !connType.equalsIgnoreCase("Postgres")) {
            return List.of();
        }
        String schema = c.getSchema() != null ? c.getSchema() : "public";
        String url = String.format("jdbc:postgresql://%s:%d/%s?currentSchema=%s",
                c.getHost(), c.getPort(), c.getDatabase(), schema);
        String pwd = c.getPassword() == null ? null : new String(Base64.getDecoder().decode(c.getPassword()), StandardCharsets.UTF_8);
        List<Map<String, String>> infos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, c.getUsername(), pwd);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String nom = meta.getColumnName(i);
                String type = sqlTypeToAggregationType(meta.getColumnType(i));
                infos.add(Map.of("nom", nom, "type", type));
            }
        } catch (Exception e) {
            log.warn("Could not fetch column infos: {}", e.getMessage());
        }
        return infos;
    }

    /** Limite max pour les données (graphiques nécessitent plus de lignes : années × niveaux × établissements) */
    private static final int MAX_DATA_LIMIT = 50000;

    @Override
    public QueryResultDTO getDatasetData(Long datasetId, int limit) {
        int safeLimit = Math.min(Math.max(limit, 1), MAX_DATA_LIMIT);
        Dataset d = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset non trouvé"));
        if (d.getImportId() != null) {
            ImportPreviewDTO preview = dataImportService.getPreview(d.getImportId(), safeLimit);
            List<String> cols = preview.getColumns() != null ? preview.getColumns() : List.of();
            List<List<Object>> rows = preview.getRows() != null
                    ? preview.getRows().stream()
                            .map(m -> cols.stream().map(c -> m.getOrDefault(c, (Object) "")).toList())
                            .collect(Collectors.toList())
                    : List.of();
            return QueryResultDTO.builder().columns(cols).rows(rows).build();
        }
        if (d.getQueryId() != null) {
            SavedQuery q = savedQueryRepository.findById(d.getQueryId())
                    .orElseThrow(() -> new IllegalArgumentException("Requête non trouvée"));
            String sql = q.getSql();
            if (sql == null || sql.isBlank()) return QueryResultDTO.builder().columns(List.of()).rows(List.of()).build();
            String limitedSql = "SELECT * FROM (" + sqlForSubquery(sql) + ") _q LIMIT " + safeLimit;
            return executeQuery(q.getConnexionId(), limitedSql, Map.of());
        }
        return QueryResultDTO.builder().columns(List.of()).rows(List.of()).build();
    }

    @Override
    public Object getDatasetAggregate(Long datasetId, String columnName, String aggregation) {
        Dataset d = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset non trouvé"));
        if (d.getImportId() != null) {
            return dataImportService.getImportAggregate(d.getImportId(), columnName, aggregation);
        }
        if (d.getQueryId() != null) {
            return getQueryAggregate(d.getQueryId(), columnName, aggregation);
        }
        return null;
    }

    @Override
    public Object getQueryAggregate(Long queryId, String columnName, String aggregation) {
        SavedQuery q = savedQueryRepository.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException("Requête non trouvée"));
        String sql = q.getSql();
        if (sql == null || sql.isBlank() || columnName == null || columnName.isBlank()) return null;
        String agg = (aggregation != null ? aggregation : "sum").toUpperCase();
        if (!List.of("SUM", "AVG", "COUNT", "MIN", "MAX").contains(agg)) agg = "SUM";
        for (String colForm : List.of(
                "\"" + columnName.replace("\"", "\"\"") + "\"",
                columnName.toLowerCase().replace("\"", ""))) {
            try {
                String aggSql = "SELECT " + agg + "(" + colForm + ") AS v FROM (" + sqlForSubquery(sql) + ") _q";
                QueryResultDTO result = executeQuery(q.getConnexionId(), aggSql, Map.of());
                if (result != null && result.getRows() != null && !result.getRows().isEmpty()) {
                    Object val = result.getRows().get(0).get(0);
                    return val != null ? val : 0;
                }
            } catch (Exception e) {
                if (colForm.contains("\"")) log.debug("Aggregate with quoted column failed, retrying: {}", e.getMessage());
            }
        }
        return null;
    }

    private SavedQueryDTO toQueryDTO(SavedQuery q) {
        return SavedQueryDTO.builder()
                .id(q.getId())
                .nom(q.getNom())
                .sql(q.getSql())
                .connexionId(q.getConnexionId())
                .tenantId(q.getTenantId())
                .build();
    }

    private DatasetDTO toDatasetDTO(Dataset d) {
        return DatasetDTO.builder()
                .id(d.getId())
                .name(d.getNom())
                .description(d.getDescription())
                .sourceType(d.getSourceType())
                .sourceLabel(d.getSourceLabel())
                .queryId(d.getQueryId())
                .connexionId(d.getConnexionId())
                .importId(d.getImportId())
                .tenantId(d.getTenantId())
                .updatedAt(java.time.LocalDate.now().toString())
                .build();
    }
}
