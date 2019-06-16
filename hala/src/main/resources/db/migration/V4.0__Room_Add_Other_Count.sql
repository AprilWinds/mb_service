ALTER TABLE `room` ADD COLUMN `fans_count` INT(8) DEFAULT 0 AFTER `attenders_count`;
ALTER TABLE `room` ADD COLUMN `insiders_count` INT(8) DEFAULT 0 AFTER `fans_count`;