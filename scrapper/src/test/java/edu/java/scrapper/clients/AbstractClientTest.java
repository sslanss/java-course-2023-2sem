package edu.java.scrapper.clients;

import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;

public class AbstractClientTest {
    @SneakyThrows
    public String jsonToString(String filePath) {
        return Files.readString(Paths.get(filePath));
    }
}
