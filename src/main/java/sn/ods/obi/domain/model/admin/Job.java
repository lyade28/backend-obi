package sn.ods.obi.domain.model.admin;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_JOB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_job", initialValue = 1, allocationSize = 1, sequenceName = "seq_job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_job")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "job_nom", nullable = false)
    private String nom;

    @Size(max = 50)
    @Column(name = "job_type")
    private String type = "Import";

    @Size(max = 50)
    @Column(name = "job_frequence")
    private String frequence = "Quotidien";

    @Size(max = 20)
    @Column(name = "job_statut")
    private String statut = "OK";

    @Column(name = "job_dernier_run")
    private java.time.LocalDateTime dernierRun;
}
