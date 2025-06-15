CREATE TABLE orders (
    my_order_id     INT AUTO_INCREMENT PRIMARY KEY,
    order_id        VARCHAR(255) NOT NULL UNIQUE,
    amount          VARCHAR(255) NOT NULL,
    receipt         VARCHAR(255),
    status          INT NOT NULL,
    user_id         VARCHAR(255) NOT NULL,
    payment_id      VARCHAR(255),
    booking_id      INT NOT NULL
) ENGINE=InnoDB;