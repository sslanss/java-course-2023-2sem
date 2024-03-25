package edu.java.domain.repository;

import edu.java.domain.model.Link;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepository {

    private final JdbcTemplate jdbcTemplate;

    public static final RowMapper<Link> LINK_ROW_MAPPER = (resultSet, rowNumber) -> {
        Link link = new Link();
        link.setLinkId(resultSet.getLong("link_id"));
        link.setUrl(URI.create(resultSet.getString("url")));
        link.setLastChecked(resultSet.getObject("last_checked_at", OffsetDateTime.class));
        return link;
    };

    @Autowired
    public LinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM links", LINK_ROW_MAPPER);
    }

    public Optional<Link> getLinkById(Long id) {
        String sqlSelect = """
            SELECT * FROM links
            WHERE id = ?
            """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlSelect, LINK_ROW_MAPPER, id));
    }

    public Optional<Link> getLinkByUrl(URI url) {
        String sql = """
            SELECT * FROM links
            WHERE url = ?
            """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, LINK_ROW_MAPPER, url));
    }

    public Long add(Link link) {
        String sql = """
            INSERT INTO links (url, last_checked_at)
            VALUES (?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, link.getUrl().toString());
            ps.setObject(2, link.getLastChecked());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void remove(Link link) {
        String sql = """
            DELETE FROM links
            WHERE link_id = ?
            """;
        jdbcTemplate.update(sql, link.getLinkId());
    }

    public List<Link> findOldestLastChecked() {
        String sql = """
            SELECT FROM links
            WHERE last_checked_at =
            """;
        return null;
    }

}
