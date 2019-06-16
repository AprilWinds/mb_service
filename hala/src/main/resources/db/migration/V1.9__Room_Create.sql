CREATE TABLE `room` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `owner_id` BIGINT NOT NULL,
    `name` VARCHAR(254) NOT NULL,
    `introduction` VARCHAR(254) NOT NULL,
    `avatar_url` VARCHAR(254) NOT NULL,
    `wealth` INT(8) NOT NULL,
    `attenders_count` INT(8) NOT NULL,
    `microphone_count` INT(2) NOT NULL DEFAULT 5,
    `microphone_price` INT(4) NOT NULL,
    `locate` POINT NOT NULL,
    `place_name` VARCHAR(32) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_room_ownerid` FOREIGN KEY (owner_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `room_entering` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `room_id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,
    `enter_or_exit` TINYINT(1) NOT NULL DEFAULT 1,
    `is_forcing` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_roomentering_roomid` FOREIGN KEY (room_id) REFERENCES room(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_roomentering_memberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `room_microphone_holding` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `room_id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,
    `on_or_off` TINYINT(1) NOT NULL DEFAULT 1,
    `is_forcing` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_roommicrophoneholding_roomid` FOREIGN KEY (room_id) REFERENCES room(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_roommicrophoneholding_memberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `room_administrator` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `room_id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,
    `is_active` TINYINT(1) NOT NULL DEFAULT 1,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_roomadministrator_roomid` FOREIGN KEY (room_id) REFERENCES room(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_roomadministrator_memberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE `gift_giving` ADD COLUMN `room_id` BIGINT NOT NULL DEFAULT 0 AFTER `gift_id`;
