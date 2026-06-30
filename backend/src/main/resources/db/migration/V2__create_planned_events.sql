CREATE TABLE planned_events (
    id               UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID         NOT NULL,
    title            VARCHAR(255) NOT NULL,
    description      TEXT,
    start_time       TIMESTAMPTZ  NOT NULL,
    end_time         TIMESTAMPTZ  NOT NULL,
    category         VARCHAR(50)  NOT NULL,
    color            VARCHAR(7),
    recurrence_rule  VARCHAR(255),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_planned_events_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT chk_planned_events_end_after_start
        CHECK (end_time > start_time),

    CONSTRAINT chk_planned_events_category
        CHECK (category IN ('WORK', 'STUDY', 'HEALTH', 'PERSONAL', 'BREAK', 'OTHER')),

    CONSTRAINT chk_planned_events_color
        CHECK (color IS NULL OR color ~ '^#[0-9A-Fa-f]{6}$')
);

CREATE INDEX idx_planned_events_user_start
    ON planned_events (user_id, start_time);
