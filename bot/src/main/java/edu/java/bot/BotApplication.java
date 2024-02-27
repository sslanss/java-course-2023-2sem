package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@Slf4j
public class BotApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BotApplication.class, args);
        ApplicationConfig config = context.getBean(ApplicationConfig.class);
        try (LinkTrackerBot bot = new LinkTrackerBot(config)) {
            bot.start();
        } catch (Exception e) {
            log.error("Bot failed with error: - Description: {}", e.getMessage());
        }

    }
}
