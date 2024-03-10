CREATE TABLE IF NOT EXISTS trackings(
    chat_id BIGINT,
    link_id INTEGER,
    FOREIGN KEY (chat_id) REFERENCES chats (chat_id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES links (link_id) ON DELETE CASCADE,
    PRIMARY KEY (link_id, chat_id)
);
