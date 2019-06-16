CREATE TABLE  `room_style`(

    `id` BIGINT NOT NULL  AUTO_INCREMENT PRIMARY KEY ,
    `level` int(1) NOT NULL  DEFAULT 0,
    `name`  VARCHAR(20) NOT NULL ,
    `coin`  int(1)  NOT NULL,
    `admin` int(1)  NOT NULL,
    `attender` int(1) NOT NULL,
    `bg_id` varchar(254) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



CREATE  TABLE  `room_background`(

    `id` BIGINT NOT NULL  AUTO_INCREMENT PRIMARY KEY ,
    `bg_type` TINYINT(1) DEFAULT 1,
    `avatar_url` VARCHAR(254) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;