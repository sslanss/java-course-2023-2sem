package edu.java.domain.repository.jooq;

import edu.java.domain.model.jooq.tables.pojos.Chats;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import static edu.java.domain.model.jooq.Tables.CHATS;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository {
    private final DSLContext dslContext;

    public Optional<Chats> getById(Long id) {
        return dslContext.selectFrom(CHATS)
            .where(CHATS.CHAT_ID.eq(id))
            .fetchOptionalInto(Chats.class);
    }

    //это под вопросом
    public void add(Chats chat) throws DataAccessException {
        dslContext.insertInto(CHATS)
            .set(dslContext.newRecord(CHATS, chat))
            .set(CHATS.CHAT_ID, chat.getChatId())
            .execute();
    }

    public boolean remove(Chats chat) {
        return dslContext.deleteFrom(CHATS)
            .where(CHATS.CHAT_ID.eq(chat.getChatId()))
            .execute() == 1;
    }

    public List<Chats> findAll() {
        return dslContext.selectFrom(CHATS)
            .fetchInto(Chats.class);
    }

}
