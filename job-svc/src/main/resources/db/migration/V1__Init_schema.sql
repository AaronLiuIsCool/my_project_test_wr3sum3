-- CREATE DATABASE `kuaidaoresume_job`;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`location` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `country` NVARCHAR(45) NOT NULL,
  `state` NVARCHAR(45) NULL,
  `city` NVARCHAR(45) NOT NULL,
  `post_code` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `country_idx` (`country` ASC),
  INDEX `city_idx` (`city` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`major` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` NVARCHAR(75) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`keyword` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` NVARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`job` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_date` DATETIME NOT NULL,
  `position_title` NVARCHAR(75) NOT NULL,
  `company_name` NVARCHAR(75) NOT NULL,
  `url` NVARCHAR(1000) NOT NULL COMMENT 'encoded',
  `employement_type` NVARCHAR(20) NULL,
  `job_type` NVARCHAR(20) NULL,
  `agency` NVARCHAR(100) NULL COMMENT 'via',
  `salary_min` INT NULL,
  `salary_max` INT NULL,
  `salary_currency` NVARCHAR(5) NULL,
  `year_experience_required` INT NULL,
  `certificate_required` NVARCHAR(45) NULL,
  `education_required` NVARCHAR(45) NULL,
  `job_post_id` VARCHAR(1000) NULL COMMENT 'not a foreign key, job id from the employer',
  `job_description` NVARCHAR(10000) NULL COMMENT 'there are 5000 char job descriptions',
  `location_id` BIGINT COMMENT 'after Jul 22 meeting, can be null',
  PRIMARY KEY (`id`),
  INDEX `fk_job_location1_idx` (`location_id` ASC),
  INDEX `id_idx` (`id` ASC),
  INDEX `post_date_idx` (`post_date` ASC),
  INDEX `position_title_idx` (`position_title` ASC),
  INDEX `company_name_idx` (`company_name` ASC),
  CONSTRAINT `fk_job_location1`
    FOREIGN KEY (`location_id`)
    REFERENCES `kuaidaoresume_job`.`location` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`job_has_required_major` (
  `job_id` BIGINT NOT NULL,
  `major_id` BIGINT NOT NULL,
  PRIMARY KEY (`job_id`, `major_id`),
  INDEX `fk_job_has_major_major1_idx` (`major_id` ASC),
  INDEX `fk_job_has_major_job1_idx` (`job_id` ASC),
  CONSTRAINT `fk_job_has_major_job1`
    FOREIGN KEY (`job_id`)
    REFERENCES `kuaidaoresume_job`.`job` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_job_has_major_major1`
    FOREIGN KEY (`major_id`)
    REFERENCES `kuaidaoresume_job`.`major` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`job_has_keyword` (
  `job_id` BIGINT NOT NULL,
  `keyword_id` BIGINT NOT NULL,
  `rating` DOUBLE NULL,
  PRIMARY KEY (`job_id`, `keyword_id`),
  INDEX `fk_job_has_keyword_keyword1_idx` (`keyword_id` ASC),
  INDEX `fk_job_has_keyword_job1_idx` (`job_id` ASC),
  INDEX `rating_idx` (`rating` ASC),
  CONSTRAINT `fk_job_has_keyword_job1`
    FOREIGN KEY (`job_id`)
    REFERENCES `kuaidaoresume_job`.`job` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_job_has_keyword_keyword1`
    FOREIGN KEY (`keyword_id`)
    REFERENCES `kuaidaoresume_job`.`keyword` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`location_has_keyword` (
  `location_id` BIGINT NOT NULL,
  `keyword_id` BIGINT NOT NULL,
  `rating` DOUBLE NULL,
  PRIMARY KEY (`location_id`, `keyword_id`),
  INDEX `fk_location_has_keyword_keyword1_idx` (`keyword_id` ASC),
  INDEX `fk_location_has_keyword_location1_idx` (`location_id` ASC),
  INDEX `rating_idx` (`rating` ASC),
  CONSTRAINT `fk_location_has_keyword_location1`
    FOREIGN KEY (`location_id`)
    REFERENCES `kuaidaoresume_job`.`location` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_location_has_keyword_keyword1`
    FOREIGN KEY (`keyword_id`)
    REFERENCES `kuaidaoresume_job`.`keyword` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `kuaidaoresume_job`.`major_has_keyword` (
  `major_id` BIGINT NOT NULL,
  `keyword_id` BIGINT NOT NULL,
  `rating` DOUBLE NULL,
  PRIMARY KEY (`major_id`, `keyword_id`),
  INDEX `fk_major_has_keyword_keyword1_idx` (`keyword_id` ASC),
  INDEX `fk_major_has_keyword_major1_idx` (`major_id` ASC),
  INDEX `rating_idx` (`rating` ASC),
  CONSTRAINT `fk_major_has_keyword_major1`
    FOREIGN KEY (`major_id`)
    REFERENCES `kuaidaoresume_job`.`major` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_major_has_keyword_keyword1`
    FOREIGN KEY (`keyword_id`)
    REFERENCES `kuaidaoresume_job`.`keyword` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;