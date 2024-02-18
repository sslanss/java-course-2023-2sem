package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(BotApplication.class, args);
        ApplicationConfig config = context.getBean(ApplicationConfig.class);
        // Создаем экземпляр бота и запускаем его
        try (LinkTrackerBot bot = new LinkTrackerBot(config)) {
            bot.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Bot has encountered an error: " + e.getMessage());
        }

    }
}
