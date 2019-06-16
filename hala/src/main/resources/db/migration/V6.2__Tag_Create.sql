
create  table  `tag`(
    `id` BIGINT NOT NULL AUTO_INCREMENT  PRIMARY KEY ,
    `category`  VARCHAR(128) NOT NULL,
    `tag_name`  VARCHAR(128) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


