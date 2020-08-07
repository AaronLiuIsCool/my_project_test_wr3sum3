DROP TABLE IF EXISTS `keyword`;
CREATE TABLE `keyword` (
                         `value` varchar(100) NOT NULL,
                         `language` varchar(2) NOT NULL,
                         PRIMARY KEY (`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
