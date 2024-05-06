package edu.java.updater;

import edu.java.api_exceptions.BadRequestException;
import edu.java.api_exceptions.ServerErrorException;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.domain.model.jdbc.Link;
import edu.java.exceptions.TooManyRequestsException;
import edu.java.responses.StackOverflowResponse;
import edu.java.updates_sender.BotUpdatesSender;
import edu.java.util.LinkValidator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class StackOverflowLinkUpdater implements LinkUpdater {
    public StackOverflowLinkUpdater(StackOverflowClient stackOverflowClient, BotUpdatesSender botUpdatesSender) {
        this.stackOverflowClient = stackOverflowClient;
        this.botUpdatesSender = botUpdatesSender;
    }

    private final StackOverflowClient stackOverflowClient;
    private final BotUpdatesSender botUpdatesSender;
    private StackOverflowResponse response;

    private long parseQuestionId(URI url) {
        Matcher matcher = LinkValidator.STACK_OVER_FLOW_PATTERN.matcher(url.toString());
        matcher.find();
        return Long.parseLong(matcher.group(1));
    }

    @Override
    public boolean haveUpdatesByTime(Link link, OffsetDateTime currentDateTime) {
        long questionId = parseQuestionId(link.getUrl());
        response = stackOverflowClient.getQuestionUpdate(questionId, link.getLastChecked(),
            currentDateTime
        );
        return response != null && !response.items().isEmpty();
    }

    @Override
    public void sendUpdatesToChats(Link link, List<Long> tgChatsIds) {
        if (response != null && !response.items().isEmpty()) {
            StringBuilder updatesDescription = getUpdatesDescription(link);

            try {
                botUpdatesSender.sendLinkUpdate(link.getLinkId(), link.getUrl(), updatesDescription.toString(),
                    tgChatsIds
                );
            } catch (TooManyRequestsException | BadRequestException e) {
                log.error("Client exception: [{}]", e.getClass());
            } catch (ServerErrorException e) {
                log.error("Server exception: [{}]", e.getCode());
            }
        }
    }

    @NotNull private StringBuilder getUpdatesDescription(Link link) {
        String changesDescription = response.items().size() == 1 ? "добавлен новый комментарий:\n"
           : "добавлены новые комментарии:\n";

        StringBuilder updatesDescription = new StringBuilder(String.format(
            "К вопросу %s ",
            link.getUrl()
        ) + changesDescription);

        for (var update : response.items()) {
            updatesDescription.append(String.format(
                "%s в %s\n",
                update.creationDate().toLocalDate().toString(),
                update.creationDate().toLocalTime().toString()
            ));
        }
        return updatesDescription;
    }
}
