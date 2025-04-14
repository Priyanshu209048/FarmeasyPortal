CREATE TABLE apply (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    farmer_id   INT NOT NULL,
    scheme_id   INT NOT NULL,
    bank_id     INT NOT NULL,
    date        DATE NOT NULL,
    status_date VARCHAR(255),
    amount      VARCHAR(255) NOT NULL,
    status      VARCHAR(255) NOT NULL,
    review      VARCHAR(255),

    CONSTRAINT fk_apply_farmer FOREIGN KEY (farmer_id) REFERENCES farmer(USER_ID),
    CONSTRAINT fk_apply_scheme FOREIGN KEY (scheme_id) REFERENCES scheme(id),
    CONSTRAINT fk_apply_bank FOREIGN KEY (bank_id) REFERENCES bank(id)
) ENGINE=InnoDB;