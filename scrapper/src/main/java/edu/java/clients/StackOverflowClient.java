package edu.java.clients;

import edu.java.responses.StackOverflowResponse;
import java.time.OffsetDateTime;

public interface StackOverflowClient {
    StackOverflowResponse getQuestionUpdate(long questionId, OffsetDateTime lastChecked);
}
