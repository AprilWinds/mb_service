CREATE TABLE `app_material` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `material_url` VARCHAR(254) NOT NULL,
    `action_url` VARCHAR(254) DEFAULT NULL,
    `category` TINYINT(1) NOT NULL,
    `sortby` TINYINT(1) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;