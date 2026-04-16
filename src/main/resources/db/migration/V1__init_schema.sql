CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255)
);

CREATE TABLE user_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    notification_time TIME,
    question_count_per_day INT,
    preferred_list VARCHAR(255),
    revision_duration_days INT,
    preferred_topics VARCHAR(500),
    preferred_difficulties VARCHAR(500),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE topics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500)
);

CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL,
    difficulty VARCHAR(50),
    topic_id BIGINT NOT NULL,
    source_lists VARCHAR(255),
    FOREIGN KEY (topic_id) REFERENCES topics(id),
    UNIQUE (title)
);

CREATE TABLE user_question_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    sent_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

CREATE TABLE question_source_lists (
    question_id BIGINT NOT NULL,
    source_list_name VARCHAR(100),
    FOREIGN KEY (question_id) REFERENCES questions(id)
);
