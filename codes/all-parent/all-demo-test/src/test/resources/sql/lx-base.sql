DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `deleted` int(11) DEFAULT '0' COMMENT '删除标志',
  `account` varchar(100) DEFAULT NULL COMMENT '开始描述',
  `name` varchar(50) DEFAULT NULL COMMENT '结束描述',
  `password` varchar(500) DEFAULT NULL COMMENT '操作描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`create_time`,`modify_time`,`version`,`deleted`,`account`,`name`,`password`) values (4,'2020-03-03 16:57:40','2020-03-03 16:57:40',0,0,'lx','刘兴','123456'),(5,'2020-03-03 17:15:07','2020-03-03 17:15:07',0,0,'lx_2','刘兴红','123'),(6,'2020-03-23 15:09:39','2020-03-23 15:09:39',0,0,'zhangsan','张三','123456'),(8,'2020-03-23 15:53:23','2020-03-23 15:53:23',0,0,'wangwu','王五','1234567');
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
