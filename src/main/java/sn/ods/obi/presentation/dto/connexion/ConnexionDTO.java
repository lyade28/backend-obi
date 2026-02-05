package sn.ods.obi.presentation.dto.connexion;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnexionDTO {
    private Long id;
    private String nom;
    private String type;
    private String host;
    private Integer port;
    private String database;
    private String schema;
    private String username;
    private String password;
    private Long tenantId;
}
