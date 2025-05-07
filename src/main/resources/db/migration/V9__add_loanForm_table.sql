CREATE TABLE LoanTable (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(255),
    email              VARCHAR(255),
    contact            VARCHAR(30),
    gender             VARCHAR(30),
    address            VARCHAR(500),
    district           VARCHAR(100),
    state              VARCHAR(100),
    pin_code           VARCHAR(30),
    id_type            VARCHAR(100),
    id_number          VARCHAR(100),

    collateral_type    VARCHAR(255),
    guarantor_name     VARCHAR(255),
    guarantor_contact  VARCHAR(30),
    guarantor_relation VARCHAR(100),

    land_amount        VARCHAR(100),
    khsra_number       VARCHAR(100),
    land_ownership     VARCHAR(100),
    soil_type          VARCHAR(100),
    crop_type          VARCHAR(100),
    pdf_name           VARCHAR(255)
) ENGINE=InnoDB;
