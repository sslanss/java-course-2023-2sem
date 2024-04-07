package edu.java.domain.repository;

import edu.java.domain.model.jdbc.Link;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepository {

    private final JdbcTemplate jdbcTemplate;

    @SuppressWarnings("MultipleStringLiterals")
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

    public Optional<Link> getById(Long id) {
        String sql = """
                SELECT * FROM links
                WHERE link_id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, LINK_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Link> getByUrl(URI url) {
        String sql = """
                SELECT * FROM links
                WHERE url = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, LINK_ROW_MAPPER, url.toString()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long add(Link link) throws DataAccessException {
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
        return (Long) keyHolder.getKeyList().getFirst().get("link_id");
    }

    public void remove(Link link) {
        String sql = """
                DELETE FROM links
                WHERE link_id = ?
                """;
        jdbcTemplate.update(sql, link.getLinkId());
    }

    public List<Link> findLongestUnchecked(int count) {
        String sql = """
                SELECT * FROM links
                ORDER BY last_check_time ASC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, LINK_ROW_MAPPER, count);
    }

    public void updateLastChecked(Link link) {
        String sql = """
                UPDATE links
                SET last_checked_at = ?
                WHERE url = ?
                """;
        jdbcTemplate.update(sql, link.getLastChecked(), link.getUrl());
    }

}
