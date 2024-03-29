package edu.java.domain.repository;

import edu.java.domain.model.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    public static final RowMapper<Chat> CHAT_ROW_MAPPER = (resultSet, rowNumber) -> {
        Chat chat = new Chat();
        chat.setChatId(resultSet.getLong("chat_id"));
        return chat;
    };

    @Autowired
    public ChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Chat> getById(Long id) {
        String sql = """
            SELECT * FROM chats
            WHERE chat_id = ?
            """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, CHAT_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        //return Optional.ofNullable(jdbcTemplate.queryForObject(sql, CHAT_ROW_MAPPER, id));
    }

    public void add(Chat chat) throws DataAccessException {
        String sql = """
            INSERT INTO chats (chat_id)
            VALUES (?)
            """;
        jdbcTemplate.update(sql, chat.getChatId());
    }

    public boolean remove(Chat chat) {
        String sql = """
            DELETE FROM chats
            WHERE chat_id = ?
            """;
        return jdbcTemplate.update(sql, chat.getChatId()) == 1;
    }

    public List<Chat> findAll() {
        String sql = """
            SELECT * FROM chats
            """;
        return jdbcTemplate.query(sql, CHAT_ROW_MAPPER);
    }
}
