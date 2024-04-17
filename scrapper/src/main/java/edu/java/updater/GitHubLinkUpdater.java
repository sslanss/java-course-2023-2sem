package edu.java.updater;

import edu.java.api_exceptions.BadRequestException;
import edu.java.api_exceptions.ServerErrorException;
import edu.java.clients.BotClient;
import edu.java.clients.github.GitHubClient;
import edu.java.domain.model.jdbc.Link;
import edu.java.exceptions.TooManyRequestsException;
import edu.java.responses.GitHubResponse;
import edu.java.util.LinkValidator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class GitHubLinkUpdater implements LinkUpdater {

    public GitHubLinkUpdater(GitHubClient gitHubClient, BotClient botClient) {
        this.gitHubClient = gitHubClient;
        this.botClient = botClient;
    }

    private final GitHubClient gitHubClient;
    private final BotClient botClient;
    private List<GitHubResponse> response;

    private RepositoryInfo parseRepositoryInfo(URI url) {
        Matcher matcher = LinkValidator.GIT_HUB_PATTERN.matcher(url.toString());
        matcher.find();
        return new RepositoryInfo(matcher.group(1), matcher.group(2));
    }

    @Override
    public boolean haveUpdatesByTime(Link link, OffsetDateTime currentDateTime) {
        RepositoryInfo repoInfo = parseRepositoryInfo(link.getUrl());
        response = gitHubClient.getRepositoryUpdate(repoInfo.owner, repoInfo.repo, link.getLastChecked(),
            currentDateTime
        );
        return response != null && !response.isEmpty();
    }

    @Override
    public void sendUpdatesToChats(Link link, List<Long> tgChatsIds) {
        if (response != null && !response.isEmpty()) {
            for (var update : response) {
                try {
                    botClient.sendLinkUpdate(link.getLinkId(), link.getUrl(),
                        String.format(
                            "Изменение в репозитории %s:\n %s %s в %s",
                            link.getUrl(),
                            update.activityType(), update.lastModified().toLocalDate().toString(),
                            update.lastModified().toLocalTime().toString()
                        ),
                        tgChatsIds
                    );
                } catch (TooManyRequestsException | BadRequestException e) {
                    log.error("Client exception: [{}]", e.getClass());
                } catch (ServerErrorException e) {
                    log.error("Server exception: [{}]", e.getCode());
                }
            }
        }
    }

    record RepositoryInfo(String owner, String repo) {
    }
}
