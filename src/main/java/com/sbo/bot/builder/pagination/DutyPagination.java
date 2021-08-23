package com.sbo.bot.builder.pagination;

import com.sbo.bot.builder.DutyMessagePrinter;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.builder.calendar.CalendarProvider;
import com.sbo.domain.postgres.entity.Duty;
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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.sbo.bot.builder.calendar.CalendarProvider.CHOSEN_DAY;
import static java.time.format.DateTimeFormatter.ISO_DATE;

/**
 * @author viktar hraskou
 */
@Component
@RequiredArgsConstructor
public class DutyPagination extends MessagePaginator<Duty> {
    //region Use this functions to set, and check callback command buttons
    //check
    public static final Predicate<String> isDutyPageChosen = (text) ->
            CalendarProvider.getIsChosenDay()
                    .and((t) -> t.matches(".*:.*:\\d+"))
                    .test(text);
    // TODO Warning, don't use static import for CalendarProvider.getIsChosenDay() it will brake your p
    private static final int PAGINATION_SIZE = 1;
    private static final String DUTY_PAGE = "DUTY_PAGE";
    private static final String SEPARATOR = ":";
    //region Use this functions to set, check and retrieved Chosen Duty
    public static final Function<Duty, String> callbackSetter = duty -> DUTY_PAGE + SEPARATOR + duty.getId();
    public static final Function<String, Long> parseDutyId = command -> Long.parseLong(command.split(SEPARATOR)[1]);
    public static final Predicate<String> isDutyChosen = text -> text.matches(DUTY_PAGE + SEPARATOR + "\\d+");
    //endregion
    //set
    private static final BiFunction<LocalDate, Integer, String> commandButtonCallback =
            (day, page) -> CHOSEN_DAY.apply(day.format(ISO_DATE)) + SEPARATOR + page;
    //retrieved
    private static final Function<String, LocalDate> dateParser = (command) -> LocalDate.parse(command.split(SEPARATOR)[1]);
    private static final Function<String, Integer> pageParser = (command) -> Integer.valueOf(command.split(SEPARATOR)[2]);
    //endregion
    private final CurrentPersonProvider personProvider;
    private final DutyService dutyService;
    private final DutyMessagePrinter dutyMessagePrinter;
    private final DateTimeFormatter isoTime = DateTimeFormatter.ISO_TIME;

    public SendMessage handleAll(CallbackQuery query) {
        return process(query, this::getAllDuties);
    }

    private SendMessage process(CallbackQuery query, BiFunction<LocalDate, Integer, Page<Duty>> dutyProvider) {
        String text = query.getData();
        throwIfCantHandle(text);

        LocalDate day = getDay(text);
        int page = getPage(text);

        // TODO huerga, ebanet 100%
        setCallBackDataProvider((pageNumber) -> commandButtonCallback.apply(day, pageNumber));

        Page<Duty> dutyPage = dutyProvider.apply(day, page);
        return paginate(dutyPage, personProvider.getCurrentPerson());
    }

    public SendMessage handleWithoutDutyOfCurrentPerson(CallbackQuery query) {
        return process(query, this::getWithoutMyDuties);
    }

    public SendMessage handleOnlyDutyOfCurrentPerson(CallbackQuery query) {
        return process(query, this::getOnlyMyDuties);
    }

    private Page<Duty> getAllDuties(LocalDate day, int page) {
        PageRequest personRequest = PageRequest.of(page, PAGINATION_SIZE, Sort.by("duty_from"));
        return dutyService.getPageWithAllDutiesOnADay(day, personRequest);
    }

    private Page<Duty> getOnlyMyDuties(LocalDate day, int page) {
        PageRequest personRequest = PageRequest.of(page, PAGINATION_SIZE, Sort.by("duty_from"));
        return dutyService.getPageWithOnlyMyDutiesOnADay(day, personRequest);
    }

    private Page<Duty> getWithoutMyDuties(LocalDate day, int page) {
        PageRequest personRequest = PageRequest.of(page, PAGINATION_SIZE, Sort.by("duty_from"));
        return dutyService.getPageWithoutMyDutiesOnADay(day, personRequest);
    }

    private int getPage(String text) {
        return isDutyPageChosen.test(text) ? pageParser.apply(text) : 0;
    }

    private LocalDate getDay(String text) {
        return CalendarProvider.getCommandButtonParser().apply(text);
    }

    private void throwIfCantHandle(String text) {
        if (!CalendarProvider.getIsChosenDay().test(text)) {
            throw new RuntimeException(String.format("expected command %s%s[day]%s[page], found %s", DUTY_PAGE, SEPARATOR, SEPARATOR, text));
        }
    }

    @Override
    protected void fillBuilderText(InlineMessageBuilder builder, Page<Duty> data) {
        builder.header("Duty info")
                .row();
        data.forEach(duty -> printDutyInfo(duty, builder));
    }

    private void printDutyInfo(Duty duty, InlineMessageBuilder builder) {
        dutyMessagePrinter.simpleDutyMessage(builder, duty);
        builder.button("Details", callbackSetter.apply(duty));
    }

}
