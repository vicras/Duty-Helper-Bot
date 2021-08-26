package com.sbo.bot.web;

import com.sbo.bot.DutyHelperBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author viktar hraskou
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BotApiController {

    private final DutyHelperBot dutyHelperBot;

    @Value("${telegram.bot.creator}")
    private long botCreator;

    @PostMapping("/touch")
    public ResponseEntity<Message> postMessage() {
        return dutyHelperBot.silent().send("Test message from web api controller😃", botCreator)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello world from duty bot web api");
    }
}
