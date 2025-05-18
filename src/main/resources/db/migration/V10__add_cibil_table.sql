CREATE TABLE cibil (
    id           int NOT NULL AUTO_INCREMENT,
    user_id      VARCHAR(255) NOT NULL ,
    cibil_score  int NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;