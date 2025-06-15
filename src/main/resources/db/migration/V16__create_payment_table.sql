CREATE TABLE payment (
    id                      UUID PRIMARY KEY,
    lending_request_id      VARCHAR(255) NOT NULL,
    amount                  DOUBLE PRECISION NOT NULL CHECK (amount > 0),
    payment_mode            VARCHAR(50) NOT NULL,
    status                  INT NOT NULL DEFAULT 0,
    payment_time            TIMESTAMP
) ENGINE=InnoDB;
