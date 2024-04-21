package edu.java.updates_sender;

import java.net.URI;
import java.util.List;

public interface BotUpdatesSender {
    void sendLinkUpdate(Long id, URI url, String description, List<Long> tgChatIds);
}
