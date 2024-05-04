package edu.java.domain.service.jooq;

import edu.java.domain.model.jdbc.Chat;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jooq.tables.pojos.Chats;
import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.domain.repository.jooq.JooqTrackingRepository;
import edu.java.domain.service.TgChatService;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.ChatReregisteringException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {

    private final JooqChatRepository jooqChatRepository;

    private final JooqTrackingRepository jooqTrackingRepository;

    @Override
    public void register(Long tgChatId) {
        if (jooqChatRepository.getById(tgChatId).isPresent()) {
            throw new ChatReregisteringException();
        }
        Chats registeringChat = new Chats(tgChatId);
        jooqChatRepository.add(registeringChat);
    }

    @Override
    public void unregister(Long tgChatId) {
        Chats unregisteringChat = new Chats(tgChatId);
        if (!jooqChatRepository.remove(unregisteringChat)) {
            throw new ChatNotFoundException();
        }
    }

    @Override
    public List<Chat> listTrackingChats(Link link) {
        return jooqTrackingRepository.getChatsByLinkId(link.getLinkId()).stream()
            .map(chat -> new Chat(chat.getChatId()))
            .toList();
    }
}
