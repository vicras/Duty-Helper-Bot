package com.sbo.bot.builder.pagination;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.builder.calendar.CalendarProvider;
import com.sbo.entity.Duty;
import com.sbo.entity.PeopleOnDuty;
import com.sbo.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.DutyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.sbo.bot.builder.calendar.CalendarProvider.CHOSEN_DAY;
import static com.sbo.bot.builder.calendar.CalendarProvider.getIsChosenDay;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.util.stream.Collectors.joining;

/**
 * @author viktar hraskou
 */
@Component
@RequiredArgsConstructor
public class DutyPagination extends MessagePaginator<Duty> {

    private static final int PAGINATION_SIZE = 1;
    private static final String DUTY_PAGE = "DUTY_PAGE";
    private static final String SEPARATOR = ":";

    public static final Predicate<String> isDutyPageChosen = (text) -> getIsChosenDay()
            .and((t) -> t.matches(".*:.*:\\d+"))
            .test(text);
    private static final Function<Duty, String> callbackSetter = duty -> DUTY_PAGE + SEPARATOR + duty.getId();

    private static final BiFunction<LocalDate, Integer, String> commandButtonCallback =
            (day, page) -> CHOSEN_DAY.apply(day.format(ISO_DATE)) + SEPARATOR + page;

    private static final Function<String, LocalDate> dateParser = (command) -> LocalDate.parse(command.split(SEPARATOR)[1]);
    private static final Function<String, Integer> pageParser = (command) -> Integer.valueOf(command.split(SEPARATOR)[2]);


    private final CurrentPersonProvider personProvider;
    private final DutyService dutyService;
    private final DateTimeFormatter isoTime = DateTimeFormatter.ISO_TIME;

    public SendMessage handle(CallbackQuery query) {
        String text = query.getData();

        if (!getIsChosenDay().test(text)) {
            throw new RuntimeException(String.format("expected command %s%s[day]%s[page], found %s", DUTY_PAGE, SEPARATOR, SEPARATOR, text));
        }

        LocalDate day = CalendarProvider.getCommandButtonParser().apply(text);
        int page = isDutyPageChosen.test(text) ? pageParser.apply(text) : 0;
        // TODO huerga
        setCallBackDataProvider((pageNumber) -> commandButtonCallback.apply(day, pageNumber));

        PageRequest personRequest = PageRequest.of(page, PAGINATION_SIZE, Sort.by("duty_from"));
        Page<Duty> dutyPage = dutyService.getDutiesPageOnADay(day, personRequest);
        return paginate(dutyPage, personProvider.getCurrentPerson());
    }

    @Override
    protected void fillBuilderText(InlineMessageBuilder builder, Page<Duty> data) {
        builder.header("Duty info")
                .row();
        data.forEach(duty -> printDutyInfo(duty, builder));
    }

    private void printDutyInfo(Duty duty, InlineMessageBuilder builder) {
        Person person = personProvider.getCurrentPerson();
        Locale locale = person.getLanguage().getLocale();

        LocalDate dutyDate = duty.getDutyFrom().toLocalDate();
        builder.header(getDutyType(duty))
                .line("Day: %s (%s)", dutyDate.toString(), dutyDate.getDayOfWeek().getDisplayName(TextStyle.FULL, locale))
                .line("Time: %s - %s", duty.getDutyFrom().format(isoTime), duty.getDutyTo().format(isoTime))
                .line("Positions: %s", duty.getMaxPeopleOnDuty())
                .line("Info: %s", getDutyDescription(duty))
                .line("People:");

        fillPersons(builder, duty);

        builder
                .button("Details", callbackSetter.apply(duty));
    }

    private void fillPersons(InlineMessageBuilder builder, Duty duty) {
        // TODO ebanet lazy
        var peoples = duty.getPeopleOnDuties();
        peoples.forEach(peopleOnDuty -> builder.line(formatPerson(peopleOnDuty)));
    }

    private String formatPerson(PeopleOnDuty peopleOnDuty) {
        return String.format("- %s time: %s - %s",
                peopleOnDuty.getPerson().telegramLink(),
                peopleOnDuty.getOnDutyFrom().format(isoTime),
                peopleOnDuty.getOnDutyTo().format(isoTime));
    }

    private String getDutyType(Duty duty) {
        // TODO ebanet lazy
        return duty.getDutyTypes().stream()
                .map(Objects::toString)
                .collect(joining("/"));
    }

    private String getDutyDescription(Duty duty) {
        return duty.getDescription().isEmpty()
                ? "empty"
                : duty.getDescription();
    }
}
