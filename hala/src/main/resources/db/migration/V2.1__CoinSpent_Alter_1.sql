ALTER TABLE `coin_spent` ADD COLUMN `member_id` BIGINT NOT NULL AFTER `id`;
ALTER TABLE `coin_spent` ADD COLUMN `reference_id` BIGINT NOT NULL AFTER `usage_at`;
ALTER TABLE `coin_spent` ADD COLUMN `through` VARCHAR(16) NOT NULL AFTER `reference`;
ALTER TABLE `coin_spent` DROP COLUMN `reference`;