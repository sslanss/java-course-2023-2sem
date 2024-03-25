CREATE TABLE IF NOT EXISTS links(
    link_id BIGSERIAL PRIMARY KEY,
    url TEXT UNIQUE NOT NULL,
    last_checked_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);