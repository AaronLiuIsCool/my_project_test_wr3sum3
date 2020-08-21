CREATE TABLE IF NOT EXISTS account (
                                     id VARCHAR(255),
                                     email VARCHAR(255) NOT NULL,
                                     name VARCHAR(255) NOT NULL default '',
                                     confirmed_and_active BOOLEAN NOT NULL DEFAULT false,
                                     member_since TIMESTAMP NOT NULL default current_timestamp,
                                     password_hash VARCHAR(100) default '',
                                     photo_url VARCHAR(255),
                                     support BOOLEAN NOT NULL DEFAULT false,
                                     PRIMARY KEY (id),
                                     key ix_account_email (email),
                                     key ix_account_phone_number (phone_number)
) ENGINE=InnoDB;
-- timezone issue
-- show variables like '%time_zone%';
-- 可以看到时区是CST,所以需修改
-- set global time_zone='+8:00';
-- set time_zone='+8:00';
-- flush privileges;
-- phoneNumber; not for phase I TODO:Woody