CREATE TABLE merchant (
    id          VARCHAR(255) PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    phone       VARCHAR(15) NOT NULL,
    email       VARCHAR(150) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    address     TEXT
) ENGINE=InnoDB;