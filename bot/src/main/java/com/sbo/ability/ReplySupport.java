package com.sbo.ability;

import com.sbo.ability.annotations.PostProcessor;
import com.sbo.ability.commands.AbilityCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static org.apache.commons.lang3.ArrayUtils.add;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReplySupport {

    private final PostProcessor postProcessor;

    public Reply switchReply(AbilityCommand command) {
        BiConsumer<BaseAbilityBot, Update> baseAbilityBotUpdateBiConsumer = (ctx, upd) -> {
            MessageContext messageContext = MessageContext.newContext(upd, AbilityUtils.getUser(upd), AbilityUtils.getChatId(upd), ctx);
            postProcessor.process(command.getNextState()).accept(messageContext);
        };

        return Reply.of(baseAbilityBotUpdateBiConsumer, add(command.getFlags(), isCommand(command)));
    }



    private Predicate<Update> isCommand(AbilityCommand command) {
        return update -> {
            if (update.hasMessage()) {
                return update.getMessage().getText().contains("/" + command.name().toLowerCase());
            }else {
                return update.getCallbackQuery().getData().contains("/"+ command.name().toLowerCase());
            }
        };
    }
}
