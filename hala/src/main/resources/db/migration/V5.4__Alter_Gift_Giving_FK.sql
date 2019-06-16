alter table `gift_giving` drop foreign key `fk_giftgiving_giftid`;
alter table `gift_giving` add constraint `fk_giftgiving_giftid` foreign key (gift_id) references `gift` (id) on DELETE CASCADE;