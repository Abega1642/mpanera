CREATE TABLE categories
(
    id   UUID NOT NULL PRIMARY KEY,
    name TEXT,
    slug TEXT,
    icon TEXT
);

CREATE TABLE providers
(
    id                   UUID PRIMARY KEY REFERENCES users (id),
    bio                  TEXT,
    category_id          UUID REFERENCES categories (id),
    average_rating       DOUBLE PRECISION         DEFAULT 0,
    completed_jobs_count INTEGER                  DEFAULT 0,
    is_verified          BOOLEAN                  DEFAULT FALSE,
    updated_at           TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
)