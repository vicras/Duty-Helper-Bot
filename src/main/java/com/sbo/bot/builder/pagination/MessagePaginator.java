package com.sbo.bot.builder.pagination;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.entity.Person;
import org.springframework.data.domain.Page;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.PAGE;

/**
 * @author viktar hraskou
 */
public abstract class MessagePaginator<T> {

    protected SendMessage paginate(Page<T> data, Person person) {
        InlineMessageBuilder builder = InlineMessageBuilder.builder(person);

        fillBuilderText(builder, data);

        builder.line("In all: %s elements. page %s from %s", data.getTotalElements(), data.getNumber() + 1, data.getTotalPages());

        builder.row();

        if (data.hasPrevious()) {
            builder.button("|<", PAGE + " " + "0");
            builder.button("<--", PAGE + " " + data.previousPageable().getPageNumber());
        }
        builder.button("Back", BACK);

        if (!data.isLast()) {
            builder.button("-->", PAGE + " " + data.nextPageable().getPageNumber());
            builder.button(">|", PAGE + " " + (data.getTotalPages() - 1));
        }
        return builder.build();
    }

    protected abstract void fillBuilderText(InlineMessageBuilder builder, Page<T> data);

}
