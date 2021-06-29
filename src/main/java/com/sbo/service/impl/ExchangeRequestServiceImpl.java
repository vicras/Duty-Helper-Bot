package com.sbo.service.impl;

import com.sbo.common.time.LocalDateTimeInterval;
import com.sbo.common.utils.StreamUtil;
import com.sbo.entity.DutyIntervalData;
import com.sbo.entity.ExchangeRequest;
import com.sbo.entity.Person;
import com.sbo.entity.enums.ExchangeRequestState;
import com.sbo.repository.ExchangeRequestRepository;
import com.sbo.service.ExchangeRequestService;
import com.sbo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeRequestServiceImpl implements ExchangeRequestService {
    @Autowired
    private ExchangeRequestRepository repository;

    @Autowired
    private PersonService personService;

    @Override
    public boolean equalsExchangeExists(ExchangeRequest exchangeRequest) {
        var currentPerson = personService.getCurrentPerson();
        var currentPersonRequests = getPersonToOtherRequests(currentPerson);
        return currentPersonRequests.stream()
                .anyMatch(existedRequest -> existedRequest.equals(exchangeRequest));
    }

    @Override
    public boolean currentPersonIsAuthorOf(ExchangeRequest exchangeRequest) {
        var currentPerson = personService.getCurrentPerson();
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
        return StreamUtil.filter(requests,exchangeRequest -> messageIsForInterval(exchangeRequest, dutyIntervalData));
    }

    private boolean messageIsForInterval(ExchangeRequest request, DutyIntervalData dutyIntervalData) {
        boolean isAuthor = currentPersonIsAuthorOf(request);
        var messageDutyData = isAuthor ? request.getAuthorIntervalData() : request.getRecipientIntervalData();
        var messageInterval = LocalDateTimeInterval.of(messageDutyData);
        var dutyInterval = LocalDateTimeInterval.of(dutyIntervalData);
        return messageInterval.intersects(dutyInterval);
    }


}
