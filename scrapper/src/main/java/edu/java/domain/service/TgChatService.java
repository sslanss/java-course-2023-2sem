package edu.java.domain.service;

import edu.java.domain.model.Chat;
import edu.java.domain.model.Link;
import java.util.List;

public interface TgChatService {
    void register(Long tgChatId);

    void unregister(Long tgChatId);

    List<Chat> listTrackingChats(Link link);
}
