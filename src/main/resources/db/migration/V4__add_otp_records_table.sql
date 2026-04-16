CREATE TABLE otp_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    otp VARCHAR(255) NOT NULL,
    expiration_time_millis BIGINT NOT NULL
);
