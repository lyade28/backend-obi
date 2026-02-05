package sn.ods.obi.domain.model.favori;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_FAVORI")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_favori", initialValue = 1, allocationSize = 1, sequenceName = "seq_favori")
public class Favori {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_favori")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 20)
    @Column(name = "favori_type")
    private String type;

    @Size(max = 100)
    @Column(name = "favori_entity_id")
    private String entityId;

    @Size(max = 200)
    @Column(name = "favori_label")
    private String label;

    @Size(max = 500)
    @Column(name = "favori_url")
    private String url;

    @Column(name = "user_id")
    private Long userId;
}
