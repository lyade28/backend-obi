package sn.ods.obi.domain.model.other;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "TD_DisposableEmail")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DisposableEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Dis_Id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "Dis_Domain")
    private String domain;
}
