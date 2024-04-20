package org.doorip.event;

import org.doorip.message.EventMessage;

public record SignUpEvent(
        EventMessage eventMessage,
        String name,
        int count
) {
    public static SignUpEvent of(EventMessage eventMessage, String name, int count) {
        return new SignUpEvent(eventMessage, name, count);
    }
}
