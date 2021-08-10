package com.sbo.bot.state.impl.management;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.entity.enums.PersonRole;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class RoleChangingState extends State {

    public RoleChangingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService) {
        super(personProvider, publisher, personService);
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(AllPersonState.class, not(this::isListOfNumbers)),
                SwitchHandler.of(AllPersonState.class, this::isListOfNumbers)
                        .setAction(this::setRoles)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var personToBlock = extractPerson(update);
        var mess = createInfoMessage(personToBlock);
        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }


    private SendMessage createInfoMessage(Person personToBlock) {
        InlineMessageBuilder builder = InlineMessageBuilder.builder(personProvider.getCurrentPerson())

                .line("[%s](tg://user?id=%d)",
                        personToBlock.getFirstName(), personToBlock.getTelegramId());
// TODO lazy init
//                .line("Current roles list:");
//        personToBlock.getRoles().forEach(role -> builder.line("- %s", role));
        builder.line()
                .line("Available roles: ");
        for (int i = 0; i <PersonRole.values().length; i++) {
            builder.line("%d. %s", i, PersonRole.values()[i]);
        }

        return builder
                .line("To set roles to user write it number through a space for reply to this message ")
                .line("Write everything else to back")
                .setForceReply()
                .build();

    }

    private Person extractPerson(Update update) {
        long id = Long.parseLong(update.getCallbackQuery().getData().split(" ")[1]);
        return personService.getPersonByTelegramId(id);
    }

    private boolean isListOfNumbers(Update update) {
        String rolesNumberRegex = "(\\d(\\W)*)+";
        return update.hasMessage()
                && update.getMessage().getText().matches(rolesNumberRegex);
    }

    private void setRoles(Update update) {
        var rolesToSet = getRolesToSet(update);
        Long idToSetRoles = getIdToSetRoles(update);
        personService.updatePersonRoles(idToSetRoles, rolesToSet);
        publish(okMessage());
    }

    private List<PersonRole> getRolesToSet(Update update) {
        String text = update.getMessage().getText();
        return Arrays.stream(text.split(" "))
                .map(Integer::valueOf)
                .map(order -> PersonRole.values()[order])
                .collect(toList());
    }

    private Long getIdToSetRoles(Update update) {
        return Long.valueOf(update.getMessage().getReplyToMessage().getEntities().get(0).getUser().getId());
    }

    private SendMessage okMessage(){
        return InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .line("Changed successfully✔")
                .build();
    }

}
