CREATE TYPE user_role AS ENUM ('CLIENT', 'PROVIDER');

CREATE TABLE users
(
    id                   UUID PRIMARY KEY NOT NULL,
    clerk_id             VARCHAR(255)     NOT NULL UNIQUE,
    username             VARCHAR(255),
    email                VARCHAR(255)     NOT NULL UNIQUE,
    first_name           VARCHAR(255),
    last_name            VARCHAR(255),
    district             VARCHAR(255),
    city                 VARCHAR(255),
    role                 user_role        NOT NULL,
    email_verified       BOOLEAN                  DEFAULT FALSE,
    on_boarding_complete BOOLEAN                  DEFAULT FALSE,
    created_at           TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
