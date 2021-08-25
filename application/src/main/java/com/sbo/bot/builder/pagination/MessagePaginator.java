package com.sbo.bot.builder.pagination;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.domain.postgres.entity.Person;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;

/**
 * @author viktar hraskou
 */
public abstract class MessagePaginator<T> {
    private static final String PAGE = "PAGE";
    private static final String SEPARATOR = ":";

    @Setter
    @Getter
    private Function<Integer, String> callBackDataProvider = (text) -> PAGE + SEPARATOR + text;

    @Setter
    @Getter
    private Predicate<String> isPageChangeRequest = (text) -> text.matches(PAGE + SEPARATOR + ".*");

    @Setter
    @Getter
    private Function<String, Integer> extractPage = (text) -> Integer.valueOf(text.split(SEPARATOR)[1]);

    protected SendMessage paginate(Page<T> data, Person person) {
        InlineMessageBuilder builder = InlineMessageBuilder.builder(person);

        fillBuilderText(builder, data);

        builder.line("In all: %s elements. page %s from %s", data.getTotalElements(), data.getNumber() + 1, data.getTotalPages());

        fillCommandButtons(data, builder);
        return builder.build();
    }

    private void fillCommandButtons(Page<T> data, InlineMessageBuilder builder) {
        builder.row();

        if (data.hasPrevious()) {
            builder.button("|<", callBackDataProvider.apply(0));
            builder.button("<--", callBackDataProvider.apply(data.previousPageable().getPageNumber()));
        }
        builder.button("Back", BACK);

        if (!data.isLast()) {
            builder.button("-->", callBackDataProvider.apply(data.nextPageable().getPageNumber()));
            builder.button(">|", callBackDataProvider.apply(data.getTotalPages() - 1));
        }
    }

    protected abstract void fillBuilderText(InlineMessageBuilder builder, Page<T> data);

}
