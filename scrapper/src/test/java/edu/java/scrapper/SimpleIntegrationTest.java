package edu.java.scrapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleIntegrationTest extends IntegrationTest {
    private static DataSource dataSource;

    private final static String INSERT_SQL_QUERY = "INSERT INTO chats(chat_id) VALUES(12345),(34567),(56789);";

    private final static String SELECT_SQL_QUERY = "SELECT * FROM chats;";

    private final static String CREATE_SQL_QUERY = """
        CREATE TABLE IF NOT EXISTS users(
            user_id BIGINT PRIMARY KEY
        );
        """;

    @BeforeAll
    public static void getDataSource() {
        dataSource = DataSourceBuilder.create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build();
    }

    @Test
    public void databaseShouldWorkProperlyWithBasicDatabaseOperations() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(INSERT_SQL_QUERY);
        ResultSet resultSet = statement.executeQuery(SELECT_SQL_QUERY);

        String expected = "12345 34567 56789";
        StringBuilder result = new StringBuilder();
        while (resultSet.next()) {
            result.append(resultSet.getString("chat_id") + (resultSet.isLast() ? "" : " "));
        }
        Assertions.assertThat(result.toString()).isEqualTo(expected);
    }

    @Test
    public void databaseShouldWorkProperlyWithSchemaChanges() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(CREATE_SQL_QUERY);

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tablesResultSet = metaData.getTables(null, null, "users",
            new String[] {"TABLE"}
        );
        assertThat(tablesResultSet.next()).isTrue();
    }
}
