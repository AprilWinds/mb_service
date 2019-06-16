ALTER TABLE `member` ADD COLUMN `locate` POINT AFTER `password`;
ALTER TABLE `member` ADD COLUMN `last_active` TIMESTAMP AFTER `locate`;