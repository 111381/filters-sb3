-- liquibase formatted sql

-- Create Filter table
CREATE TABLE filter
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    selection VARCHAR(50)  NOT NULL
);

-- Create FilterCriteria base table
CREATE TABLE filter_criteria
(
    id            BIGSERIAL PRIMARY KEY,
    filter_type   VARCHAR(31) NOT NULL,
    filter_id     BIGINT      NOT NULL,
    CONSTRAINT fk_filter_criteria_filter
        FOREIGN KEY (filter_id)
            REFERENCES filter (id)
                ON DELETE CASCADE
);

-- Create AmountCriteria table (joins with filter_criteria)
CREATE TABLE amount_criteria
(
    id             BIGINT PRIMARY KEY,
    condition_type VARCHAR(100),
    amount_value   DECIMAL(19, 2),
    CONSTRAINT fk_amount_criteria_base
        FOREIGN KEY (id)
            REFERENCES filter_criteria (id)
                ON DELETE CASCADE
);

-- Create DateCriteria table (joins with filter_criteria)
CREATE TABLE date_criteria
(
    id             BIGINT PRIMARY KEY,
    condition_type VARCHAR(100),
    date_value     DATE,
    CONSTRAINT fk_date_criteria_base
        FOREIGN KEY (id)
            REFERENCES filter_criteria (id)
                ON DELETE CASCADE
);

-- Create TextCriteria table (joins with filter_criteria)
CREATE TABLE text_criteria
(
    id             BIGINT PRIMARY KEY,
    condition_type VARCHAR(100),
    text_value     VARCHAR(500),
    CONSTRAINT fk_text_criteria_base
        FOREIGN KEY (id)
            REFERENCES filter_criteria (id)
                ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_filter_criteria_filter_id ON filter_criteria (filter_id);
CREATE INDEX idx_filter_criteria_filter_type ON filter_criteria (filter_type);
