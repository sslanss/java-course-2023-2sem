package edu.java.configuration;

import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.TrackingRepository;
import jakarta.validation.constraints.NotNull;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.datasource", ignoreUnknownFields = false)
public record JdbcDatabaseConfiguration(@NotNull String driverClassName, @NotNull String url, @NotNull String username,
                                        @NotNull String password) {
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    @Bean
    public ChatRepository chatRepository(JdbcTemplate jdbcTemplate) {
        return new ChatRepository(jdbcTemplate);
    }

    @Bean
    public LinkRepository linkRepository(JdbcTemplate jdbcTemplate) {
        return new LinkRepository(jdbcTemplate);
    }

    @Bean
    public TrackingRepository trackingRepository(JdbcTemplate jdbcTemplate) {
        return new TrackingRepository(jdbcTemplate);
    }
}
