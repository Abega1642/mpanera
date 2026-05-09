CREATE TYPE service_request_status AS ENUM ('OPEN', 'NEGOTIATING', 'ASSIGNED', 'CLOSED', 'EXPIRED');

CREATE TABLE service_requests
(
    id               UUID                   NOT NULL PRIMARY KEY,
    clientId         UUID                   NOT NULL REFERENCES users ("id"),
    categoryId       UUID                   NOT NULL REFERENCES categories ("id"),
    title            TEXT                   NOT NULL,
    description      TEXT                   NOT NULL,
    district         TEXT,
    indicativeBudget DECIMAL(65, 30),
    desiredDeadline  TIMESTAMP(3),
    status           service_request_status NOT NULL DEFAULT 'OPEN',
    createdAt        TIMESTAMP(3)           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiresAt        TIMESTAMP(3)
);