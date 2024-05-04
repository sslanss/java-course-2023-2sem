package edu.java.domain.jdbc;

import edu.java.domain.model.Chat;
import edu.java.domain.model.Link;
import edu.java.domain.model.Tracking;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.TrackingRepository;
import edu.java.domain.service.LinkService;
import edu.java.exceptions.tracker_exceptions.AlreadyTrackedLinkException;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.UntrackedLinkException;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final TrackingRepository trackingRepository;
    private final ChatRepository chatRepository;
    private static final int COUNT_LINKS_FOR_UPDATE = 10;

    public JdbcLinkService(
        LinkRepository linkRepository,
        TrackingRepository trackingRepository,
        ChatRepository chatRepository
    ) {
        this.linkRepository = linkRepository;
        this.trackingRepository = trackingRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        //check case link valid
        Chat followingChat = chatRepository.getById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        Link trackedLink = linkRepository.getByUrl(url)
            .orElseGet(() -> {
                Link newLink = new Link();
                newLink.setUrl(url);
                newLink.setLastChecked(OffsetDateTime.now());
                Long linkId = linkRepository.add(newLink);
                newLink.setLinkId(linkId);
                return newLink;
            });

        Tracking tracking = new Tracking(followingChat.getChatId(), trackedLink.getLinkId());
        try {
            trackingRepository.add(tracking);
        } catch (DataAccessException e) {
            throw new AlreadyTrackedLinkException();
        }

        return new LinkResponse(trackedLink.getLinkId(), trackedLink.getUrl());
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        Chat followingChat = chatRepository.getById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        Link untrackedLink = linkRepository.getByUrl(url)
            .orElseThrow(UntrackedLinkException::new);

        Tracking tracking = new Tracking(followingChat.getChatId(), untrackedLink.getLinkId());
        if (!trackingRepository.remove(tracking)) {
            throw new UntrackedLinkException();
        }

        if (!trackingRepository.findChatsByDeletedLinkId(untrackedLink.getLinkId())) {
            linkRepository.remove(untrackedLink);
        }

        return new LinkResponse(untrackedLink.getLinkId(), untrackedLink.getUrl());
    }

    @Override
    public ListLinksResponse listAllTrackedLinks(Long tgChatId) {
        return mapLinksToListLinks(trackingRepository.getLinksByChatId(tgChatId));
    }

    @Override
    public List<Link> listLongestUncheckedLinks() {
        return linkRepository.findLongestUnchecked(COUNT_LINKS_FOR_UPDATE);
    }

    @Override
    public void updateLastChecked(Link link, OffsetDateTime currentCheck) {
        link.setLastChecked(currentCheck);
        linkRepository.updateLastChecked(link);
    }

    private ListLinksResponse mapLinksToListLinks(List<Link> links) {
        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getLinkId(), link.getUrl()))
            .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
