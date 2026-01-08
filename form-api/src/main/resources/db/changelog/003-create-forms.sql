-- liquibase formatted sql

-- changeset klillenb:003-create-forms

CREATE TABLE forms
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT      NOT NULL,
    has_agreed BOOLEAN   NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);
