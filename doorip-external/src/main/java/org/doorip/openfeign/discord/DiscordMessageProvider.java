package org.doorip.openfeign.discord;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.doorip.exception.InvalidValueException;
import org.doorip.message.ErrorMessage;
import org.springframework.stereotype.Component;

import static org.doorip.openfeign.discord.DiscordMessage.createDiscordMessage;

@RequiredArgsConstructor
@Component
public class DiscordMessageProvider {
    private final DiscordFeignClient discordFeignClient;

    public void sendMessage(String message) {
        DiscordMessage discordMessage = createDiscordMessage(message);
        sendMessageToDiscord(discordMessage);
    }

    private void sendMessageToDiscord(DiscordMessage discordMessage) {
        try {
            discordFeignClient.sendMessage(discordMessage);
        } catch (FeignException e) {
            throw new InvalidValueException(ErrorMessage.INVALID_DISCORD_MESSAGE);
        }
    }
}
