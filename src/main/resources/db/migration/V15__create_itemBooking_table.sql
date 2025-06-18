CREATE TABLE item_booking (
    id                      INT PRIMARY KEY AUTO_INCREMENT,
    item_id                 INT NOT NULL,
    farmer_id               VARCHAR(255) NOT NULL,
    start_date              DATE NOT NULL,
    end_date                DATE NOT NULL,
    quantity_requested      INT NOT NULL,
    status                  INT NOT NULL DEFAULT 0,
    total_cost              DECIMAL(19, 2) NOT NULL,
    payment_status          INT NOT NULL DEFAULT 0,
    delivered_status        INT NOT NULL DEFAULT 0
) ENGINE=InnoDB;
