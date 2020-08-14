CREATE DATABASE `kuaidaoresume_job`;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`Location` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `country` NVARCHAR(45) NULL,
  `city` NVARCHAR(20) NULL,
  `post_code` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `country_idx` (`country` ASC),
  INDEX `city_idx` (`city` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`Major` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` NVARCHAR(20) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`Keyword` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` NVARCHAR(20) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`Job` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `post_date` DATETIME NOT NULL,
  `position_title` NVARCHAR(20) NOT NULL,
  `company_name` NVARCHAR(20) NOT NULL,
  `url` VARCHAR(255) NOT NULL COMMENT 'encoded',
  `salary_min` INT NULL,
  `salary_max` INT NULL,
  `salary_currency` NVARCHAR(5) NULL,
  `year_experience_required` TINYINT NULL,
  `certificate_required` NVARCHAR(45) NULL,
  `education_required` NVARCHAR(45) NULL,
  `job_post_id` VARCHAR(20) NULL COMMENT 'not a foreign key, job id from the employer',
  `job_description` NVARCHAR(1000) NULL,
  `Location_id` INT COMMENT 'after Jul 22 meeting, can be null',
  PRIMARY KEY (`id`),
  INDEX `fk_Job_Location1_idx` (`Location_id` ASC),
  INDEX `id_idx` (`id` ASC),
  INDEX `post_date_idx` (`post_date` ASC),
  INDEX `position_title_idx` (`position_title` ASC),
  INDEX `company_name_idx` (`company_name` ASC),
  CONSTRAINT `fk_Job_Location1`
    FOREIGN KEY (`Location_id`)
    REFERENCES `kuaidaoresume_job`.`Location` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`Job_has_Required_Major` (
  `Job_id` INT NOT NULL,
  `Major_id` INT NOT NULL,
  PRIMARY KEY (`Job_id`, `Major_id`),
  INDEX `fk_Job_has_Major_Major1_idx` (`Major_id` ASC),
  INDEX `fk_Job_has_Major_Job1_idx` (`Job_id` ASC),
  CONSTRAINT `fk_Job_has_Major_Job1`
    FOREIGN KEY (`Job_id`)
    REFERENCES `kuaidaoresume_job`.`Job` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Job_has_Major_Major1`
    FOREIGN KEY (`Major_id`)
    REFERENCES `kuaidaoresume_job`.`Major` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`Job_has_Keyword` (
  `Job_id` INT NOT NULL,
  `Keyword_id` INT NOT NULL,
  `rating` DECIMAL(10) NULL,
  PRIMARY KEY (`Job_id`, `Keyword_id`),
  INDEX `fk_Job_has_Keyword_Keyword1_idx` (`Keyword_id` ASC),
  INDEX `fk_Job_has_Keyword_Job1_idx` (`Job_id` ASC),
  INDEX `rating_idx` (`rating` ASC),
  CONSTRAINT `fk_Job_has_Keyword_Job1`
    FOREIGN KEY (`Job_id`)
    REFERENCES `kuaidaoresume_job`.`Job` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Job_has_Keyword_Keyword1`
    FOREIGN KEY (`Keyword_id`)
    REFERENCES `kuaidaoresume_job`.`Keyword` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`Location_has_Keyword` (
  `Location_id` INT NOT NULL,
  `Keyword_id` INT NOT NULL,
  `rating` DECIMAL(10) NULL,
  PRIMARY KEY (`Location_id`, `Keyword_id`),
  INDEX `fk_Location_has_Keyword_Keyword1_idx` (`Keyword_id` ASC),
  INDEX `fk_Location_has_Keyword_Location1_idx` (`Location_id` ASC),
  INDEX `rating_idx` (`rating` ASC),
  CONSTRAINT `fk_Location_has_Keyword_Location1`
    FOREIGN KEY (`Location_id`)
    REFERENCES `kuaidaoresume_job`.`Location` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Location_has_Keyword_Keyword1`
    FOREIGN KEY (`Keyword_id`)
    REFERENCES `kuaidaoresume_job`.`Keyword` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`Major_has_Keyword` (
  `Major_id` INT NOT NULL,
  `Keyword_id` INT NOT NULL,
  `rating` DECIMAL(10) NULL,
  PRIMARY KEY (`Major_id`, `Keyword_id`),
  INDEX `fk_Major_has_Keyword_Keyword1_idx` (`Keyword_id` ASC),
  INDEX `fk_Major_has_Keyword_Major1_idx` (`Major_id` ASC),
  INDEX `rating_idx` (`rating` ASC),
  CONSTRAINT `fk_Major_has_Keyword_Major1`
    FOREIGN KEY (`Major_id`)
    REFERENCES `kuaidaoresume_job`.`Major` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Major_has_Keyword_Keyword1`
    FOREIGN KEY (`Keyword_id`)
    REFERENCES `kuaidaoresume_job`.`Keyword` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;