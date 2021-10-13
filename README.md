# 一个好用的 mybatis Typehandler 小工具
首先来建一个 postgresql 数据库的表  
```postgresql
-- 创建表
create table dog
(
	id serial constraint dog_pk primary key,
	name varchar,
	coat_colors varchar[],
	height_length numeric[],
	vaccination_date date[],
	is_ear_pricked_up bool[],
    vaccination_age integer[]
);
-- 插入点数据
INSERT INTO dog (id, name, coat_colors, height_length, vaccination_date, is_ear_pricked_up, vaccination_age)
VALUES (DEFAULT, '旺财', '{黑}', '{22.5, 33.2}', '{20210119,20210120}', '{true, true}', '{1,1}'),
       (DEFAULT, '来福', '{白,灰}', '{26.5, 43.0}', '{20200119}', '{false, false}', '{2}'),
       (DEFAULT, '大黄', '{黄,白}', '{40.5, 50.1}', '{20190101,20210101}', '{false, true}', '{6,8}'),
       (DEFAULT, '四眼', '{黄,黑}', '{43.2, 46.7}', '{}', '{false, true}', '{}');
```

