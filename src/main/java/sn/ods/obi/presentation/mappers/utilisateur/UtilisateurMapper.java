package sn.ods.obi.presentation.mappers.utilisateur;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.presentation.dto.requests.utilisateur.UtilisateurReqDTO;
import sn.ods.obi.presentation.dto.responses.utilisateur.UtilisateurResDTO;
import sn.ods.obi.presentation.mappers.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Abdou Karim CISSOKHO
 * @created 09/01/2025-15:53
 * @project obi
 */

@Mapper
public interface UtilisateurMapper extends EntityMapper<Utilisateur, UtilisateurReqDTO, UtilisateurResDTO> {


    @Named("ignoreTraining")
    UtilisateurResDTO toDtoWithout(Utilisateur utilisateur);

    default List<UtilisateurResDTO> toDtoList(List<Utilisateur> entityList) {

        return entityList.stream().map(this::toDtoWithout).collect(Collectors.toList());
    }

    default Page<UtilisateurResDTO> toDtoPage(Page<Utilisateur> entityPage) {
        Pageable pageable = entityPage.getPageable();
        List<UtilisateurResDTO> dtoList = toDtoList(entityPage.getContent());
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }
}
