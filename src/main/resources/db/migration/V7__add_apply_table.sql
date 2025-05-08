CREATE TABLE apply (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    farmer_id   VARCHAR(255) NOT NULL,
    scheme_id   INT NOT NULL,
    bank_id     VARCHAR(255) NOT NULL,
    date        DATE NOT NULL,
    status_date VARCHAR(255),
    amount      VARCHAR(255) NOT NULL,
    status      VARCHAR(255) NOT NULL,
    review      VARCHAR(255)
) ENGINE=InnoDB;