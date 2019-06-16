alter TABLE `feedback` modify COLUMN `image_url` VARCHAR(254) DEFAULT null;
alter TABLE `feedback` modify COLUMN `description` VARCHAR(254) DEFAULT null;
alter TABLE `report` add COLUMN `description` VARCHAR(254) DEFAULT null after `reason`;
alter TABLE `report` add COLUMN `image_url` VARCHAR(254) DEFAULT null after `description`;