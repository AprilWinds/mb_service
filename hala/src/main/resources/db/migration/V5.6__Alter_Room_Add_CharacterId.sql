alter table `room` add column character_id varchar(16) default null after `id`;
alter table `room` add unique `unique_on_characterid` (character_id);