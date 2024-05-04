package edu.java.domain.service;

import edu.java.domain.model.Link;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    LinkResponse add(Long tgChatId, URI url);

    LinkResponse remove(Long tgChatId, URI url);

    ListLinksResponse listAllTrackedLinks(Long tgChatId);

    List<Link> listLongestUncheckedLinks();

    void updateLastChecked(Link link, OffsetDateTime currentCheck);

}
