package edu.java.updater;

import edu.java.domain.model.jdbc.Chat;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.service.LinkService;
import edu.java.domain.service.TgChatService;
import edu.java.util.LinkValidator;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class LinkUpdateChecker {

    private final Map<LinkValidator.LinkType, LinkUpdater> updaterMap;
    private final LinkService linkService;

    private final TgChatService tgChatService;

    public LinkUpdateChecker(
        LinkService linkService, StackOverflowLinkUpdater stackOverflowLinkUpdater,
        GitHubLinkUpdater gitHubLinkUpdater, TgChatService tgChatService
    ) {
        this.linkService = linkService;
        this.tgChatService = tgChatService;
        updaterMap = Map.of(
            LinkValidator.LinkType.STACKOVERFLOW, stackOverflowLinkUpdater,
            LinkValidator.LinkType.GITHUB, gitHubLinkUpdater
        );
    }

    public void checkUpdates() {
        List<Link> linksForUpdate = linkService.listLongestUncheckedLinks();
        for (var link : linksForUpdate) {
            LinkValidator.LinkType linkType = LinkValidator.checkLinkType(link.getUrl());
            var updater = updaterMap.get(linkType);

            OffsetDateTime currentDateTime = OffsetDateTime.now();
            if (updater.haveUpdatesByTime(link, currentDateTime)) {
                List<Long> trackingChatIds = getTrackingChatIds(link);
                updater.sendUpdatesToChats(link, trackingChatIds);
            }

            linkService.updateLastChecked(link, currentDateTime);
        }
    }

    private List<Long> getTrackingChatIds(Link link) {
        return tgChatService.listTrackingChats(link).stream()
            .map(Chat::getChatId)
            .toList();
    }
}
