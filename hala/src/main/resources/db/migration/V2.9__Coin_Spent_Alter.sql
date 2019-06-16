ALTER TABLE `coin_spent` change `quantity` `amount` INT(4) NOT NULL;
ALTER table `coin_spent` modify COLUMN `usage_at` VARCHAR(2) NOT NULL;