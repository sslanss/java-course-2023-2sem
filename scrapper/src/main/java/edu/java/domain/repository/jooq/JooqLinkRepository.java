package edu.java.domain.repository.jooq;

import edu.java.domain.model.jooq.tables.pojos.Links;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import static edu.java.domain.model.jooq.tables.Links.LINKS;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository {
    private final DSLContext dslContext;

    public List<Links> findAll() {
        return dslContext.selectFrom(LINKS)
            .fetchInto(Links.class);
    }

    public Optional<Links> getById(Long id) {
        return dslContext.selectFrom(LINKS)
            .where(LINKS.LINK_ID.eq(id))
            .fetchOptionalInto(Links.class);
    }

    public Optional<Links> getByUrl(String url) {
        return dslContext.selectFrom(LINKS)
            .where(LINKS.URL.eq(url))
            .fetchOptionalInto(Links.class);
    }

    public Long add(Links link) throws DataAccessException {
        return dslContext.insertInto(LINKS)
            .set(LINKS.URL, link.getUrl())
            .set(LINKS.LAST_CHECKED_AT, link.getLastCheckedAt())
            .returning(LINKS.LINK_ID)
            .fetchOptional()
            .orElseThrow(() -> new DataAccessException(""))
            .get(LINKS.LINK_ID);
    }

    public void remove(Links link) {
        dslContext.deleteFrom(LINKS)
            .where(LINKS.LINK_ID.eq(link.getLinkId()))
            .execute();
    }

    public List<Links> findLongestUnchecked(int count) {
        return dslContext.selectFrom(LINKS)
            .orderBy(LINKS.LAST_CHECKED_AT.asc())
            .limit(count)
            .fetchInto(Links.class);
    }

    public void updateLastChecked(Links link) {
        dslContext.update(LINKS)
            .set(LINKS.LAST_CHECKED_AT, link.getLastCheckedAt())
            .where(LINKS.URL.eq(link.getUrl()))
            .execute();
    }
}
