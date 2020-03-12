/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.19 : Database - lx-base
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`lx-base` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `lx-base`;

/*Table structure for table `lx_log` */

DROP TABLE IF EXISTS `lx_log`;

CREATE TABLE `lx_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate` varchar(100) DEFAULT NULL COMMENT '操作描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `lx_log` */

insert  into `lx_log`(`id`,`operate`,`create_time`) values (1,'查询数据','2020-02-28 16:04:05');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int DEFAULT '0' COMMENT '版本号',
  `deleted` int DEFAULT '0' COMMENT '删除标志',
  `account` varchar(100) DEFAULT NULL COMMENT '开始描述',
  `name` varchar(50) DEFAULT NULL COMMENT '结束描述',
  `password` varchar(500) DEFAULT NULL COMMENT '操作描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`create_time`,`modify_time`,`version`,`deleted`,`account`,`name`,`password`) values (3,NULL,NULL,NULL,NULL,'lx','刘兴','123456'),(4,'2020-03-03 16:57:40','2020-03-03 16:57:40',0,0,'lx','刘兴','123456'),(5,'2020-03-03 17:15:07','2020-03-03 17:15:07',0,0,'lx_2','刘兴红','123');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
