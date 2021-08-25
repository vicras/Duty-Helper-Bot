package com.sbo.service.impl;

import com.sbo.common.time.LocalDateTimeInterval;
import com.sbo.common.utils.StreamUtil;
import com.sbo.domain.postgres.entity.BaseEntity;
import com.sbo.domain.postgres.entity.DutyIntervalData;
import com.sbo.domain.postgres.entity.ExchangeRequest;
import com.sbo.domain.postgres.entity.Person;
import com.sbo.domain.postgres.entity.enums.ExchangeRequestState;
import com.sbo.domain.postgres.repository.ExchangeRequestRepository;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.ExchangeRequestService;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeRequestServiceImpl implements ExchangeRequestService {

    private final CurrentPersonProvider userProvider;
    private final ExchangeRequestRepository repository;
    private final PersonService personService;

    @Override
    public boolean equalsExchangeExists(ExchangeRequest exchangeRequest) {
        var currentPerson = userProvider.getCurrentPerson();
        var currentPersonRequests = getPersonToOtherRequests(currentPerson);
        return currentPersonRequests.stream()
                .anyMatch(existedRequest -> existedRequest.equals(exchangeRequest));
    }

    @Override
    public boolean currentPersonIsAuthorOf(ExchangeRequest exchangeRequest) {
        var currentPerson = userProvider.getCurrentPerson();
        return exchangeRequest.getAuthorIntervalData().getPeopleOnDuty().getPerson().equals(currentPerson);
    }

    @Override
    public void removeMessagesOfPersonOnExcept(DutyIntervalData dutyIntervalData, ExchangeRequest addedRequest) {
        var person = dutyIntervalData.getPeopleOnDuty().getPerson();
        var neededMessages = getSentMessagesFor(dutyIntervalData, person);
        neededMessages.addAll(getIncomeMessageFor(dutyIntervalData, person));
        neededMessages.forEach(myMessage -> {
            if (!myMessage.equals(addedRequest)) {
                myMessage.setExchangeRequestState(ExchangeRequestState.DECLINED);
                repository.save(myMessage);
            }
        });
    }

    @Override
    public List<ExchangeRequest> getPersonToOtherRequests(Person currentUser) {
        return StreamUtil.filter(repository.findAll(), exchangeRequest ->
                exchangeRequest.getAuthorIntervalData().getPeopleOnDuty().getPerson().equals(currentUser));
    }

    @Override
    public List<ExchangeRequest> getPersonIncomingRequests(Person currentUser) {
        return StreamUtil.filter(repository.findAll(), exchangeRequest ->
                exchangeRequest.getRecipientIntervalData().getPeopleOnDuty().getPerson().equals(currentUser));
    }

    @Override
    public List<ExchangeRequest> getExchangeRequestsHistory(int maxNumber) {
        return repository.findAll().stream()
                .filter(exchange -> exchange.getExchangeRequestState().equals(ExchangeRequestState.ACCEPTED))
                .sorted(Comparator.comparing(BaseEntity::getCreatedAt))
                .limit(maxNumber)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExchangeRequest> getExchangeRequestsHistoryAfter(LocalDate localDate) {
        return StreamUtil.filter(
                repository.findAll(),
                exchange -> exchange.getExchangeRequestState().equals(ExchangeRequestState.ACCEPTED),
                exchange -> exchange.getCreatedAt().isAfter(ChronoLocalDateTime.from(localDate))
        );
    }

    private List<ExchangeRequest> getSentMessagesFor(DutyIntervalData dutyIntervalData, Person person) {
        var authorMessages = getPersonToOtherRequests(person);
        return selectExchangesForInterval(authorMessages, dutyIntervalData);
    }

    private List<ExchangeRequest> getIncomeMessageFor(DutyIntervalData dutyIntervalData, Person person) {
        var incomingRequests = getPersonIncomingRequests(person);
        return selectExchangesForInterval(incomingRequests, dutyIntervalData);
    }

    private List<ExchangeRequest> selectExchangesForInterval(List<ExchangeRequest> requests,
                                                             DutyIntervalData dutyIntervalData) {
        return StreamUtil.filter(requests, exchangeRequest -> messageIsForInterval(exchangeRequest, dutyIntervalData));
    }

    private boolean messageIsForInterval(ExchangeRequest request, DutyIntervalData dutyIntervalData) {
        boolean isAuthor = currentPersonIsAuthorOf(request);
        var messageDutyData = isAuthor ? request.getAuthorIntervalData() : request.getRecipientIntervalData();
        var messageInterval = LocalDateTimeInterval.of(messageDutyData);
        var dutyInterval = LocalDateTimeInterval.of(dutyIntervalData);
        return messageInterval.intersects(dutyInterval);
    }


}