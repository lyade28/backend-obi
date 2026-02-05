package sn.ods.obi.domain.model.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

/**
 * @author G2k R&D
 */

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {

    @CreatedBy
    @Column(name = "Uti_CreatedBy", updatable = false)
    protected T createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name = "Uti_CreatedDate", updatable = false)
    protected Date createdDate;

    @LastModifiedBy
    @Column(name = "Uti_ModifiedBy")
    protected T lastModifiedBy;

    @LastModifiedDate
    @Column(name = "Uti_ModifiedDate")
    protected Date lastModifiedDate;

}
