CREATE TABLE scheme_rule (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    rule_name   VARCHAR(255) NOT NULL,
    field       VARCHAR(20) NOT NULL,
    operator    VARCHAR(20) NOT NULL,
    value       VARCHAR(20) NOT NULL,
    scheme_id   INT NOT NULL
) ENGINE=InnoDB;
