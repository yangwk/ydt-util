-- for mysql
-- 测试sql
CREATE TABLE  `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) null,
  `parentId` int null,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_parentId` FOREIGN KEY (`parentId`) REFERENCES `city` (`id`)
  on delete no action
  on update no action
);

-- 插入sql
insert into city(id,name,parentId) values(1,'地球',null);
insert into city(id,name,parentId) values(2,'中国',1);
insert into city(id,name,parentId) values(3,'广西省',2);
insert into city(id,name,parentId) values(4,'钦州市',3);
insert into city(id,name,parentId) values(5,'钦南区',4);
insert into city(id,name,parentId) values(6,'犀牛脚镇',5);
insert into city(id,name,parentId) values(7,'船厂村',6);
insert into city(id,name,parentId) values(8,'广东省',2);
insert into city(id,name,parentId) values(9,'广州市',8);
insert into city(id,name,parentId) values(10,'天河区',9);
insert into city(id,name,parentId) values(11,'番禺区',9);
insert into city(id,name,parentId) values(12,'深圳市',8);
insert into city(id,name,parentId) values(13,'福田区',12);
insert into city(id,name,parentId) values(14,'海南省',2);
insert into city(id,name,parentId) values(15,'青龙村',6);
insert into city(id,name,parentId) values(16,'担水坑',6);
insert into city(id,name,parentId) values(17,'炮台村',6);
insert into city(id,name,parentId) values(18,'西乡村',6);
insert into city(id,name,parentId) values(19,'湖南省',2);
insert into city(id,name,parentId) values(20,'长沙市',19);
insert into city(id,name,parentId) values(21,'新农村',6);
insert into city(id,name,parentId) values(22,'新农屯1',7);
insert into city(id,name,parentId) values(23,'新农屯2',7);

