package sn.ods.obi.application.services.interfaces.favori;

import sn.ods.obi.presentation.dto.favori.FavoriDTO;

import java.util.List;

public interface FavoriService {
    List<FavoriDTO> getFavoris(Long userId);
    FavoriDTO addFavori(FavoriDTO dto);
    void removeFavori(Long userId, String type, String entityId);
}
