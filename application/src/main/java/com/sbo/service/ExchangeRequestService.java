package com.sbo.service;

import com.sbo.domain.postgres.entity.DutyIntervalData;
import com.sbo.domain.postgres.entity.ExchangeRequest;
import com.sbo.domain.postgres.entity.Person;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRequestService {
    boolean equalsExchangeExists(ExchangeRequest exchangeRequest);

    boolean currentPersonIsAuthorOf(ExchangeRequest exchangeRequest);

    void removeMessagesOfPersonOnExcept(DutyIntervalData dutyIntervalData, ExchangeRequest addedRequest);

    List<ExchangeRequest> getPersonToOtherRequests(Person currentUser);

    List<ExchangeRequest> getPersonIncomingRequests(Person currentUser);

    List<ExchangeRequest> getExchangeRequestsHistory(int maxNumber);

    List<ExchangeRequest> getExchangeRequestsHistoryAfter(LocalDate localDate);
}
