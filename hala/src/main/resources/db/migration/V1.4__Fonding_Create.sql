CREATE TABLE `fonding` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `parent_id` BIGINT NOT NULL,
    `name` VARCHAR(128) NOT NULL,
    `used_count` INT(8) NOT NULL DEFAULT 0,
    `is_system` TINYINT(1) DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX index_on_parent_id ON `fonding`(parent_id);

CREATE TABLE `member_fonding` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT NOT NULL,
    `fonding_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_memberfonding_memberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_memberfonding_fondingid` FOREIGN KEY (fonding_id) REFERENCES fonding(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
