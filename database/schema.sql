CREATE DATABASE recipes_db;
USE recipes_db;

CREATE TABLE recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cuisine VARCHAR(100),
    title VARCHAR(255),
    rating FLOAT DEFAULT NULL,
    prep_time INT DEFAULT NULL,
    cook_time INT DEFAULT NULL,
    total_time INT DEFAULT NULL,
    description TEXT,
    nutrients JSON,
    serves VARCHAR(50)
);
