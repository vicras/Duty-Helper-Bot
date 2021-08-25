package com.sbo.bot.builder;

import com.sbo.domain.postgres.entity.Duty;
import com.sbo.domain.postgres.entity.PeopleOnDuty;
import com.sbo.provider.CurrentPersonProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * @author viktar hraskou
 */
@Component
@Transactional(readOnly = true)
@AllArgsConstructor
public class DutyMessagePrinter {

    private final DateTimeFormatter isoTime = DateTimeFormatter.ISO_TIME;
    private final CurrentPersonProvider personProvider;
    private final TransactionTemplate transactionTemplate;

    public InlineMessageBuilder simpleDutyMessage(InlineMessageBuilder builder, Duty duty) {
        Locale locale = personProvider.getCurrentPerson().getLanguage().getLocale();

        LocalDate dutyDate = duty.getDutyFrom().toLocalDate();
        builder.header(getDutyType(duty))
                .line("Day: %s (%s)", dutyDate.toString(), dutyDate.getDayOfWeek().getDisplayName(TextStyle.FULL, locale))
                .line("Time: %s - %s", duty.getDutyFrom().format(isoTime), duty.getDutyTo().format(isoTime))
                .line("Positions: %s", duty.getMaxPeopleOnDuty())
                .line("Info: %s", getDutyDescription(duty))
                .line("People:");

        fillPersons(builder, duty);

        return builder;
    }

    public String getDutyType(Duty duty) {
        // TODO ebanet lazy
        return transactionTemplate.execute((status) ->
                duty.getDutyTypes().stream()
                        .map(Objects::toString)
                        .collect(joining("/"))
        );
    }

    private void fillPersons(InlineMessageBuilder builder, Duty duty) {
//         TODO ebanet lazy
        var peoples = duty.getPeopleOnDuties();
        peoples.forEach(peopleOnDuty -> builder.line(personWithTime(peopleOnDuty)));
    }

    private String personWithTime(PeopleOnDuty peopleOnDuty) {
        return String.format("- %s time: %s - %s",
                peopleOnDuty.getPerson().telegramLink(),
                peopleOnDuty.getFromTime().format(isoTime),
                peopleOnDuty.getToTime().format(isoTime));
    }

    private String getDutyDescription(Duty duty) {
        return duty.getDescription().isEmpty()
                ? "empty"
                : duty.getDescription();
    }

    public InlineMessageBuilder addButtonsWithPersonOnDuty(InlineMessageBuilder builder, Duty duty) {
        var amount = 0;
        for (var peopleOnDuty : duty.getPeopleOnDuties()) {
            addButton(builder, peopleOnDuty, amount++);
        }
        return builder;
    }

    public void addButton(InlineMessageBuilder builder, PeopleOnDuty peopleOnDuty, int i) {
        if (i % 3 == 0) {
            builder.row();
        }
        builder.button(peopleOnDuty.getPerson().shortFirstLastName(), peopleOnDuty.getId().toString());
    }
}
