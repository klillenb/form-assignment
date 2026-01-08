-- liquibase formatted sql

-- changeset klillenb:004-create-form-sectors

-- did not include soft delete nor update timestamp for simplicity
CREATE TABLE form_sectors
(
    id         BIGSERIAL PRIMARY KEY,
    form_id    BIGINT    NOT NULL,
    sector_id  BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_form_sectors_form
        FOREIGN KEY (form_id)
            REFERENCES forms (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_form_sectors_sector
        FOREIGN KEY (sector_id)
            REFERENCES sectors (id)
            ON DELETE RESTRICT
);
