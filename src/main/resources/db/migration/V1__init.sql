CREATE TABLE IF NOT EXISTS insurance_policy
(
    id
    SERIAL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL,
    status VARCHAR
(
    20
) NOT NULL,
    coverage_start_date DATE NOT NULL,
    coverage_end_date DATE NOT NULL,
    creation_date DATE NOT NULL,
    update_date DATE NOT NULL,
    expiry_date DATE
    );
