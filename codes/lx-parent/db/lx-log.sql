/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.19 : Database - lx-log
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`lx-log` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `lx-log`;

/*Table structure for table `lx_log` */

DROP TABLE IF EXISTS `lx_log`;

CREATE TABLE `lx_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int DEFAULT '0' COMMENT '版本号',
  `deleted` int DEFAULT '0' COMMENT '删除标志',
  `start` varchar(200) DEFAULT NULL COMMENT '开始描述',
  `end` varchar(200) DEFAULT NULL COMMENT '结束描述',
  `operate` varchar(100) DEFAULT NULL COMMENT '操作描述',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作者',
  `method` varchar(100) DEFAULT NULL COMMENT '执行的方法',
  `parms` varchar(200) DEFAULT NULL COMMENT '执行方法的参数',
  `error` text COMMENT '错误描述',
  `result` text COMMENT '结果',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `lx_log` */

insert  into `lx_log`(`id`,`create_time`,`modify_time`,`version`,`deleted`,`start`,`end`,`operate`,`operator`,`method`,`parms`,`error`,`result`,`start_time`,`end_time`) values (2,'2020-03-01 21:05:01','2020-03-01 21:05:01',0,0,'start test','execute test successfully','test','liuxing','UserController.test','',NULL,'Response(requestId=null, code=200, msg=LX_RESPONSE_001, data=hello)','2020-03-01 13:05:01','2020-03-01 13:05:01'),(3,'2020-03-01 21:57:12','2020-03-01 21:57:12',0,0,'start test','execute test successfully','test','liuxing','UserController.test','',NULL,'Response(requestId=null, code=200, msg=LX_RESPONSE_001, data=hello)','2020-03-01 13:57:12','2020-03-01 13:57:12'),(4,'2020-03-03 09:44:29','2020-03-03 09:44:29',0,0,'start test','execute test log successfully','test log',NULL,'UserController.test','',NULL,'Response(requestId=null, code=200, msg=LX_RESPONSE_001, data=hello)','2020-03-03 01:44:28','2020-03-03 01:44:28'),(5,'2020-03-03 09:47:18','2020-03-03 09:47:18',0,0,'start test user','execute test log successfully','test log',NULL,'UserController.test','',NULL,'Response(requestId=null, code=200, msg=LX_RESPONSE_001, data=hello)','2020-03-03 01:47:18','2020-03-03 01:47:18');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
