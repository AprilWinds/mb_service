CREATE  TABLE  `dynamic_report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    `report_member_id` BIGINT NOT NULL,
    `report_id` BIGINT NOT NULL,
    `description` VARCHAR(254),
    `img_url` VARCHAR(254) ,
    `content` VARCHAR(254) ,
    `reason` VARCHAR(8) NOT NULL,
    `handler` TINYINT(5) DEFAULT 0,
    `dynamic_id` BIGINT,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
