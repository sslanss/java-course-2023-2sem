package edu.java.domain.repository.jooq;

import edu.java.domain.model.jooq.tables.pojos.Chats;
import edu.java.domain.model.jooq.tables.pojos.Links;
import edu.java.domain.model.jooq.tables.pojos.Trackings;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import static edu.java.domain.model.jooq.tables.Chats.CHATS;
import static edu.java.domain.model.jooq.tables.Links.LINKS;
import static edu.java.domain.model.jooq.tables.Trackings.TRACKINGS;

@Repository
@RequiredArgsConstructor
public class JooqTrackingRepository {
    private final DSLContext dslContext;

    public List<Chats> getChatsByLinkId(Long linkId) {
        return dslContext.select()
            .from(TRACKINGS)
            .join(CHATS).on(CHATS.CHAT_ID.eq(TRACKINGS.CHAT_ID))
            .where(TRACKINGS.LINK_ID.eq(linkId))
            .fetchInto(Chats.class);
    }

    public void add(Trackings tracking) throws DataAccessException {
        dslContext.insertInto(TRACKINGS)
            .set(TRACKINGS.CHAT_ID, tracking.getChatId())
            .set(TRACKINGS.LINK_ID, tracking.getLinkId())
            .execute();
    }

    public boolean remove(Trackings tracking) {
        return dslContext.deleteFrom(TRACKINGS)
            .where(TRACKINGS.CHAT_ID.eq(tracking.getChatId())
                .and(TRACKINGS.LINK_ID.eq(tracking.getLinkId())))
            .execute() == 1;
    }

    public List<Trackings> findAll() {
        return dslContext.selectFrom(TRACKINGS)
            .fetchInto(Trackings.class);
    }

    public List<Links> getLinksByChatId(Long tgChatId) {
        return dslContext.select()
            .from(TRACKINGS)
            .join(LINKS).on(LINKS.LINK_ID.eq(TRACKINGS.LINK_ID))
            .where(TRACKINGS.CHAT_ID.eq(tgChatId))
            .fetchInto(Links.class);
    }

    public boolean findChatsByDeletedLinkId(Long linkId) {
        Record1<Boolean> result = dslContext.select(DSL.exists(
                dslContext.selectOne()
                    .from(TRACKINGS)
                    .where(TRACKINGS.LINK_ID.eq(linkId))))
            .fetchOne();

        return result != null && result.value1();
    }

}
