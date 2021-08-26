package com.sbo.ability;

import com.sbo.ability.annotations.PostProcessor;
import com.sbo.ability.commands.AbilityCommand;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.sbo.ability.MessageSenders.tryToEditUpdateWithMessage;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;

/**
 * @author viktar hraskou
 */
@Component
@RequiredArgsConstructor
public class AbilitySupport {

    private final PostProcessor postProcessor;
    private final AbilityBot abilityBot;

    public Ability defaultUserAbility(@NotNull AbilityCommand abilityCommand, Consumer<MessageContext> action) {
        return Ability
                .builder()
                .basedOn(abilityCommand.defaultUserAbilityWithoutActions())
                .action(action)
                .post(ctx -> postProcessor.process(abilityCommand.getNextState()))
                .build();
    }

    public Ability defaultUserAbility(@NotNull AbilityCommand abilityCommand, Function<MessageContext, SendMessage> action) {
        return Ability
                .builder()
                .basedOn(abilityCommand.defaultUserAbilityWithoutActions())
                .action(tryToEditUpdateWithMessage(action))
                .post(ctx -> postProcessor.process(abilityCommand.getNextState()))
                .build();
    }

    public Ability defaultUserAbilityWithText(AbilityCommand abilityCommand, String text) {
        return defaultUserAbilityWithTextAndKeyboard(abilityCommand, text, null);
    }

    public Ability defaultUserAbilityWithTextAndKeyboard(@NotNull AbilityCommand abilityCommand, String text, ReplyKeyboard replyKeyboard) {
        Function<MessageContext, SendMessage> action =
                context -> SendMessage.builder()
                        .chatId(context.chatId() + "")
                        .parseMode(MARKDOWN)
                        .text(text)
                        .replyMarkup(replyKeyboard)
                        .build();

        return Ability
                .builder()
                .basedOn(abilityCommand.defaultUserAbilityWithoutActions())
                .action(tryToEditUpdateWithMessage(action))
                .post(ctx -> postProcessor.process(abilityCommand.getNextState()))
                .build();
    }
}
