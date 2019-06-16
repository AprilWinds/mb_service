ALTER TABLE `task` add COLUMN `arabic_description` VARCHAR(254) NOT NULL after `description`;
ALTER TABLE `gift` add COLUMN `arabic_name` VARCHAR(128) NOT NULL after `name`;
ALTER TABLE `fonding` add COLUMN `arabic_name` VARCHAR(128) DEFAULT NULL after `name`;