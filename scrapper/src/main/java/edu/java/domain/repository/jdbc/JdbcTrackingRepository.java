package edu.java.domain.repository;

import edu.java.domain.model.jdbc.Chat;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.Tracking;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import static edu.java.domain.repository.ChatRepository.CHAT_ROW_MAPPER;
import static edu.java.domain.repository.LinkRepository.LINK_ROW_MAPPER;

@Repository
public class TrackingRepository {

    private final JdbcTemplate jdbcTemplate;

    public static final RowMapper<Tracking> TRACKING_ROW_MAPPER = (resultSet, rowNumber) -> {
        Tracking tracking = new Tracking();
        tracking.setChatId(resultSet.getLong("chat_id"));
        tracking.setLinkId(resultSet.getLong("link_id"));
        return tracking;
    };

    @Autowired
    public TrackingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Chat> getChatsByLinkId(Long linkId) {
        String sql = """
            SELECT chat_id
            FROM trackings
            WHERE link_id = ?
            """;
        return jdbcTemplate.query(sql, CHAT_ROW_MAPPER, linkId);
    }

    public void add(Tracking tracking) throws DataAccessException {
        String sql = """
            INSERT INTO trackings(chat_id, link_id)
            VALUES(?, ?)
            """;
        jdbcTemplate.update(sql, tracking.getChatId(), tracking.getLinkId());

    }

    public boolean remove(Tracking tracking) {
        String sql = """
            DELETE FROM trackings
            WHERE chat_id = ? AND link_id = ?
            """;
        return jdbcTemplate.update(sql, tracking.getChatId(), tracking.getLinkId()) == 1;
    }

    public List<Tracking> findAll() {
        return jdbcTemplate.query("SELECT * FROM trackings", TRACKING_ROW_MAPPER);
    }

    public List<Link> getLinksByChatId(Long tgChatId) {
        String sql = """
                SELECT L.link_id, L.url, L.last_checked_at
                FROM trackings T
                JOIN links L ON T.link_id = L.link_id AND T.chat_id = ?
            """;
        return jdbcTemplate.query(sql, LINK_ROW_MAPPER, tgChatId
        );
    }

    public boolean findChatsByDeletedLinkId(Long linkId) {
        String sql = """
                SELECT EXISTS ( SELECT 1
                                 FROM trackings
                                 WHERE link_id = ?
                                )
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, linkId));
    }
}
