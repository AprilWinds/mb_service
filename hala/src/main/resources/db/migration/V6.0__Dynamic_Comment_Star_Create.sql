
CREATE TABLE  `dynamic`(
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `member_id` BIGINT NOT NULL ,
  /* `nickname` VARCHAR (254),
  `avatar_url` VARCHAR(254),*/
  `img_url`  VARCHAR(254),
  `content`  VARCHAR(254),
  `star`     int default 0,
  `comment`  int default 0,
  `read`     int default 0,
  `locate`   POINT  NOT NULL,
  `position` VARCHAR(254) NOT NULL ,
   `type`  TINYINT(4) NOT NULL  DEFAULT  1,
  `time`  TIMESTAMP  NOT NULL

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE  TABLE  `comment`(
  `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY  KEY,
  `dynamic_id` BIGINT NOT NULL,
  `parent_id`  BIGINT  DEFAULT 0,
  `from_id`    BIGINT  NOT NULL,
  `to_id`      BIGINT  NOT NULL,
  `content`    VARCHAR(254) NOT NULL,
  `locate`     POINT NOT NULL,
  `position`   VARCHAR(254) NOT NULL,
  `time`  TIMESTAMP  NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE  TABLE  `star`(
 `id` BIGINT  NOT NULL  AUTO_INCREMENT  PRIMARY  KEY,
 `dynamic_id` BIGINT  NOT NULL ,
 `from_id`  BIGINT NOT NULL,
 `to_id`   BIGINT NOT NULL,
 `time` TIMESTAMP  NOT NULL,
 `state` TINYINT(4)  NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;