package edu.java.domain.service.jpa;

import edu.java.domain.model.jdbc.Chat;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jpa.ChatEntity;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.service.TgChatService;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.ChatReregisteringException;
import edu.java.exceptions.tracker_exceptions.UntrackedLinkException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JpaTgChatService implements TgChatService {

    private final JpaLinkRepository jpaLinkRepository;
    private final JpaChatRepository jpaChatRepository;

    public JpaTgChatService(JpaLinkRepository jpaLinkRepository, JpaChatRepository jpaChatRepository) {
        this.jpaLinkRepository = jpaLinkRepository;
        this.jpaChatRepository = jpaChatRepository;
    }

    @Override
    public void register(Long tgChatId) {
        if (jpaChatRepository.findById(tgChatId).isPresent()) {
            throw new ChatReregisteringException();
        }
        ChatEntity registeringChat = new ChatEntity(tgChatId);
        jpaChatRepository.save(registeringChat);
    }

    @Override
    public void unregister(Long tgChatId) {
        ChatEntity deletingChat = jpaChatRepository.findById(tgChatId).orElseThrow(ChatNotFoundException::new);
        jpaChatRepository.delete(deletingChat);
    }

    @Override
    public List<Chat> listTrackingChats(Link link) {
        return jpaLinkRepository.findByUrl(link.getUrl()).orElseThrow(UntrackedLinkException::new)
            .getTrackingChats()
            .stream()
            .map(chat -> new Chat(chat.getChatId()))
            .toList();
    }
}
