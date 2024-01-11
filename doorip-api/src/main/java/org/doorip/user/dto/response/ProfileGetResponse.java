package org.doorip.user.dto.response;

import org.doorip.user.domain.User;

public record ProfileGetResponse(
        String name,
        String intro,
        int result
) {

    public static ProfileGetResponse of(User user) {
        return new ProfileGetResponse(
                user.getName(),
                user.getIntro(),
                user.getResult().getNumResult()
        );
    }
}
