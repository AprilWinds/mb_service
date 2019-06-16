CREATE TABLE `room_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `room_id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,
    `role` TINYINT(1) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_roomrole_roomid` FOREIGN KEY (room_id) REFERENCES room(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_roomrole_memberid` FOREIGN KEY (member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE `room_role` ADD UNIQUE `roomrole_roomid_memberid` (`room_id`, `member_id`);

drop TABLE `room_administrator`;
drop TABLE `room_insider`;
