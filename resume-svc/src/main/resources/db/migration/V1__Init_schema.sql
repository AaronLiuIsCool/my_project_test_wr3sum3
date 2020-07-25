--
-- Table structure for table `resume`
--

DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `language` varchar(2) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `basic_info`
--

DROP TABLE IF EXISTS `basic_info`;
CREATE TABLE `basic_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `alias` varchar(20) DEFAULT NULL,
  `city` varchar(100) NOT NULL,
  `country` varchar(60) NOT NULL,
  `email` varchar(100) NOT NULL,
  `full_name` varchar(50) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `province` varchar(255) DEFAULT NULL,
  `resume_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3t3mrpok6s6bg8a90y4j8br47` (`resume_id`),
  CONSTRAINT `FK1kns8xitl0wtox4enf876hw5x` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
CREATE TABLE `profile` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `basic_info_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtg6mox52p5qvmp46fo95vn8yy` (`basic_info_id`),
  CONSTRAINT `FKtg6mox52p5qvmp46fo95vn8yy` FOREIGN KEY (`basic_info_id`) REFERENCES `basic_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `education`
--

DROP TABLE IF EXISTS `education`;
CREATE TABLE `education` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(100) NOT NULL,
  `country` varchar(60) NOT NULL,
  `degree` varchar(20) NOT NULL,
  `end_date` date NOT NULL,
  `gpa` varchar(4) NOT NULL,
  `institution` varchar(50) NOT NULL,
  `major` varchar(50) NOT NULL,
  `start_date` date NOT NULL,
  `resume_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjyor7xegks1vh01tajsof6gb4` (`resume_id`),
  CONSTRAINT `FKjyor7xegks1vh01tajsof6gb4` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `award`
--

DROP TABLE IF EXISTS `award`;
CREATE TABLE `award` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `education_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKete66ps5wrd2a7oxn1oprjms8` (`education_id`),
  CONSTRAINT `FKete66ps5wrd2a7oxn1oprjms8` FOREIGN KEY (`education_id`) REFERENCES `education` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `work_experience`
--

DROP TABLE IF EXISTS `work_experience`;
CREATE TABLE `work_experience` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(100) NOT NULL,
  `country` varchar(60) NOT NULL,
  `description` varchar(500) NOT NULL,
  `end_date` date NOT NULL,
  `organization` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL,
  `start_date` date NOT NULL,
  `resume_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlho647rchg4fd13e5mhpsdbmh` (`resume_id`),
  CONSTRAINT `FKlho647rchg4fd13e5mhpsdbmh` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `certificate`
--

DROP TABLE IF EXISTS `certificate`;
CREATE TABLE `certificate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiration_date` date DEFAULT NULL,
  `issue_date` date DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `resume_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi4oy75n04281os1vrl5k2nmlu` (`resume_id`),
  CONSTRAINT `FKi4oy75n04281os1vrl5k2nmlu` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
