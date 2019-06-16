rename table `coin_spent` to `coin_transaction`;
ALTER TABLE `coin_transaction` change `usage_at` `from_action` VARCHAR(2) NOT NULL;
