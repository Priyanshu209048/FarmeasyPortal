CREATE TABLE grievences (
    id                      INT AUTO_INCREMENT PRIMARY KEY,
    farmer_id               INT NOT NULL,
    bank_id                 INT NOT NULL,
    grievences_date         DATE NOT NULL,
    grievences_type         VARCHAR(255) NOT NULL,
    grievences_description  VARCHAR(500) NOT NULL,
    grievences_status       VARCHAR(255),
    grievences_review       VARCHAR(500),

    CONSTRAINT fk_grievences_farmer FOREIGN KEY (farmer_id) REFERENCES FARMER(USER_ID),
    CONSTRAINT fk_grievences_bank FOREIGN KEY (bank_id) REFERENCES bank(id)
) ENGINE=InnoDB;