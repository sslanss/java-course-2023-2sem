package edu.java.domain.jdbc;

import edu.java.domain.model.jdbc.Chat;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcTrackingRepository;
import edu.java.domain.service.TgChatService;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.ChatReregisteringException;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JdbcTgChatService implements TgChatService {

    private final JdbcChatRepository jdbcChatRepository;

    private final JdbcTrackingRepository jdbcTrackingRepository;

    public JdbcTgChatService(JdbcChatRepository jdbcChatRepository, JdbcTrackingRepository jdbcTrackingRepository) {
        this.jdbcChatRepository = jdbcChatRepository;
        this.jdbcTrackingRepository = jdbcTrackingRepository;
    }

    @Override
    public void register(Long tgChatId) {
        Chat registeringChat = new Chat(tgChatId);
        try {
            jdbcChatRepository.add(registeringChat);
        } catch (DataAccessException e) {
            throw new ChatReregisteringException();
        }
    }

    @Override
    public void unregister(Long tgChatId) {
        Chat unregisteringChat = new Chat(tgChatId);
        if (!jdbcChatRepository.remove(unregisteringChat)) {
            throw new ChatNotFoundException();
        }
    }

    @Override
    public List<Chat> listTrackingChats(Link link) {
        return jdbcTrackingRepository.getChatsByLinkId(link.getLinkId());
    }
}
