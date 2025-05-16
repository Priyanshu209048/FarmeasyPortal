CREATE TABLE user (
    id        int NOT NULL AUTO_INCREMENT,
    email     varchar(20) DEFAULT NULL,
    password  varchar(255) DEFAULT NULL,
    role      varchar(30) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;