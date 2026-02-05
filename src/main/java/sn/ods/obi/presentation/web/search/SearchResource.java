package sn.ods.obi.presentation.web.search;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.search.SearchService;
import sn.ods.obi.presentation.dto.responses.APIResponse;

import java.util.Map;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchResource {

    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "Recherche globale")
    public ResponseEntity<APIResponse> search(@RequestParam String q, @RequestParam Long tenantId) {
        Map<String, Object> result = searchService.search(q, tenantId);
        return ResponseEntity.ok(APIResponse.success(result));
    }
}
