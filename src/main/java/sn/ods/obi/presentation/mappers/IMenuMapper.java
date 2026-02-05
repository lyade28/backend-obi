
package sn.ods.obi.presentation.mappers;

import org.mapstruct.Mapper;
import sn.ods.obi.domain.model.utilisateur.Menu;
import sn.ods.obi.presentation.dto.requests.utilisateur.MenuDTO;


@Mapper
public interface IMenuMapper {
    MenuDTO menuToMenuDTO(Menu menu);

}

