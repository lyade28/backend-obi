package sn.ods.obi.presentation.web.favori;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.favori.FavoriService;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.favori.FavoriDTO;

import java.util.List;

@RestController
@RequestMapping("/favoris")
@RequiredArgsConstructor
public class FavoriResource {

    private final FavoriService favoriService;

    @GetMapping
    @Operation(summary = "Liste des favoris")
    public ResponseEntity<APIResponse> getFavoris(@RequestParam Long userId) {
        List<FavoriDTO> list = favoriService.getFavoris(userId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @PostMapping
    @Operation(summary = "Ajouter un favori")
    public ResponseEntity<APIResponse> addFavori(@RequestBody FavoriDTO dto) {
        FavoriDTO created = favoriService.addFavori(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un favori")
    public ResponseEntity<APIResponse> removeFavori(@RequestParam Long userId, @RequestParam String type, @RequestParam String entityId) {
        favoriService.removeFavori(userId, type, entityId);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }
}
