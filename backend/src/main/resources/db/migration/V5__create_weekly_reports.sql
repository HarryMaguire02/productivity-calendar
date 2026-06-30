CREATE TABLE weekly_reports (
    id                  UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID         NOT NULL,
    week_start          DATE         NOT NULL,
    week_end            DATE         NOT NULL,
    status              VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    ai_prompt_tokens    INTEGER,
    ai_response_tokens  INTEGER,
    pdf_path            VARCHAR(500),
    generated_at        TIMESTAMPTZ,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_weekly_reports_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT uq_weekly_reports_user_week
        UNIQUE (user_id, week_start),

    CONSTRAINT chk_weekly_reports_week_end_after_start
        CHECK (week_end > week_start),

    CONSTRAINT chk_weekly_reports_status
        CHECK (status IN ('PENDING', 'PROCESSING', 'DONE', 'FAILED')),

    CONSTRAINT chk_weekly_reports_tokens
        CHECK (
            (ai_prompt_tokens IS NULL OR ai_prompt_tokens >= 0) AND
            (ai_response_tokens IS NULL OR ai_response_tokens >= 0)
        )
);
