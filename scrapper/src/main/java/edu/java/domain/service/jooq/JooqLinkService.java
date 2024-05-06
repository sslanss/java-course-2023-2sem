package edu.java.domain.service.jooq;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jooq.tables.pojos.Chats;
import edu.java.domain.model.jooq.tables.pojos.Links;
import edu.java.domain.model.jooq.tables.pojos.Trackings;
import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.domain.repository.jooq.JooqTrackingRepository;
import edu.java.domain.service.LinkService;
import edu.java.exceptions.tracker_exceptions.AlreadyTrackedLinkException;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.UnsupportedLinkFormatException;
import edu.java.exceptions.tracker_exceptions.UntrackedLinkException;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import edu.java.util.LinkValidator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {

    private final JooqLinkRepository jooqLinkRepository;

    private final JooqTrackingRepository jooqTrackingRepository;

    private final JooqChatRepository jooqChatRepository;

    private static final int COUNT_LINKS_FOR_UPDATE = 10;

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        Chats followingChat = jooqChatRepository.getById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        Links trackedLink = jooqLinkRepository.getByUrl(url.toString())
            .orElseGet(() -> {
                if (!LinkValidator.isLinkValid(url)) {
                    throw new UnsupportedLinkFormatException();
                }

                Links newLink = new Links();
                newLink.setUrl(url.toString());
                newLink.setLastCheckedAt(OffsetDateTime.now());
                Long linkId = jooqLinkRepository.add(newLink);
                newLink.setLinkId(linkId);
                return newLink;
            });

        Trackings tracking = new Trackings(followingChat.getChatId(), trackedLink.getLinkId());
        try {
            jooqTrackingRepository.add(tracking);
        } catch (DataAccessException e) {
            throw new AlreadyTrackedLinkException();
        }

        return new LinkResponse(trackedLink.getLinkId(), URI.create(trackedLink.getUrl()));
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        Chats followingChat = jooqChatRepository.getById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        Links untrackedLink = jooqLinkRepository.getByUrl(url.toString())
            .orElseThrow(UntrackedLinkException::new);

        Trackings tracking = new Trackings(followingChat.getChatId(), untrackedLink.getLinkId());
        if (!jooqTrackingRepository.remove(tracking)) {
            throw new UntrackedLinkException();
        }

        if (!jooqTrackingRepository.findChatsByDeletedLinkId(untrackedLink.getLinkId())) {
            jooqLinkRepository.remove(untrackedLink);
        }

        return new LinkResponse(untrackedLink.getLinkId(), URI.create(untrackedLink.getUrl()));
    }

    @Override
    public ListLinksResponse listAllTrackedLinks(Long tgChatId) {
        jooqChatRepository.getById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        return mapLinksToListLinks(jooqTrackingRepository.getLinksByChatId(tgChatId));
    }

    @Override
    public List<Link> listLongestUncheckedLinks() {
        return jooqLinkRepository.findLongestUnchecked(COUNT_LINKS_FOR_UPDATE)
            .stream()
            .map((link) -> new Link(link.getLinkId(), URI.create(link.getUrl()),
                link.getLastCheckedAt()
            ))
            .toList();
    }

    @Override
    public void updateLastChecked(Link link, OffsetDateTime currentCheck) {
        link.setLastChecked(currentCheck);
        jooqLinkRepository.add(new Links(link.getLinkId(), link.getUrl().toString(), link.getLastChecked()));
    }

    private ListLinksResponse mapLinksToListLinks(List<Links> links) {
        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getLinkId(), URI.create(link.getUrl())))
            .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
