package com.sbo.ability.states;

import com.sbo.ability.AbilitySupport;
import com.sbo.ability.ReplySupport;
import com.sbo.ability.State;
import com.sbo.bot.builder.InlineMessageBuilder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.sbo.ability.commands.BaseCommands.START;
import static com.sbo.ability.commands.StateCommands.SETTINGS;

/**
 * @author viktar hraskou
 */
@Component
@RequiredArgsConstructor
public class StartState implements State {

    private final AbilitySupport abilitySupport;
    private final ReplySupport replySupport;

    public Reply toSettings(){
        return replySupport.switchReply(SETTINGS);
    }

    public Ability startCommand() {
        return abilitySupport.defaultUserAbilitySend(START, this::getStartMessage);
    }

    @NotNull
    private SendMessage getStartMessage(@NotNull MessageContext context) {
        return InlineMessageBuilder.builder(context.chatId())
                .header("Hi")
                .line("You use simple duty helper telegram bot")
                .row()
                .button("Settings", SETTINGS.getCommand())
                .build();
    }
}
