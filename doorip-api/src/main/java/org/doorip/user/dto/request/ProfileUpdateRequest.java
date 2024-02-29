package org.doorip.user.dto.request;

public record ProfileUpdateRequest(
        String name,
        String intro
) {
}
