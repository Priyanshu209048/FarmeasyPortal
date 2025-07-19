CREATE TABLE evaluation_request (
    id              int NOT NULL AUTO_INCREMENT,
    scheme_code     VARCHAR(20),
    bank_id         VARCHAR(255),
    age             INT,
    amount          DECIMAL(10),
    cibil_score     INT,
    salary          DECIMAL(10),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
