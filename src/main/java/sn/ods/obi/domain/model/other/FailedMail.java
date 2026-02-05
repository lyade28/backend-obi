package sn.ods.obi.domain.model.other;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Table(name = "TD_FailedMail")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Where(clause = "Fai_isSent = false")
public class FailedMail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Fai_Id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "Fai_Email")
    private String email;

    @Column(name = "Fai_Subject")
    private String subject;

    @Column(name = "Fai_Text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "Fai_isSent", columnDefinition = "boolean default false")
    private Boolean isSent;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name = "Fai_CreatedDate", updatable = false)
    protected Date createdDate;
}
