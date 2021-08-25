package com.sbo.bot.builder.calendar;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.domain.postgres.entity.Person;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.HOME;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.TextStyle.FULL;
import static java.time.format.TextStyle.SHORT;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.util.Objects.isNull;

/**
 * @author viktar hraskou
 */

@Component
@Scope("prototype")
public class CalendarProvider {

    private static final String ISO_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    private static final String SEPARATOR = ":";

    private static final String MONTH_PAGE = "MONTH_PAGE";
    private static final String YEAR_PAGE = "YEAR_PAGE";
    private static final String TO_MONTH = "TO_MONTH_VIEW";
    private static final String CHOSEN_MONTH_CONST = "CHOSEN_MONTH";
    private static final String CHOSEN_DAY_CONST = "CHOSEN_DAY";
    public static final Function<String, String> CHOSEN_DAY = (text) -> String.format(CHOSEN_DAY_CONST + ":%s", text);
    private static final Function<LocalDate, String> MONTH_PAGE_SWITCHER = (day) -> String.format(MONTH_PAGE + ":%s", day);
    private static final Function<LocalDate, String> YEAR_PAGE_SWITCHER = (day) -> String.format(YEAR_PAGE + ":%s", day);
    private static final Function<LocalDate, String> TO_YEAR_VIEW = (day) -> String.format(TO_MONTH + ":%s", day);
    private static final Function<LocalDate, String> CHOSEN_MONTH = (day) -> String.format(CHOSEN_MONTH_CONST + ":%s", day);
    @Getter
    private final static Function<String, LocalDate> commandButtonParser = (command) -> LocalDate.parse(command.split(":")[1]);
    @Getter
    private final static Predicate<String> isMonthSwitch = (command) -> command.matches(MONTH_PAGE + SEPARATOR + ISO_REGEX);
    @Getter
    private final static Predicate<String> isYearSwitch = (command) -> command.matches(YEAR_PAGE + SEPARATOR + ISO_REGEX);
    @Getter
    private final static Predicate<String> isToYear = (command) -> command.matches(TO_MONTH + SEPARATOR + ISO_REGEX);
    @Getter
    private final static Predicate<String> isDrawYearView = isYearSwitch.or(isToYear);
    @Getter
    private final static Predicate<String> isChosenMonth = (command) -> command.matches(CHOSEN_MONTH_CONST + SEPARATOR + ISO_REGEX);
    @Getter
    private final static Predicate<String> isDrawMonthView = isMonthSwitch.or(isChosenMonth);
    @Getter
    private final static Predicate<String> isChosenDay = (command) -> command.matches(CHOSEN_DAY_CONST + SEPARATOR + ".*");
    @Getter
    private final Predicate<String> isCommand = isDrawYearView.or(isDrawMonthView);
    @Setter
    private Function<LocalDate, String> mapToText = (date) -> "" + date.getDayOfMonth();
    @Setter
    private Function<LocalDate, String> mapToCallback = (date) -> date.format(ISO_LOCAL_DATE);
    @Setter
    private Supplier<String> monthText = () -> "Choose month";
    @Setter
    private Supplier<String> dayText = () -> "Choose date";

    public SendMessage handle(CallbackQuery callbackQuery, Person person) {
        String text = isNull(callbackQuery) ? "" : callbackQuery.getData();

        LocalDate day = isCommand.test(text)
                ? commandButtonParser.apply(text)
                : LocalDate.now();

        return isDrawYearView.test(text)
                ? generateYearView(day, person)
                : generateMonthView(day, person);

    }

    public SendMessage generateMonthView(LocalDate localDate, Person person) {
        InlineMessageBuilder builder = InlineMessageBuilder.builder(person)
                .line(dayText.get());

        Locale locale = person.getLanguage().getLocale();
        fillWithDayNamesButtons(builder, locale);
        fillWithNumberButtons(builder, localDate);
        fillWithMonthCommandButtons(builder, localDate, locale);

        return builder.build();
    }

    private void fillWithDayNamesButtons(InlineMessageBuilder builder, Locale locale) {
        builder.row();
        Arrays.stream(DayOfWeek.values()).forEach(dayOfWeek -> setTextForDayNamesButtons(builder, locale, dayOfWeek));
    }

    private void setTextForDayNamesButtons(InlineMessageBuilder builder, Locale locale, DayOfWeek dayOfWeek) {
        builder.button(dayOfWeek.getDisplayName(SHORT, locale), dayOfWeek.name());
    }

    private void fillWithNumberButtons(InlineMessageBuilder builder, LocalDate date) {
        LocalDate firstDayOfMonth = date.with(firstDayOfMonth());
        LocalDate firstDay = firstDayOfMonth.with(previousOrSame(MONDAY));

        LocalDate lastDayOfMonth = date.with(lastDayOfMonth());
        LocalDate lastDay = lastDayOfMonth.with(nextOrSame(SUNDAY));

        firstDay.datesUntil(lastDay.plusDays(1))
                .forEach(day -> setTextForNumberButtons(day, builder));
    }

    protected void setTextForNumberButtons(LocalDate date, InlineMessageBuilder builder) {
        if (date.getDayOfWeek().equals(MONDAY)) {
            builder.row();
        }
        builder.button(mapToText.apply(date), CHOSEN_DAY.apply(mapToCallback.apply(date)));
    }

    private void fillWithMonthCommandButtons(InlineMessageBuilder builder, LocalDate day, Locale locale) {
        builder.row()
                .button("⏪", MONTH_PAGE_SWITCHER.apply(day.minusMonths(1)))
                .button(day.getMonth().getDisplayName(SHORT, locale), TO_YEAR_VIEW.apply(day))
                .button("⏩", MONTH_PAGE_SWITCHER.apply(day.plusMonths(1)))
                .row()
                .button("HOME", HOME);
    }

    /**
     * View consist all month, also you can choose next or previous year.
     */
    public SendMessage generateYearView(LocalDate day, Person person) {
        Locale locale = person.getLanguage().getLocale();
        InlineMessageBuilder builder = InlineMessageBuilder.builder(person)
                .line(monthText.get());
        fillWithMonthButtons(builder, day, locale);
        fillWithYearCommandButtons(builder, day);
        return builder.build();
    }

    private void fillWithMonthButtons(InlineMessageBuilder builder, LocalDate day, Locale locale) {
        Arrays.stream(Month.values())
                .forEach(month -> setTextForMonthButtons(builder, day.getYear(), month, locale));
    }

    private void setTextForMonthButtons(InlineMessageBuilder builder, int year, Month month, Locale locale) {
        if (month.firstMonthOfQuarter().equals(month))
            builder.row();
        builder.button(month.getDisplayName(FULL, locale), CHOSEN_MONTH.apply(LocalDate.of(year, month.getValue(), 1)));
    }

    private void fillWithYearCommandButtons(InlineMessageBuilder builder, LocalDate day) {
        builder.row()
                .button("⏪", YEAR_PAGE_SWITCHER.apply(day.minusYears(1)))
                .button(String.valueOf(day.getYear()), "~")
                .button("⏩", YEAR_PAGE_SWITCHER.apply(day.plusYears(1)));
    }

}

