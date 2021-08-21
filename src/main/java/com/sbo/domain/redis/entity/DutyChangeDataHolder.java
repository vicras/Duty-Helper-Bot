package com.sbo.domain.redis.entity;

import com.google.common.collect.Range;
import com.sbo.domain.postgres.entity.PeopleOnDuty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author viktar hraskou
 */
@Data
@Builder
public class DutyChangeDataHolder implements Serializable {
    private PeopleOnDuty fromDutyId;
    private Range<LocalDateTime> fromRange;
    private PeopleOnDuty toDutyId;
    private Range<LocalDateTime> toRange;
}
