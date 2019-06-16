

ALTER  TABLE `member` CHANGE COLUMN facebook_id  third_id  VARCHAR(254);
ALTER  TABLE `member` ADD COLUMN  source  varchar(20);