package edu.java.clients.stackoverflow;

import edu.java.responses.StackOverflowResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public interface StackOverflowClient {
    StackOverflowResponse getQuestionUpdate(
        long questionId, @NotNull OffsetDateTime fromDate,
        @NotNull OffsetDateTime toDate
    );
}
