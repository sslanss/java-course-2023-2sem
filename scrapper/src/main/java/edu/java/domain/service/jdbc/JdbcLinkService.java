package edu.java.domain.service.jdbc;

import edu.java.domain.model.jdbc.Chat;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.Tracking;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcTrackingRepository;
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
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcTrackingRepository jdbcTrackingRepository;
    private final JdbcChatRepository jdbcChatRepository;
    private static final int COUNT_LINKS_FOR_UPDATE = 10;

    public JdbcLinkService(
        JdbcLinkRepository jdbcLinkRepository,
        JdbcTrackingRepository jdbcTrackingRepository,
        JdbcChatRepository jdbcChatRepository
    ) {
        this.jdbcLinkRepository = jdbcLinkRepository;
        this.jdbcTrackingRepository = jdbcTrackingRepository;
        this.jdbcChatRepository = jdbcChatRepository;
    }

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        Chat followingChat = jdbcChatRepository.getById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        Link trackedLink = jdbcLinkRepository.getByUrl(url)
            .orElseGet(() -> {
                if (!LinkValidator.isLinkValid(url)) {
                    throw new UnsupportedLinkFormatException();
                }

                Link newLink = new Link();
                newLink.setUrl(url);
                newLink.setLastChecked(OffsetDateTime.now());
                Long linkId = jdbcLinkRepository.add(newLink);
                newLink.setLinkId(linkId);
                return newLink;
            });

        Tracking tracking = new Tracking(followingChat.getChatId(), trackedLink.getLinkId());
        try {
            jdbcTrackingRepository.add(tracking);
        } catch (DataAccessException e) {
            throw new AlreadyTrackedLinkException();
        }

        return new LinkResponse(trackedLink.getLinkId(), trackedLink.getUrl());
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        Chat followingChat = jdbcChatRepository.getById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        Link untrackedLink = jdbcLinkRepository.getByUrl(url)
            .orElseThrow(UntrackedLinkException::new);

        Tracking tracking = new Tracking(followingChat.getChatId(), untrackedLink.getLinkId());
        if (!jdbcTrackingRepository.remove(tracking)) {
            throw new UntrackedLinkException();
        }

        if (!jdbcTrackingRepository.findChatsByDeletedLinkId(untrackedLink.getLinkId())) {
            jdbcLinkRepository.remove(untrackedLink);
        }

        return new LinkResponse(untrackedLink.getLinkId(), untrackedLink.getUrl());
    }

    @Override
    public ListLinksResponse listAllTrackedLinks(Long tgChatId) {
        Chat followingChat = jdbcChatRepository.getById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        return mapLinksToListLinks(jdbcTrackingRepository.getLinksByChatId(tgChatId));
    }

    @Override
    public List<Link> listLongestUncheckedLinks() {
        return jdbcLinkRepository.findLongestUnchecked(COUNT_LINKS_FOR_UPDATE);
    }

    @Override
    public void updateLastChecked(Link link, OffsetDateTime currentCheck) {
        link.setLastChecked(currentCheck);
        jdbcLinkRepository.updateLastChecked(link);
    }

    private ListLinksResponse mapLinksToListLinks(List<Link> links) {
        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getLinkId(), link.getUrl()))
            .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
