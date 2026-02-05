package sn.ods.obi.domain.model.connexion;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_CONNEXION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_connexion", initialValue = 1, allocationSize = 1, sequenceName = "seq_connexion")
public class Connexion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_connexion")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "conn_nom", nullable = false)
    private String nom;

    @Size(max = 50)
    @Column(name = "conn_type")
    private String type = "PostgreSQL";

    @Size(max = 255)
    @Column(name = "conn_host")
    private String host;

    @Column(name = "conn_port")
    private Integer port = 5432;

    @Size(max = 100)
    @Column(name = "conn_database")
    private String database;

    @Size(max = 100)
    @Column(name = "conn_schema")
    private String schema = "public";

    @Size(max = 200)
    @Column(name = "conn_username")
    private String username;

    @Size(max = 500)
    @Column(name = "conn_password")
    private String password;

    @Column(name = "tenant_id")
    private Long tenantId;

    /** Statut d'accessibilité de la base (OK, Erreur, Non vérifié) - distinct de la création */
    @Column(name = "conn_statut_db", length = 20)
    private String statutDb = "Non vérifié";

    /** Date/heure de la dernière vérification de connexion à la base */
    @Column(name = "conn_derniere_verif_db")
    private java.time.LocalDateTime derniereVerifDb;
}
