package com.sbo.ability.annotations;

import com.sbo.ability.MessageSenders;
import com.sbo.ability.State;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

import static com.sbo.ability.State.IDENTITY_STATE;
import static com.sbo.ability.State.stateSetter;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class PostProcessor {

    public Consumer<MessageContext> process(@NotNull Consumer<MessageContext> action, Class<? extends State> nextState) {
        return action.andThen(process(nextState));
    }

    public Consumer<MessageContext> processAfter(Consumer<MessageContext> action, Class<? extends State> nextState) {
        return process(nextState).andThen(action);
    }

    public Consumer<MessageContext> process(@NotNull Class<? extends State> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        var startMethodsStream = Arrays.stream(methods)
                .filter(this::isStateFirstMethod);

        Consumer<MessageContext> methodInvokes = context -> startMethodsStream.forEach(invokeExceptionally(context));
        return updateStateIfNecessary(clazz).andThen(methodInvokes);
    }

    @NotNull
    @Contract(pure = true)
    private Consumer<MessageContext> updateStateIfNecessary(Class<? extends State> clazz) {
        return IDENTITY_STATE.getClass().equals(clazz)
                ? messageContext -> {}
                : messageContext -> stateSetter(messageContext, clazz);
    }

    @NotNull
    private Consumer<Method> invokeExceptionally(MessageContext context) {
        return method -> {
            try {
                if (isSendMessageReturnType(method)) {
                    var message = (SendMessage) method.invoke(context);
                    sendWithEditRule(message, method, context);
                } else {
                    method.invoke(context);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
    }

    private void sendWithEditRule(SendMessage message, Method method, MessageContext messageContext) {
        var t = isTryToEditUpdateWithSendMessage(method)
                ? MessageSenders.tryToEditUpdateWithMessage(context -> message)
                : MessageSenders.tryToSendMethod(context -> message);
        t.accept(messageContext);
    }

    //annotated with State first annotation
    //has one parameter of MessageContext type
    //no return type or SendMessage type
    private boolean isStateFirstMethod(Method method) {
        return method.getAnnotation(StateFirst.class) != null
                && method.getParameterCount() == 1
                && method.getGenericParameterTypes()[0].getTypeName().equals(MessageContext.class.getTypeName())
                && (method.getReturnType().equals(Void.class) || isSendMessageReturnType(method));
    }

    private boolean isSendMessageReturnType(Method method) {
        return method.getReturnType().equals(SendMessage.class);
    }

    private boolean isTryToEditUpdateWithSendMessage(Method method) {
        StateFirst declaredAnnotation = method.getAnnotation(StateFirst.class);
        return declaredAnnotation.tryEdit();
    }

}
