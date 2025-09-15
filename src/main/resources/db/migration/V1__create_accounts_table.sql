CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number BIGINT NOT NULL UNIQUE,
    balance DECIMAL(19,2) NOT NULL,
    owner_name VARCHAR(255),
    version BIGINT
);