package edu.java.updater;

import edu.java.api_exceptions.BadRequestException;
import edu.java.api_exceptions.ServerErrorException;
import edu.java.clients.BotClient;
import edu.java.clients.responses.StackOverflowResponse;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.domain.model.jdbc.Link;
import edu.java.exceptions.TooManyRequestsException;
import edu.java.util.LinkValidator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class StackOverflowLinkUpdater implements LinkUpdater {
    public StackOverflowLinkUpdater(StackOverflowClient stackOverflowClient, BotClient botClient) {
        this.stackOverflowClient = stackOverflowClient;
        this.botClient = botClient;
    }

    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;
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
            for (var update : response.items()) {
                try {
                    botClient.sendLinkUpdate(link.getLinkId(), link.getUrl(),
                        String.format(
                            "К вопросу %s добавлен новый комментарий %s в %s",
                            link.getUrl(),
                            update.creationDate().toLocalDate().toString(),
                            update.creationDate().toLocalTime().toString()
                        ),
                        tgChatsIds
                    );
                    //наверное добавить код ошибки
                } catch (TooManyRequestsException | BadRequestException e) {
                    log.error("Client exception: [{}]", e.getClass());
                } catch (ServerErrorException e) {
                    log.error("Server exception: [{}]", e.getCode());
                }
            }
        }
    }
}
