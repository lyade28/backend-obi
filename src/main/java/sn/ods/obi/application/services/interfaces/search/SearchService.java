package sn.ods.obi.application.services.interfaces.search;

import java.util.Map;

public interface SearchService {
    Map<String, Object> search(String query, Long tenantId);
}
