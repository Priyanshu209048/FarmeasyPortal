CREATE TABLE user (
    id        int NOT NULL AUTO_INCREMENT,
    email     varchar(50) DEFAULT NULL,
    password  varchar(50) DEFAULT NULL,
    role      varchar(50) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;