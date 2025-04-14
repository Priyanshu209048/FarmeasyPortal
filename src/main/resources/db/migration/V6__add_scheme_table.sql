CREATE TABLE scheme (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    scheme_name         VARCHAR(255),
    scheme_code         VARCHAR(255),
    scheme_description  VARCHAR(1000),
    benefits            VARCHAR(1000),
    eligibility         VARCHAR(1000),
    documents           VARCHAR(1000),
    roi                 VARCHAR(255),
    tenure              VARCHAR(255),
    scheme_type         VARCHAR(255),
    bank_id             INT NOT NULL,

    CONSTRAINT fk_scheme_bank FOREIGN KEY (bank_id) REFERENCES bank(id)
) ENGINE=InnoDB;