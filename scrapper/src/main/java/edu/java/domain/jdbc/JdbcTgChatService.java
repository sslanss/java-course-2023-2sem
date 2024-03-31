package edu.java.domain.jdbc;

import edu.java.domain.model.Chat;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.service.TgChatService;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.ChatReregisteringException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JdbcTgChatService implements TgChatService {

    private final ChatRepository chatRepository;

    public JdbcTgChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void register(Long tgChatId) {
        Chat registeringChat = new Chat(tgChatId);
        try {
            chatRepository.add(registeringChat);
        } catch (DataAccessException e) {
            throw new ChatReregisteringException();
        }
    }

    @Override
    public void unregister(Long tgChatId) {
        Chat unregisteringChat = new Chat(tgChatId);
        if (!chatRepository.remove(unregisteringChat)) {
            throw new ChatNotFoundException();
        }
    }
}
