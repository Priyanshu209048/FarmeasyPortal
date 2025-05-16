CREATE TABLE scheme (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    scheme_name         VARCHAR(100),
    scheme_code         VARCHAR(30),
    scheme_description  VARCHAR(1000),
    benefits            VARCHAR(500),
    eligibility         VARCHAR(255),
    min_salary          DECIMAL(10, 2),
    max_salary          DECIMAL(10, 2),
    cibil_score         VARCHAR(10),
    documents           VARCHAR(255),
    roi                 VARCHAR(10),
    tenure              VARCHAR(20),
    scheme_type         VARCHAR(25),
    bank_id             VARCHAR(255) NOT NULL
) ENGINE=InnoDB;