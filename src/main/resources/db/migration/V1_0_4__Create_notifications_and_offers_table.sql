CREATE TYPE notification_status AS ENUM ('SENT', 'VIEWED', 'RESPONDED', 'IGNORED');;
CREATE TYPE offer_status AS ENUM ('PENDING', 'ACCEPTED', 'REFUSED', 'WITHDRAWN');


CREATE TABLE notifications
(
    id                 UUID                     NOT NULL PRIMARY KEY,
    service_request_id UUID                     NOT NULL,
    provider_id        UUID                     NOT NULL,
    status             notification_status      NOT NULL DEFAULT 'SENT',
    sent_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    viewed_at          TIMESTAMP WITH TIME ZONE
);


CREATE TABLE offers
(
    id               UUID            NOT NULL PRIMARY KEY ,
    notification_id   UUID            NOT NULL,
    serviceRequestId UUID            NOT NULL,
    provider_id       UUID            NOT NULL,
    proposed_price    DECIMAL(65, 30) NOT NULL,
    message          TEXT,
    status           offer_status    NOT NULL DEFAULT 'PENDING',
    created_at        TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP,
);