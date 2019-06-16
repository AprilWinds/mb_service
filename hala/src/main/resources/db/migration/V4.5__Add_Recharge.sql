CREATE TABLE `recharge` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT NOT NULL,
    `through` INT(1) DEFAULT 0 NOT NULL,
    `out_transaction_code` VARCHAR(254) DEFAULT NULL,
    `transaction_id` VARCHAR(16) NOT NULL,
    `amount` DOUBLE(6, 2) NOT NULL,
    `state` INT(1) DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT `fk_recharge_memberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

