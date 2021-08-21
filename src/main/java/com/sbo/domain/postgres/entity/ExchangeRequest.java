package com.sbo.domain.postgres.entity;

import com.sbo.domain.postgres.entity.enums.EntityStatus;
import com.sbo.domain.postgres.entity.enums.ExchangeRequestState;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.hash;
import static javax.persistence.EnumType.STRING;

@Data
@Entity
@NoArgsConstructor
@Table(name = "exchange")
public class ExchangeRequest extends BaseEntity {

    private DutyIntervalData authorIntervalData;

    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "from_recipient")),
            @AttributeOverride(name = "to", column = @Column(name = "to_recipient"))
    })
    private DutyIntervalData recipientIntervalData;

    @Enumerated(STRING)
    @CollectionTable(name = "exchange_state")
    private ExchangeRequestState exchangeRequestState = ExchangeRequestState.SENT;

    @Builder
    public ExchangeRequest(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, EntityStatus entityStatus, DutyIntervalData authorIntervalData, DutyIntervalData recipientIntervalData, ExchangeRequestState exchangeRequestState) {
        super(id, createdAt, updatedAt, entityStatus);
        this.authorIntervalData = authorIntervalData;
        this.recipientIntervalData = recipientIntervalData;
        this.exchangeRequestState = exchangeRequestState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRequest exchangeRequest = (ExchangeRequest) o;
        return Objects.equals(
                authorIntervalData.getPeopleOnDuty(),
                exchangeRequest.getAuthorIntervalData().getPeopleOnDuty()) &&
                Objects.equals(
                        recipientIntervalData.getPeopleOnDuty(),
                        exchangeRequest.getRecipientIntervalData().getPeopleOnDuty()
                );
    }

    @Override
    public int hashCode() {
        return hash(
                super.hashCode(),
                authorIntervalData.getPeopleOnDuty(),
                recipientIntervalData.getPeopleOnDuty()
        );
    }
}
