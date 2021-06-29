package com.sbo.entity;

import com.sbo.entity.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.time.LocalDateTime;

/**
 * @author viktar hraskou
 */
@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Version
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "entity_status")
    private EntityStatus entityStatus;

    @PrePersist
    private void setCreationDate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void setChangeDate() {
        this.updatedAt = LocalDateTime.now();
    }

}

