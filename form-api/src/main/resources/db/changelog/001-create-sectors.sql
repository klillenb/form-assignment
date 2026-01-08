-- liquibase formatted sql

-- changeset klillenb:001-create-sectors

CREATE TABLE sectors
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT      NOT NULL,
    parent_id  BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_sectors_parent
        FOREIGN KEY (parent_id)
            REFERENCES sectors (id)
            ON DELETE CASCADE
);
