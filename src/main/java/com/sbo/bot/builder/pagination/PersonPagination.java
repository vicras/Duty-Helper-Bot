package com.sbo.bot.builder.pagination;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * @author viktar hraskou
 */
@Component
@RequiredArgsConstructor
public class PersonPagination extends MessagePaginator<Person> {

    private static final int PAGINATION_SIZE = 4;
    private final PersonService personService;
    private final CurrentPersonProvider personProvider;

    public SendMessage paginateActivePersons(int page){
        PageRequest personRequest = PageRequest.of(page, PAGINATION_SIZE, Sort.by("lastName"));
        Page<Person> personPage = personService.getActivePersons(personRequest);
        return paginate(personPage, personProvider.getCurrentPerson());
    }

    @Override
    protected void fillBuilderText(InlineMessageBuilder builder, Page<Person> data) {
        builder.header("Persons info")
                .row();
        data.forEach(person -> printPersonInfo(person, builder));
    }

    private void printPersonInfo(Person person, InlineMessageBuilder builder) {
        builder.line(personSeparator())
                .header("%s %s %s", person.getLastName(), person.getFirstName(), person.getPatronymic())
                .line("Tel: %s", person.getTel())
                .line("Address: %s", person.getHomeAddress())
                .line("Email: %s", person.getMail())
                .line("Birth: %s", person.getBirthDate());
        person.getRoles().forEach(role -> builder.line("- %s", role));
        builder.line("Link: [%s](tg://user?id=%d)%s", person.getFirstName(), person.getTelegramId())
                .line()
                .button(person.getFirstName().charAt(0)+ ". " + person.getLastName(), person.getTelegramId().toString());
    }

    private String personSeparator() {
        return Stream.generate(() -> "*")
                .limit(30)
                .collect(joining());
    }
}
