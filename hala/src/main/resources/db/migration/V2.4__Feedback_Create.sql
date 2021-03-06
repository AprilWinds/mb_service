CREATE TABLE `feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT NOT NULL,
    `description` VARCHAR(254) NOT NULL,
    `image_url` VARCHAR(254) NOT NULL,
    `reason` VARCHAR(8) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_feedback_memberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;