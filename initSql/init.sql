CREATE TABLE `ds_info` (
  `id` VARCHAR(10) NOT NULL COMMENT '数据源ID',
  `url` VARCHAR(200) NOT NULL COMMENT '连接url',
  `driver_class` VARCHAR(200) NOT NULL COMMENT '驱动类',
  `user_name` VARCHAR(32) NOT NULL COMMENT '用户名',
  `password` VARCHAR(32) NOT NULL COMMENT '密码',
  `type` VARCHAR(32) NOT NULL COMMENT '数据库类型',
  `flag` TINYINT(3) NOT NULL COMMENT '数据库状态',
  `max_num` INT(11) NOT NULL COMMENT '最大使用数',
  `used_num` INT(11) NOT NULL COMMENT '已使用数量',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `log_global_id_seq`(
	`id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	`std` VARCHAR(1) NOT NULL,
	UNIQUE KEY(`std`)
);

CREATE TABLE `mylog`(
	`id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	`ip` VARCHAR(15) NULL DEFAULT '' COMMENT'客户端IP地址',
	`server_id` VARCHAR(20) NOT NULL COMMENT'服务器ID',
	`user_id` BIGINT(20) NULL DEFAULT 0 COMMENT'会话中用户ID',
	`global_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT'全局日志ID',
	`level` VARCHAR(10) NOT NULL COMMENT'日志级别',
	`class_name` VARCHAR(100) NOT NULL COMMENT'类名',
	`method` VARCHAR(100) NOT NULL COMMENT'方法名',
	`msg` TEXT NULL COMMENT'日志消息文本',
	`throwable` VARCHAR(500) NULL DEFAULT '' COMMENT'',
	`stacktrace` MEDIUMTEXT NOT NULL COMMENT'堆栈信息',
	`thread_id` VARCHAR(50) NOT NULL COMMENT'线程ID',
	`error_code` VARCHAR(10) NOT NULL COMMENT'错误代码',
	`log_time` DATETIME NOT NULL COMMENT'日志时间'
);
