package com.sbo.domain.postgres.entity;

import com.sbo.domain.postgres.entity.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.sbo.domain.postgres.entity.enums.EntityStatus.ACTIVE;
import static javax.persistence.EnumType.STRING;

/**
 * @author viktar hraskou
 */
@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Version
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(STRING)
    @Column(name = "entity_status")
    private EntityStatus entityStatus = ACTIVE;

    @PrePersist
    private void setCreationDate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void setChangeDate() {
        this.updatedAt = LocalDateTime.now();
    }

}

