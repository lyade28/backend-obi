package sn.ods.obi.domain.model.utilisateur;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * @author Abdou Karim CISSOKHO
 * @created 17/01/2025-11:43
 * @project obi
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "TD_VALIDATION_USER")
@SequenceGenerator(name = "seq_validate_user", initialValue = 100, allocationSize = 2, sequenceName = "seq_validate_user")
public class ValidationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_validate_user")
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    private Instant creation;
    private Instant expiration;
    private Instant activation;
    private String code;

    @OneToOne(cascade = CascadeType.ALL)
    private Utilisateur user;
}
