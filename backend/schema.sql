CREATE TABLE IF NOT EXISTS simulation_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    model_id VARCHAR(64) NOT NULL,
    steps INT NOT NULL,
    score INT NOT NULL,
    created_at DATETIME NOT NULL
);
