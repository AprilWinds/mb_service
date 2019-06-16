ALTER TABLE `room` ADD COLUMN `hotweight` int(2) not null default 0 after `place_name`;
ALTER TABLE `room` ADD COLUMN `is_active` tinyint(1) not null default 1 after `hotweight`;

ALTER TABLE `feedback` ADD COLUMN `replied` tinyint(1) not null default 0 after `reason`;