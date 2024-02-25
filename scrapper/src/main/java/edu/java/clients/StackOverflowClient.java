package edu.java.clients;

import edu.java.responses.StackOverflowResponse;

public interface StackOverflowClient {
    StackOverflowResponse getQuestionUpdate(long questionId);
}
