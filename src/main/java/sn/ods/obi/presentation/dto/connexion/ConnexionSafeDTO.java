package sn.ods.obi.presentation.dto.connexion;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnexionSafeDTO {
    private Long id;
    private String nom;
    private String type;
    private String host;
    private Integer port;
    private String database;
    private String schema;
    private String username;
    private Long tenantId;
    /** Statut d'accessibilité de la base : OK, Erreur, Non vérifié */
    private String statutDb;
    /** Date de la dernière vérification de connexion */
    private String derniereVerifDb;
}
