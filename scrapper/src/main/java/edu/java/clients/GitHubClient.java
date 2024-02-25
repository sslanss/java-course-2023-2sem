package edu.java.clients;

import edu.java.responses.GitHubResponse;

public interface GitHubClient {
    GitHubResponse getRepositoryUpdate(String owner, String repository);
}
