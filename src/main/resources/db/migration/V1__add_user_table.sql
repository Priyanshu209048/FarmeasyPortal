CREATE TABLE user (
    id        int NOT NULL AUTO_INCREMENT,
    email     varchar(255) DEFAULT NULL,
    password  varchar(255) DEFAULT NULL,
    role      varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;