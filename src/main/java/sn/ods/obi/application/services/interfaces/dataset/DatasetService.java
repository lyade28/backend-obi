package sn.ods.obi.application.services.interfaces.dataset;

import sn.ods.obi.presentation.dto.dataset.*;

import java.util.List;
import java.util.Map;

public interface DatasetService {
    List<SavedQueryDTO> getSavedQueries(Long tenantId);
    SavedQueryDTO getQuery(Long id);
    SavedQueryDTO saveQuery(SavedQueryDTO dto);
    SavedQueryDTO updateQuery(Long id, SavedQueryDTO dto);
    void deleteQuery(Long id);

    List<DatasetDTO> getDatasets(Long tenantId);
    DatasetDTO createDataset(DatasetDTO dto);
    DatasetDTO updateDataset(Long id, DatasetDTO dto);
    void deleteDataset(Long id);

    List<QueryParameterDTO> getParams(Long queryId);
    QueryParameterDTO addParam(QueryParameterDTO dto);

    QueryResultDTO executeQuery(Long connexionId, String sql, Map<String, Object> params);

    /** Colonnes d'un dataset (selon import ou requête) — nom + type (nombre/texte) pour choix agrégation */
    List<Map<String, String>> getDatasetColumnInfos(Long datasetId);

    /** Colonnes d'une requête sauvegardée (exécution LIMIT 0) — nom + type pour choix agrégation */
    List<Map<String, String>> getQueryColumnInfos(Long queryId);

    /** Données d'un dataset (selon import ou requête, limité à 500 lignes) */
    QueryResultDTO getDatasetData(Long datasetId, int limit);

    /** Valeur agrégée d'une colonne (SUM, AVG, COUNT, MIN, MAX) sur tout le dataset */
    Object getDatasetAggregate(Long datasetId, String columnName, String aggregation);

    /** Valeur agrégée sur une requête SQL */
    Object getQueryAggregate(Long queryId, String columnName, String aggregation);
}
