ALTER  TABLE  `room` drop column `style` ;
ALTER  TABLE  `room` add  column `style_id` bigint default 1;