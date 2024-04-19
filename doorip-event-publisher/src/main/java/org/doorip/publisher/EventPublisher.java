package org.doorip.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.doorip.event.SignUpEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishSignUpEvent(SignUpEvent event) {
        log.info("[publish] sign up event {}", event);
        eventPublisher.publishEvent(event);
    }
}
