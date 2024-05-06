package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.LinkTrackerBot;
import edu.java.requests.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {

    private final LinkTrackerBot bot;

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        String updateMessage = "Новые обновления:\n\n"
            + linkUpdateRequest.getDescription();

        linkUpdateRequest.getTgChatIds()
            .forEach((chatId) -> {
                SendMessage linkUpdate = new SendMessage(chatId, updateMessage);
                bot.execute(linkUpdate);
            });
    }

}
