package com.sbo.ability;

import com.sbo.ability.annotations.PostProcessor;
import com.sbo.ability.commands.AbilityCommand;
import com.sbo.ability.commands.AbilityStateCommand;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.sbo.ability.MessageSenders.tryToEditUpdateWithMessage;
import static com.sbo.ability.MessageSenders.tryToSendMethod;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AbilitySupport {

    @Setter(onMethod_ = {@Autowired})
    private AbilityBot abilityBot;
    private final PostProcessor postProcessor;


    public Ability switchAbility(AbilityCommand abilityCommand) {
        return getAbility(abilityCommand)
                .action((ctx) -> {})
                .post(postProcessor.process(abilityCommand.getNextState()))
                .build();
    }

    public Ability defaultUserAbility(@NotNull AbilityCommand abilityCommand, Consumer<MessageContext> action) {
        return getAbility(abilityCommand)
                .action(action)
                .post(postProcessor.process(abilityCommand.getNextState()))
                .build();
    }

    public Ability defaultUserAbilityEdit(@NotNull AbilityCommand abilityCommand, Function<MessageContext, SendMessage> action) {
        return getAbility(abilityCommand)
                .action(tryToEditUpdateWithMessage(action))
                .post(postProcessor.process(abilityCommand.getNextState()))
                .build();
    }

    public Ability defaultUserAbilitySend(@NotNull AbilityCommand abilityCommand, Function<MessageContext, SendMessage> action) {
        return getAbility(abilityCommand)
                .action(tryToSendMethod(action))
                .post(postProcessor.process(abilityCommand.getNextState()))
                .build();
    }

    public Ability defaultUserAbilityWithTextEdit(AbilityCommand abilityCommand, String text) {
        return defaultUserAbilityWithTextAndKeyboardEdit(abilityCommand, text, null);
    }

    public Ability defaultUserAbilityWithTextAndKeyboardEdit(@NotNull AbilityCommand abilityCommand, String text, ReplyKeyboard replyKeyboard) {
        Function<MessageContext, SendMessage> action =
                context -> SendMessage.builder()
                        .chatId(context.chatId() + "")
                        .parseMode(MARKDOWN)
                        .text(text)
                        .replyMarkup(replyKeyboard)
                        .build();

        return getAbility(abilityCommand)
                .action(tryToEditUpdateWithMessage(action))
                .post(ctx -> postProcessor.process(abilityCommand.getNextState()))
                .build();
    }

    private Ability.AbilityBuilder getAbility(@NotNull AbilityCommand abilityCommand) {
        if (abilityCommand instanceof AbilityStateCommand) {
            return ((AbilityStateCommand) abilityCommand).defaultUserAbilityWithoutActions(abilityBot.db());
        }
        return abilityCommand.defaultUserAbilityWithoutActions();
    }
}
