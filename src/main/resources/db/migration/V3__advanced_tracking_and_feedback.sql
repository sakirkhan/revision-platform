ALTER TABLE user_configs ADD COLUMN is_active BOOLEAN DEFAULT TRUE;
ALTER TABLE user_configs ADD COLUMN unsubscribe_reason VARCHAR(255);

ALTER TABLE user_question_history ADD COLUMN is_clicked BOOLEAN DEFAULT FALSE;

CREATE TABLE feedbacks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    image_path VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
