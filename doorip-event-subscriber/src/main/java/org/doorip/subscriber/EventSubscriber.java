package org.doorip.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.doorip.event.SignUpEvent;
import org.doorip.message.EventMessage;
import org.doorip.openfeign.discord.DiscordMessageProvider;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventSubscriber {
    private final DiscordMessageProvider discordMessageProvider;

    @Async
    @EventListener
    public void subscribeSignUpEvent(SignUpEvent event) {
        log.info("[subscribe] sign up event {}", event);
        EventMessage eventMessage = event.eventMessage();
        String message = String.format(eventMessage.getMessage(), event.name(), event.count());
        discordMessageProvider.sendMessage(message);
    }
}
