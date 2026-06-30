CREATE TABLE tasks (
    id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID         NOT NULL,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    status       VARCHAR(20)  NOT NULL DEFAULT 'TODO',
    priority     VARCHAR(10)  NOT NULL DEFAULT 'MEDIUM',
    due_date     DATE,
    category     VARCHAR(50),
    completed_at TIMESTAMPTZ,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_tasks_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT chk_tasks_status
        CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),

    CONSTRAINT chk_tasks_priority
        CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),

    CONSTRAINT chk_tasks_completed_at
        CHECK (status != 'DONE' OR completed_at IS NOT NULL)
);

CREATE INDEX idx_tasks_user_status
    ON tasks (user_id, status);

CREATE INDEX idx_tasks_user_due_date
    ON tasks (user_id, due_date);
