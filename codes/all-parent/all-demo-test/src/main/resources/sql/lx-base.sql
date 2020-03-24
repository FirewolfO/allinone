/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.7.28-log : Database - lx-base
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`lx-base` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `lx-base`;

/*Table structure for table `lx_log` */

DROP TABLE IF EXISTS `lx_log`;

CREATE TABLE `lx_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate` varchar(100) DEFAULT NULL COMMENT '操作描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `lx_log` */

insert  into `lx_log`(`id`,`operate`,`create_time`) values (1,'查询数据','2020-02-28 16:04:05');

/*Table structure for table `user` */

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`create_time`,`modify_time`,`version`,`deleted`,`account`,`name`,`password`) values (4,'2020-03-03 16:57:40','2020-03-03 16:57:40',0,0,'lx','刘兴','123456'),(5,'2020-03-03 17:15:07','2020-03-03 17:15:07',0,0,'lx_2','刘兴红','123'),(6,'2020-03-23 15:09:39','2020-03-23 15:09:39',0,0,'zhangsan','张三','123456'),(8,'2020-03-23 15:53:23','2020-03-23 15:53:23',0,0,'wangwu','王五','1234567'),(9,'2020-03-23 16:09:31','2020-03-23 16:09:31',0,0,'zhaoliu','王五','1234567'),(12,'2020-03-23 17:27:12','2020-03-23 17:27:12',0,0,'hhhh',NULL,NULL),(16,'2020-03-23 17:34:53','2020-03-23 17:34:53',0,0,'jjjj',NULL,NULL),(17,'2020-03-23 17:37:48','2020-03-23 17:37:48',0,0,'mmmmm',NULL,NULL),(18,'2020-03-23 17:54:49','2020-03-23 17:54:49',0,0,'mmmmm',NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
