ALTER TABLE `room` ADD COLUMN `microphone_switched` VARCHAR(32) NOT NULL DEFAULT "1,2,3,4,5" AFTER `microphone_price`;
ALTER TABLE `room_microphone_holding` CHANGE COLUMN `seat_number` `microphone_number` INT(2) NOT NULL DEFAULT 1;