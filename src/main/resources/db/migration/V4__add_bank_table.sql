CREATE TABLE bank (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    bank_name       VARCHAR(255),
    bank_address    VARCHAR(255),
    bank_city       VARCHAR(100),
    bank_state      VARCHAR(100),
    bank_zip        VARCHAR(20),
    email           VARCHAR(255),
    bank_phone      VARCHAR(20)
) ENGINE=InnoDB;
