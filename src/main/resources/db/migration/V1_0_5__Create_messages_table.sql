CREATE TABLE messages
(
    id          UUID NOT NULL PRIMARY KEY,
    user_id     UUID NOT NULL REFERENCES users ("id"),
    message     TEXT NOT NULL,
    ai_response TEXT NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);