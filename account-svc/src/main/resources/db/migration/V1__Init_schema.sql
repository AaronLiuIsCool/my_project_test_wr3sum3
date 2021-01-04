DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
    id VARCHAR(255),
    openid VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL default '',
    phone_number VARCHAR(255) NOT NULL default '',
    confirmed_and_active BOOLEAN NOT NULL DEFAULT false,
    member_since TIMESTAMP NOT NULL default current_timestamp,
    password_hash VARCHAR(100) default '',
    photo_url VARCHAR(255),
    login_type VARCHAR(16),
    support BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (id),
    key ix_account_email (email),
    key ix_account_phone_number (phone_number)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume` (
    id VARCHAR(255),
    alias VARCHAR(50),
    PRIMARY KEY (id),
    account_id varchar(255) NOT NULL,
    thumbnail_uri VARCHAR(255),
    created_at TIMESTAMP NOT NULL default current_timestamp,
    KEY fk_account (account_id),
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (id)
) ENGINE=InnoDB;
