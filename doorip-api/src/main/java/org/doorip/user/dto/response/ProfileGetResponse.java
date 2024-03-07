package org.doorip.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.user.domain.User;

@Builder(access = AccessLevel.PRIVATE)
public record ProfileGetResponse(
        String name,
        String intro,
        int result
) {
    public static ProfileGetResponse of(User user) {
        return ProfileGetResponse.builder()
                .name(user.getName())
                .intro(user.getIntro())
                .result(user.getResult() == null ? -1 : user.getResult().getNumResult())
                .build();
    }
}
