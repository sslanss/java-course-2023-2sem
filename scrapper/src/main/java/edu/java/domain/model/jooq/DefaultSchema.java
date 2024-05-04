/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.model.jooq;

import edu.java.domain.model.jooq.tables.Chats;
import edu.java.domain.model.jooq.tables.Links;
import edu.java.domain.model.jooq.tables.Trackings;
import java.util.Arrays;
import java.util.List;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>CHATS</code>.
     */
    public final Chats CHATS = Chats.CHATS;

    /**
     * The table <code>LINKS</code>.
     */
    public final Links LINKS = Links.LINKS;

    /**
     * The table <code>TRACKINGS</code>.
     */
    public final Trackings TRACKINGS = Trackings.TRACKINGS;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }

    @Override
    @NotNull
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    @NotNull
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Chats.CHATS,
            Links.LINKS,
            Trackings.TRACKINGS
        );
    }
}
