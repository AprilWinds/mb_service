ALTER TABLE `room` change `microphone_count` `microphone_limit` INT(2) NOT NULL;
ALTER TABLE `room` change `admin_count` `admin_limit` INT(2) NOT NULL;
ALTER TABLE `room` change `attender_count` `attender_limit` INT(2) NOT NULL;
ALTER TABLE `room` ADD COLUMN `microphone_facing` tinyint(1) NOT NULL default 0 after `microphone_limit`;

