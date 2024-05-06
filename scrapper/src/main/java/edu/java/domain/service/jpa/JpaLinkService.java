package edu.java.domain.service.jpa;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jpa.ChatEntity;
import edu.java.domain.model.jpa.LinkEntity;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
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
import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaChatRepository jpaChatRepository;
    private static final int COUNT_LINKS_FOR_UPDATE = 10;

    public JpaLinkService(
        JpaLinkRepository jpaLinkRepository,
        JpaChatRepository jpaChatRepository
    ) {
        this.jpaLinkRepository = jpaLinkRepository;
        this.jpaChatRepository = jpaChatRepository;
    }

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        ChatEntity followingChat = jpaChatRepository.findById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        LinkEntity trackedLink = jpaLinkRepository.findByUrl(url).orElseGet(() -> {
            if (!LinkValidator.isLinkValid(url)) {
                throw new UnsupportedLinkFormatException();
            }
            LinkEntity newLink = new LinkEntity(url, OffsetDateTime.now());
            return jpaLinkRepository.save(newLink);
        });

        if (followingChat.getTrackedLinks().contains(trackedLink)) {
            throw new AlreadyTrackedLinkException();
        }

        followingChat.getTrackedLinks().add(trackedLink);
        jpaChatRepository.save(followingChat); //?
        return new LinkResponse(trackedLink.getLinkId(), trackedLink.getUrl());
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        ChatEntity followingChat = jpaChatRepository.findById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        LinkEntity untrackedLink = jpaLinkRepository.findByUrl(url)
            .orElseThrow(UntrackedLinkException::new);

        if (!followingChat.getTrackedLinks().remove(untrackedLink)) {
            throw new UntrackedLinkException();
        }

        jpaChatRepository.save(followingChat);

        if (untrackedLink.getTrackingChats().size() == 1) {
            jpaLinkRepository.delete(untrackedLink);
        }

        return new LinkResponse(untrackedLink.getLinkId(), untrackedLink.getUrl());
    }

    @Override
    public ListLinksResponse listAllTrackedLinks(Long tgChatId) {
        ChatEntity followingChat = jpaChatRepository.findById(tgChatId)
            .orElseThrow(ChatNotFoundException::new);

        return mapLinksToListLinks(followingChat.getTrackedLinks());
    }

    @Override
    public List<Link> listLongestUncheckedLinks() {
        return jpaLinkRepository.findByOrderByLastCheckedAtAsc(Limit.of(COUNT_LINKS_FOR_UPDATE))
            .stream()
            .map((linkEntity) -> new Link(linkEntity.getLinkId(), linkEntity.getUrl(), linkEntity.getLastCheckedAt()))
            .toList();
    }

    @Override
    public void updateLastChecked(Link link, OffsetDateTime currentCheck) {
        link.setLastChecked(currentCheck);
        jpaLinkRepository.save(new LinkEntity(link.getLinkId(), link.getUrl(), link.getLastChecked()));
    }

    private ListLinksResponse mapLinksToListLinks(List<LinkEntity> links) {
        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getLinkId(), link.getUrl()))
            .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
