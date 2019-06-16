ALTER TABLE `task` change `icon_name` `symbol` VARCHAR(16) NOT NULL;
CREATE UNIQUE INDEX index_on_symbol ON `task`(symbol);
