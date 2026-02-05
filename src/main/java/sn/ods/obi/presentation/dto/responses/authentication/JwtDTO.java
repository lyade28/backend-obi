package sn.ods.obi.presentation.dto.responses.authentication;


import sn.ods.obi.domain.model.utilisateur.Menu;

import java.util.Set;

public record JwtDTO(String username, String token, String  refreshToken, String type, Set<Menu> menus) {
}
