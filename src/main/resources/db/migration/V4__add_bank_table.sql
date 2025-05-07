CREATE TABLE bank (
    id              varchar(255) PRIMARY KEY,
    bank_name       VARCHAR(255),
    bank_address    VARCHAR(255),
    bank_city       VARCHAR(255),
    bank_state      VARCHAR(255),
    bank_zip        VARCHAR(255),
    email           VARCHAR(255),
    bank_phone      VARCHAR(255),
    password        VARCHAR(255)
) ENGINE=InnoDB;
