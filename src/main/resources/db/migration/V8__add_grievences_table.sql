CREATE TABLE grievences (
    id                      INT AUTO_INCREMENT PRIMARY KEY,
    farmer_id               VARCHAR(255) NOT NULL,
    bank_id                 VARCHAR(255) NOT NULL,
    grievences_date         DATE NOT NULL,
    grievences_type         VARCHAR(255) NOT NULL,
    grievences_description  VARCHAR(500) NOT NULL,
    grievences_status       VARCHAR(255),
    grievences_review       VARCHAR(500)
) ENGINE=InnoDB;