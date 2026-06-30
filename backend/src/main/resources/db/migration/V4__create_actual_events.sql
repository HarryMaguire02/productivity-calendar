CREATE TABLE actual_events (
    id                      UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                 UUID         NOT NULL,
    title                   VARCHAR(255) NOT NULL,
    description             TEXT,
    start_time              TIMESTAMPTZ  NOT NULL,
    end_time                TIMESTAMPTZ  NOT NULL,
    category                VARCHAR(50)  NOT NULL,
    linked_planned_event_id UUID,
    mood                    SMALLINT,
    created_at              TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_actual_events_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_actual_events_planned_event
        FOREIGN KEY (linked_planned_event_id) REFERENCES planned_events (id) ON DELETE SET NULL,

    CONSTRAINT chk_actual_events_end_after_start
        CHECK (end_time > start_time),

    CONSTRAINT chk_actual_events_category
        CHECK (category IN ('WORK', 'STUDY', 'HEALTH', 'PERSONAL', 'BREAK', 'OTHER')),

    CONSTRAINT chk_actual_events_mood
        CHECK (mood IS NULL OR mood BETWEEN 1 AND 5)
);

CREATE INDEX idx_actual_events_user_start
    ON actual_events (user_id, start_time);
