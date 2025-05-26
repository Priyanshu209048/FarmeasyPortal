CREATE TABLE evaluation_request (
    id          INT PRIMARY KEY,
    scheme_id   INT,
    bank_id     VARCHAR(255),
    age         INT,
    amount      DECIMAL(10),
    cibil_score INT,
    salary      DECIMAL(10)
) ENGINE=InnoDB;
