package com.sbo.service.impl;

import com.google.common.collect.Range;
import com.sbo.domain.postgres.entity.PeopleOnDuty;
import com.sbo.domain.postgres.entity.Person;
import com.sbo.domain.redis.entity.DutyChangeDataHolder;
import com.sbo.domain.redis.repository.DutyChangeDataRepository;
import com.sbo.exception.ChangeRequestCreationException;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.ChangeRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.sbo.common.utils.DateTimeUtil.convert2LocalDateTimeRange;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author viktar hraskou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeRequestServiceImpl implements ChangeRequestService {

    private final DutyChangeDataRepository dutyChangeDataRepository;
    private final CurrentPersonProvider personProvider;

    //region update request logic
    @Override
    public void updateChangeRequest(PeopleOnDuty peopleOnDuty) {
        // crutch
        Hibernate.initialize(peopleOnDuty.getDuty());
        Hibernate.initialize(peopleOnDuty.getDuty().getDutyTypes());
        Hibernate.initialize(peopleOnDuty.getPerson());

        Person currentPerson = personProvider.getCurrentPerson();
        dutyChangeDataRepository.findDutyChangeData(currentPerson).ifPresentOrElse(
                dutyChangeHolder -> updateRequestWith(peopleOnDuty, dutyChangeHolder),
                () -> createNewHolderWith(peopleOnDuty)
        );
    }

    @Override
    public void updateChangeRequest(Range<LocalDateTime> range) {
        Person currentPerson = personProvider.getCurrentPerson();
        dutyChangeDataRepository.findDutyChangeData(currentPerson).ifPresentOrElse(
                dutyChangeHolder -> updateRequestWith(range, dutyChangeHolder),
                () -> createNewHolderWith(range)
        );
    }

    @Override
    public void updateChangeRequestWithSameDay(Range<LocalTime> range) {
        Person currentPerson = personProvider.getCurrentPerson();
        dutyChangeDataRepository.findDutyChangeData(currentPerson).ifPresentOrElse(
                dutyChangeHolder -> {
                    Range<LocalDateTime> localDateTimeRange = getLocalDateTimeRange(range, dutyChangeHolder);
                    updateRequestWith(localDateTimeRange, dutyChangeHolder);
                },
                () -> {
                    throw new ChangeRequestCreationException("Choose person on duty before");
                }
        );
    }

    @NotNull
    private Range<LocalDateTime> getLocalDateTimeRange(Range<LocalTime> range, DutyChangeDataHolder dutyChangeHolder) {
        if (nonNull(dutyChangeHolder.getFromPeopleOnDuty())) {
            return convert2LocalDateTimeRange(range, dutyChangeHolder.getFromPeopleOnDuty().getRange().lowerEndpoint().toLocalDate());
        }
        if (nonNull(dutyChangeHolder.getToPeopleOnDuty())) {
            return convert2LocalDateTimeRange(range, dutyChangeHolder.getToPeopleOnDuty().getRange().lowerEndpoint().toLocalDate());
        }
        throw new ChangeRequestCreationException("Choose person on duty before");
    }

    @Override
    public void useFullRange() {
        Person currentPerson = personProvider.getCurrentPerson();
        DutyChangeDataHolder holder = getIncompleteData(currentPerson);

        if (isNull(holder.getFromRange()) && nonNull(holder.getFromPeopleOnDuty())) {
            holder.setFromRange(holder.getFromPeopleOnDuty().getRange());
        }

        if (isNull(holder.getToRange()) && nonNull(holder.getToPeopleOnDuty())) {
            holder.setToRange(holder.getToPeopleOnDuty().getRange());
        }


        saveIncompleteDataForPerson(currentPerson, holder);
    }

    @Override
    public void sendRequestIfComplete() {
// TODO send request if full
        deleteDataForPerson(personProvider.getCurrentPerson());
    }

    @Override
    public boolean isDataComplete() {
        Person currentPerson = personProvider.getCurrentPerson();
        return dutyChangeDataRepository.findDutyChangeData(currentPerson)
                .map(this::isDataComplete)
                .orElse(FALSE);
    }

    private void updateRequestWith(PeopleOnDuty peopleOnDuty, DutyChangeDataHolder holder) {
        if (isDataComplete(holder)) {
            throw new ChangeRequestCreationException("Request data is full");
        }

        if (isNull(holder.getFromPeopleOnDuty())) {
            checkRange(peopleOnDuty, holder.getFromRange());
            holder.setFromPeopleOnDuty(peopleOnDuty);
        } else {
            checkRange(peopleOnDuty, holder.getToRange());
            holder.setToPeopleOnDuty(peopleOnDuty);
        }
        saveIncompleteDataForPerson(personProvider.getCurrentPerson(), holder);
    }

    private void updateRequestWith(Range<LocalDateTime> range, DutyChangeDataHolder holder) {
        if (isDataComplete(holder)) {
            throw new ChangeRequestCreationException("Request data is full");
        }

        if (isNull(holder.getFromRange())) {
            checkRange(holder.getFromPeopleOnDuty(), range);
            holder.setFromRange(range);
        } else {
            checkRange(holder.getToPeopleOnDuty(), range);
            holder.setToRange(range);
        }
        saveIncompleteDataForPerson(personProvider.getCurrentPerson(), holder);
    }

    void checkRange(PeopleOnDuty peopleOnDuty, Range<LocalDateTime> range) {
        if (isNull(peopleOnDuty) || isNull(range))
            return;

        if (!peopleOnDuty.getRange().encloses(range)) {
            throw new ChangeRequestCreationException(
                    String.format("Wrong range! %s don't encloses %s", peopleOnDuty.getRange(), range)
            );
        }
    }

    void createNewHolderWith(PeopleOnDuty peopleOnDuty) {
        DutyChangeDataHolder holder = DutyChangeDataHolder.builder()
                .fromPeopleOnDuty(peopleOnDuty)
                .build();
        saveIncompleteDataForPerson(personProvider.getCurrentPerson(), holder);
    }

    void createNewHolderWith(Range<LocalDateTime> range) {
        DutyChangeDataHolder holder = DutyChangeDataHolder.builder()
                .fromRange(range)
                .build();
        saveIncompleteDataForPerson(personProvider.getCurrentPerson(), holder);
    }
    //endregion

    @Override
    public void saveIncompleteDataForPerson(Person person, DutyChangeDataHolder holder) {
        dutyChangeDataRepository.saveDutyChangeData(person, holder);
    }

    @Override
    public DutyChangeDataHolder getIncompleteData(Person person) {
        return dutyChangeDataRepository.findDutyChangeData(person)
                .orElseThrow(() -> new EntityNotFoundException(DutyChangeDataHolder.class, person.getId()));
    }

    @Override
    public void deleteDataForPerson(Person person) {
        dutyChangeDataRepository.deleteDutyChangeData(person);
        log.info("holder for person {} delete", person);
    }

    @Override
    public boolean isDataComplete(DutyChangeDataHolder holder) {
        return nonNull(holder.getFromPeopleOnDuty())
                && nonNull(holder.getToPeopleOnDuty())
                && nonNull(holder.getFromRange())
                && nonNull(holder.getToRange());
    }

    @Override
    public boolean isPersonAlreadyFilledInHolder(Person person) {
        return dutyChangeDataRepository.findDutyChangeData(person)
                .filter(holder -> isPersonFilled(person, holder))
                .map(e -> TRUE)
                .orElse(FALSE);
    }

    private boolean isPersonFilled(Person person, DutyChangeDataHolder holder) {
        PeopleOnDuty fromPeopleOnDuty = holder.getFromPeopleOnDuty();
        PeopleOnDuty toPeopleOnDuty = holder.getToPeopleOnDuty();
        return nonNull(fromPeopleOnDuty) && fromPeopleOnDuty.getPerson().getId().equals(person.getId()) ||
                nonNull(toPeopleOnDuty) && toPeopleOnDuty.getPerson().getId().equals(person.getId());
    }
}
