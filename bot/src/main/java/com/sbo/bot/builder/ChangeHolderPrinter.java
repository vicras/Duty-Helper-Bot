package com.sbo.bot.builder;

import com.google.common.collect.Range;
import com.sbo.domain.postgres.entity.PeopleOnDuty;
import com.sbo.domain.postgres.entity.enums.DutyTypes;
import com.sbo.domain.redis.entity.DutyChangeDataHolder;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.ChangeRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;

import static java.util.stream.Collectors.joining;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeHolderPrinter {
    private final ChangeRequestService changeRequestService;
    private final CurrentPersonProvider currentPersonProvider;

    public InlineMessageBuilder fillMessageWithDataHolderData(InlineMessageBuilder builder) {
        DutyChangeDataHolder data = changeRequestService.getIncompleteData(currentPersonProvider.getCurrentPerson());
        return builder.line("Proposes:")
                .line(data.getFromPeopleOnDuty().getPerson().telegramLink())
                .line(getDutyInfo(data.getToPeopleOnDuty()))
                .line(getRangeInfo(data.getToRange()))
                .line("And")
                .line(data.getToPeopleOnDuty().getPerson().telegramLink())
                .line(getDutyInfo(data.getFromPeopleOnDuty()))
                .line(getRangeInfo(data.getFromRange()));
    }

    private String getRangeInfo(Range<LocalDateTime> toRange) {
        LocalDateTime lower = toRange.lowerEndpoint();
        LocalDate lowerDay = lower.toLocalDate();
        LocalDateTime upper = toRange.upperEndpoint();
        String dayOfWeek = lowerDay.getDayOfWeek().getDisplayName(TextStyle.SHORT, currentPersonProvider.getCurrentPerson().getLanguage().getLocale());
        return String.format("%s (%s) %s-%s", lowerDay, dayOfWeek, lower.toLocalTime(), upper.toLocalTime());
    }

    private String getDutyInfo(PeopleOnDuty toPeopleOnDuty) {
        return toPeopleOnDuty.getDuty().getDutyTypes()
                .stream()
                .map(DutyTypes::name)
                .collect(joining("/"));
    }

}
