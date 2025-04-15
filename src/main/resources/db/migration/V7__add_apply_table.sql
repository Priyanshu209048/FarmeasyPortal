CREATE TABLE apply (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    farmer_id   INT NOT NULL,
    scheme_id   INT NOT NULL,
    bank_id     INT NOT NULL,
    date        DATE NOT NULL,
    status_date VARCHAR(255),
    amount      VARCHAR(255) NOT NULL,
    status      VARCHAR(255) NOT NULL,
    review      VARCHAR(255)
) ENGINE=InnoDB;