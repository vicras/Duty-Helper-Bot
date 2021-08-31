package com.sbo.ability.chain;

import com.sbo.ability.MessageSenders;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.sbo.domain.postgres.entity.enums.EntityStatus.ACTIVE;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;
import static org.telegram.abilitybots.api.util.AbilityUtils.getUser;

/**
 * @author viktar hraskou
 */
@Component
@RequiredArgsConstructor
public class SecurityProcessor implements UpdateProcessor {

    private final PersonService personService;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean process(Update update, AbilityBot abilityBot) {
        var messageContext = MessageContext.newContext(update, getUser(update), getChatId(update), abilityBot, "");

        if (!isRecognisePerson(messageContext.chatId())) {
            sendNotFoundMessage(messageContext);
            return false;
        }

        return true;
    }

    private boolean isRecognisePerson(long userId) {
        try {
            return ACTIVE.equals(personService.getPersonByTelegramId(userId).getEntityStatus());
        } catch (Exception e) {
            return false;
        }
    }

    private void sendNotFoundMessage(MessageContext context) {
        MessageSenders.tryToSendMethod(this::messageToSend)
                .accept(context);
    }

    private SendMessage messageToSend(MessageContext context) {
        var admins = personService.getActiveAdmins();
        var messBuilder = InlineMessageBuilder.builder(context.chatId())
                .line("*Error!!!*")
                .line("You do not have permission to use this bot.");

        if (!admins.isEmpty()) {
            messBuilder
                    .line("Contact with:")
                    .line();
            admins.forEach(person -> messBuilder.line("- %s", person.telegramLink()));
        } else {
            messBuilder
                    .line("There are no people you can contact at the moment!");
        }
        return messBuilder.build();
    }

}
