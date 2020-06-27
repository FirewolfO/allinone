DROP TABLE IF EXISTS `event_rule`;
CREATE TABLE `event_rule` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '事件规则主键、自增',
  `uuid` varchar(40) DEFAULT NULL COMMENT '唯一id，给第三方平台用',
  `zone_id` bigint(11) DEFAULT NULL COMMENT '区域ID',
  `name` varchar(100) DEFAULT NULL COMMENT '等级名',
  `event_level_id` bigint(11) DEFAULT NULL COMMENT '事件等级ID',
  `time_plan_id` bigint(11) DEFAULT NULL COMMENT '时间计划ID',
  `event_invoke_type` varchar(100) DEFAULT NULL COMMENT '联动类型',
  `is_enable` int(11) DEFAULT NULL COMMENT '是否启用',
  `gmt_create` datetime DEFAULT current_timestamp() COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT current_timestamp() COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
insert  into `event_rule`(`id`,`uuid`,`zone_id`,`name`,`event_level_id`,`time_plan_id`,`event_invoke_type`,`is_enable`,`gmt_create`,`gmt_modified`) values
(13,'701c00eae87d4dc2a0588a79a4dd4551',2000,'预置规则1',1,1,'1,2,3',1,'2020-04-27 14:35:12','2020-04-27 14:35:12'),
(15,'701c00eae87d4dc2a0588a79a4dd4552',2000,'预置规则2',1,2,'1,2,3',1,'2020-04-27 14:35:12','2020-04-27 14:35:12');

DROP TABLE IF EXISTS `event_rule_item`;
CREATE TABLE `event_rule_item` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '事件规则项主键、自增',
  `rule_id` bigint(11) DEFAULT NULL COMMENT '事件规则ID',
  `event_type` bigint(11) DEFAULT NULL COMMENT '事件类型',
  `device_id` bigint(11) DEFAULT NULL COMMENT '设备ID',
  `device_set_id` bigint(11) DEFAULT NULL COMMENT '设备setID',
  `gmt_create` datetime DEFAULT current_timestamp() COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT current_timestamp() COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;
insert  into `event_rule_item`(`id`,`rule_id`,`event_type`,`device_id`,`device_set_id`,`gmt_create`,`gmt_modified`) values
(81,13,101,1,22,'2020-04-27 14:35:12','2020-04-27 14:35:12'),
(82,13,102,1,22,'2020-04-27 14:35:12','2020-04-27 14:35:12'),
(83,13,101,2,23,'2020-04-27 14:35:12','2020-04-27 14:35:12'),
(84,13,102,2,23,'2020-04-27 14:35:12','2020-04-27 14:35:12'),
(85,13,101,3,24,'2020-04-27 14:35:12','2020-04-27 14:35:12'),
(86,13,102,3,24,'2020-04-27 14:35:12','2020-04-27 14:35:12'),
(87,15,201,3,25,'2020-04-27 14:35:12','2020-04-27 14:35:12'),
(88,15,202,3,25,'2020-04-27 14:35:12','2020-04-27 14:35:12');

DROP TABLE IF EXISTS `event_level`;
CREATE TABLE `event_level` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '事件等级主键、自增',
  `uuid` varchar(40) DEFAULT NULL COMMENT '唯一id，给第三方平台用',
  `zone_id` bigint(11) DEFAULT NULL COMMENT '区域ID',
  `zone_name` varchar(100) DEFAULT NULL COMMENT '区域名',
  `name` varchar(100) DEFAULT NULL COMMENT '等级名',
  `color` varchar(10) DEFAULT NULL COMMENT '颜色编码',
  `gmt_create` datetime DEFAULT current_timestamp() COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT current_timestamp() COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
insert  into `event_level`(`id`,`uuid`,`zone_id`,`zone_name`,`name`,`color`,`gmt_create`,`gmt_modified`) values
(1,'4efd4d7f-0dfa-4bbc-91a9-dd170100f173',0,'园区测试','高','#D9001B','2020-04-01 12:25:54','2020-04-01 12:25:54'),
(2,'d79945ee-39fa-49a0-9575-81f67d0a69ae',0,'园区测试','中','#E1E600','2020-04-02 11:44:04','2020-04-02 11:44:04'),
(3,'d79945ee-39fa-49a0-9575-269f4c76-4203-4a',0,'园区测试','低','#03B615','2020-04-02 11:44:10','2020-04-02 11:44:10');
