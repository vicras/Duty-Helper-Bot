package com.sbo.service;

import com.sbo.entity.DutyIntervalData;
import com.sbo.entity.ExchangeRequest;
import com.sbo.entity.Person;

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
