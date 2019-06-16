CREATE TABLE `member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `character_id` VARCHAR(16) NOT NULL,
    `nickname` VARCHAR(32) NOT NULL,
    `avatar_url` VARCHAR(254) NOT NULL,
    `dob` VARCHAR(16) NOT NULL,
    `gender` VARCHAR(8) NOT NULL,
    `mobile_number` VARCHAR(16) NULL,
    `facebook_id` VARCHAR(128) DEFAULT NULL,
    `password` VARCHAR(254) DEFAULT NULL,
    `is_active` TINYINT(1) DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX index_on_character_id ON `member`(character_id);
