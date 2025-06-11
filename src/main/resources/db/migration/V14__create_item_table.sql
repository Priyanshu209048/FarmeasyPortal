CREATE TABLE item (
    id              int PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL,
    description     TEXT NOT NULL,
    total_quantity  INT NOT NULL,
    price_per_day   DECIMAL(10, 2) NOT NULL,
    category        INT NOT NULL,
    imageName       VARCHAR(255) NOT NULL,
    merchant_id     VARCHAR(255) NOT NULL
) ENGINE=InnoDB;