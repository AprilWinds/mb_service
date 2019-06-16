ALTER TABLE member add COLUMN `spent` int(4) DEFAULT 0 after `coins`;
ALTER TABLE member MODIFY COLUMN `gender` tinyint(1) DEFAULT 0;
alter TABLE room add COLUMN `style` tinyint(1) DEFAULT 0 after `avatar_url`;
ALTER TABLE room modify COLUMN `insider_price` int(4) not null after `microphone_price`;
ALTER TABLE room add COLUMN `admin_count` int(4) NOT NULL DEFAULT 0 after `insider_price`;
ALTER TABLE room add COLUMN `attender_count` int(4) NOT NULL DEFAULT 0 after `admin_count`;
ALTER TABLE `coin_transaction` modify COLUMN `from_action` tinyint(1) NOT NULL;
