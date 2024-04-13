package edu.java.updater;

import edu.java.domain.model.jdbc.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {

    boolean haveUpdatesByTime(Link link, OffsetDateTime currentDateTime);

    void sendUpdatesToChats(Link link, List<Long> tgChatsIds);
}
