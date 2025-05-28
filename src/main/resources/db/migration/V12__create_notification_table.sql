CREATE TABLE notification (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(255),
    short_message   TEXT,
    full_message    TEXT,
    is_read         BOOLEAN DEFAULT FALSE,
    timestamp       DATETIME,
    farmer          VARCHAR(255)
) ENGINE=InnoDB;