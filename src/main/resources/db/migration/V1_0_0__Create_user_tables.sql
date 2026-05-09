CREATE TABLE users (
    id UUID PRIMARY KEY NOT NULL ,
    clerk_id VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email_verified BOOLEAN DEFAULT FALSE
);