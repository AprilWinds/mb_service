CREATE TABLE `room_banning` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `room_id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,
    `ban_member_id` BIGINT NOT NULL,
    `acting` TINYINT(1) NOT NULL DEFAULT 0,
    `reference_id` BIGINT DEFAULT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_roombanning_roomid` FOREIGN KEY (room_id) REFERENCES room(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_roombanning_memberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_roombanning_banmemberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE `room_entering` CHANGE `enter_or_exit` `inout` TINYINT(1) NOT NULL DEFAULT 1;
ALTER TABLE `room_entering` DROP COLUMN `is_forcing`;

ALTER TABLE `room_microphone_holding` CHANGE `on_or_off` `onoff` TINYINT(1) NOT NULL DEFAULT 1;
ALTER TABLE `room_microphone_holding` DROP COLUMN `is_forcing`;
