package edu.java.clients;

import edu.java.responses.StackOverflowResponse;
import java.time.OffsetDateTime;
import java.util.List;

public interface StackOverflowClient {
    StackOverflowResponse getQuestionUpdate(long questionId, OffsetDateTime lastChecked);
}
