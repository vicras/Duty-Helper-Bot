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
@Service
@RequiredArgsConstructor
public class ChangeRequestServiceImpl implements ChangeRequestService {

    private final DutyChangeDataRepository dutyChangeDataRepository;
    private final CurrentPersonProvider personProvider;

    //region update request logic
    @Override
    public void updateChangeRequest(PeopleOnDuty peopleOnDuty) {
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
        if (nonNull(dutyChangeHolder.getFromDutyId())) {
            return convert2LocalDateTimeRange(range, dutyChangeHolder.getFromDutyId().getRange().lowerEndpoint().toLocalDate());
        }
        if (nonNull(dutyChangeHolder.getToDutyId())) {
            return convert2LocalDateTimeRange(range, dutyChangeHolder.getToDutyId().getRange().lowerEndpoint().toLocalDate());
        }
        throw new ChangeRequestCreationException("Choose person on duty before");
    }

    @Override
    public void useFullRange() {
        Person currentPerson = personProvider.getCurrentPerson();
        DutyChangeDataHolder holder = getIncompleteData(currentPerson);

        if (isNull(holder.getFromRange()) && nonNull(holder.getFromDutyId())) {
            holder.setFromRange(holder.getFromDutyId().getRange());
        }

        if (isNull(holder.getToRange()) && nonNull(holder.getToDutyId())) {
            holder.setToRange(holder.getToDutyId().getRange());
        }


        saveIncompleteDataForPerson(currentPerson, holder);
    }

    @Override
    public void sendRequestIfComplete() {
// TODO send request if full
    }

    @Override
    public boolean isDataComplete() {
        Person currentPerson = personProvider.getCurrentPerson();
        return dutyChangeDataRepository.findDutyChangeData(currentPerson)
                .map(this::isDataComplete)
                .orElse(FALSE);
    }

    void updateRequestWith(PeopleOnDuty peopleOnDuty, DutyChangeDataHolder holder) {
        if (isDataComplete(holder)) {
            throw new ChangeRequestCreationException("Request data is full");
        }

        if (isNull(holder.getFromDutyId())) {
            checkRange(peopleOnDuty, holder.getFromRange());
            holder.setFromDutyId(peopleOnDuty);
        } else {
            checkRange(peopleOnDuty, holder.getToRange());
            holder.setToDutyId(peopleOnDuty);
        }
    }

    void updateRequestWith(Range<LocalDateTime> range, DutyChangeDataHolder holder) {
        if (isDataComplete(holder)) {
            throw new ChangeRequestCreationException("Request data is full");
        }

        if (isNull(holder.getFromRange())) {
            checkRange(holder.getFromDutyId(), range);
            holder.setFromRange(range);
        } else {
            checkRange(holder.getToDutyId(), range);
            holder.setToRange(range);
        }
    }

    void checkRange(PeopleOnDuty peopleOnDuty, Range<LocalDateTime> range) {
        if (isNull(peopleOnDuty) || isNull(range))
            return;

        if (peopleOnDuty.getRange().encloses(range)) {
            throw new ChangeRequestCreationException(
                    String.format("Wrong range! %s don't encloses %s", peopleOnDuty.getRange(), range)
            );
        }
    }

    void createNewHolderWith(PeopleOnDuty peopleOnDuty) {
        DutyChangeDataHolder holder = DutyChangeDataHolder.builder()
                .fromDutyId(peopleOnDuty)
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
    }

    @Override
    public boolean isDataComplete(DutyChangeDataHolder holder) {
        return nonNull(holder.getFromDutyId())
                && nonNull(holder.getToDutyId())
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
        return holder.getFromDutyId().getPerson().getId().equals(person.getId()) ||
                holder.getToDutyId().getPerson().getId().equals(person.getId());
    }
}
