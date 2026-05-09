CREATE TABLE service_request_photos
(
    id                 UUID    NOT NULL PRIMARY KEY,
    service_request_id UUID    NOT NULL REFERENCES service_requests ("id"),
    storage_key        TEXT    NOT NULL,
    display_order      INTEGER NOT NULL DEFAULT 0
);