package edu.java.configuration;

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
}
