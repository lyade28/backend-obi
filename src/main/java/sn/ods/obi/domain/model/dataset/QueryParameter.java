package sn.ods.obi.domain.model.dataset;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_QUERY_PARAMETER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_param", initialValue = 1, allocationSize = 1, sequenceName = "seq_param")
public class QueryParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_param")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "param_nom")
    private String nom;

    @Size(max = 50)
    @Column(name = "param_type")
    private String type = "texte";

    @Size(max = 200)
    @Column(name = "param_defaut")
    private String defaut;

    @Column(name = "query_id")
    private Long queryId;
}
