package sn.ods.obi.infrastructure.logging;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity // Cette annotation est obligatoire pour que JPA reconnaisse la classe comme une entité
public class ErrorLog {

    // Getters et Setters (obligatoires pour JPA)
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "seq_logs")
    private Long id;

    private String message;

    @Lob
    private String stackTrace;

    private LocalDateTime timestamp;

}