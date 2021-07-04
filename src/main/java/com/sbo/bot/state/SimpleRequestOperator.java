package com.sbo.bot.state;

import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Dmitars
 */
@Component
@Scope("prototype")
public class SimpleRequestOperator extends RequestOperator {
    public SimpleRequestOperator(ApplicationEventPublisher publisher) {
        super(publisher);
    }

    public SendMessageOperatorBuilder builder(){
        return new SendMessageOperatorBuilder(this);
    }

    public static class SendMessageOperatorBuilder {
        private final SimpleRequestOperator simpleRequestOperator;
        private String text = "";

        SendMessageOperatorBuilder(SimpleRequestOperator simpleRequestOperator) {
            this.simpleRequestOperator = simpleRequestOperator;
        }

        public SimpleRequestOperator.SendMessageOperatorBuilder text(@NonNull String text) {
            if (text == null) {
                throw new NullPointerException("text is marked non-null but is null");
            } else {
                this.text = text;
                return this;
            }
        }

        public SimpleRequestOperator.SendMessageOperatorBuilder line(@NonNull String text) {
            if (text == null) {
                throw new NullPointerException("text is marked non-null but is null");
            } else {
                this.text = this.text + "\n" + text;
                return this;
            }
        }

        public SimpleRequestOperator build() {
            SendMessage sendMessage = SendMessage.builder()
                    .text(text)
                    .build();
            simpleRequestOperator.addMessage(sendMessage);
            return simpleRequestOperator;
        }
    }
}
