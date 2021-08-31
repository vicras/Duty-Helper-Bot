package com.sbo.ability.chain;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Comparator.comparingInt;

/**
 * @author viktar hraskou
 */
public interface UpdateProcessor {
    int getPriority();

    boolean process(Update update, AbilityBot abilityBot);

    static Predicate<Update> updateProcessorAppender(List<UpdateProcessor> processors, AbilityBot abilityBot) {
        return update -> {
            return processors.stream()
                    .sorted(comparingInt(UpdateProcessor::getPriority))
                    .map(updateProcessor -> updateProcessor.process(update, abilityBot))
                    .filter(res -> !res)
                    .findFirst()
                    .orElse(true);
        };
    }
}
