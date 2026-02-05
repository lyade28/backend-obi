package sn.ods.obi.application.services.implement.favori;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ods.obi.domain.model.favori.Favori;
import sn.ods.obi.domain.repository.favori.FavoriRepository;
import sn.ods.obi.application.services.interfaces.favori.FavoriService;
import sn.ods.obi.presentation.dto.favori.FavoriDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriServiceImpl implements FavoriService {

    private final FavoriRepository favoriRepository;

    @Override
    public List<FavoriDTO> getFavoris(Long userId) {
        return favoriRepository.findByUserId(userId).stream()
                .map(f -> FavoriDTO.builder()
                        .id(f.getId())
                        .type(f.getType())
                        .entityId(f.getEntityId())
                        .label(f.getLabel())
                        .url(f.getUrl())
                        .userId(f.getUserId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FavoriDTO addFavori(FavoriDTO dto) {
        Favori f = Favori.builder()
                .type(dto.getType())
                .entityId(dto.getEntityId())
                .label(dto.getLabel())
                .url(dto.getUrl())
                .userId(dto.getUserId())
                .build();
        f = favoriRepository.save(f);
        return FavoriDTO.builder().id(f.getId()).type(f.getType()).entityId(f.getEntityId()).label(f.getLabel()).url(f.getUrl()).userId(f.getUserId()).build();
    }

    @Override
    @Transactional
    public void removeFavori(Long userId, String type, String entityId) {
        favoriRepository.deleteByUserIdAndTypeAndEntityId(userId, type, entityId);
    }
}
