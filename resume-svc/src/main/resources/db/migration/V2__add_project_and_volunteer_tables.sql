--
-- Table structure for table `project_experience`
--

DROP TABLE IF EXISTS `project_experience`;
CREATE TABLE `project_experience` (
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
                                      KEY `FK9jb659lm8t5ruvfeyg9uxwu1b` (`resume_id`),
                                      CONSTRAINT `FK9jb659lm8t5ruvfeyg9uxwu1b` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `volunteer_experience`
--

DROP TABLE IF EXISTS `volunteer_experience`;
CREATE TABLE `volunteer_experience` (
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
                                        KEY `FKhe7tudyysem6hj5rb1ffcaudx` (`resume_id`),
                                        CONSTRAINT `FKhe7tudyysem6hj5rb1ffcaudx` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;