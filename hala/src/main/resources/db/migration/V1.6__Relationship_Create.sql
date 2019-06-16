CREATE TABLE `relationship` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `follower_id` BIGINT NOT NULL,
    `following_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_relationship_followerid` FOREIGN KEY (follower_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_relationship_followingid` FOREIGN KEY (following_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `blocking` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `blocker_id` BIGINT NOT NULL,
    `blocking_member_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_relationship_blockerid` FOREIGN KEY (blocker_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_relationship_blockingmemberid` FOREIGN KEY (blocking_member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `reporter_id` BIGINT NOT NULL,
    `report_member_id` BIGINT NOT NULL,
    `reason` VARCHAR(16) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id),
    CONSTRAINT `fk_relationship_reporter` FOREIGN KEY (reporter_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_relationship_reportmemberid` FOREIGN KEY (report_member_id) REFERENCES member(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
