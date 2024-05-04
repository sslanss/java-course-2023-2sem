package edu.java.domain.service;

import edu.java.domain.model.jdbc.Chat;
import edu.java.domain.model.jdbc.Link;
import java.util.List;

public interface TgChatService {
    void register(Long tgChatId);

    void unregister(Long tgChatId);

    List<Chat> listTrackingChats(Link link);
}
