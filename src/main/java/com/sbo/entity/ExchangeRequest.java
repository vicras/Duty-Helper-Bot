package com.sbo.entity;

import com.sbo.entity.enums.ExchangeRequestState;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;

@Data
@Entity
@NoArgsConstructor
@Table(name = "exchange")
public class ExchangeRequest extends BaseEntity {

    private DutyIntervalData authorIntervalData;
    private DutyIntervalData recipientIntervalData;

    @Enumerated(STRING)
    @CollectionTable(name = "exchange_state")
    private ExchangeRequestState exchangeRequestState = ExchangeRequestState.SENT;

    @Builder
    public ExchangeRequest(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, DutyIntervalData authorIntervalData, DutyIntervalData recipientIntervalData) {
        super(id, createdAt, updatedAt);
        this.authorIntervalData = authorIntervalData;
        this.recipientIntervalData = recipientIntervalData;
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
                        exchangeRequest.getRecipientIntervalData().getPeopleOnDuty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                authorIntervalData.getPeopleOnDuty(),
                recipientIntervalData.getPeopleOnDuty()
        );
    }
}
