CREATE TABLE `photo` (
  `photo_id` bigint(20) NOT NULL auto_increment,
  `create_time` datetime default NULL,
  `delete_status` int(11) default NULL,
  `name` varchar(255) default NULL,
  `resource_path` varchar(255) default NULL,
  `update_time` datetime default NULL,
  `user_id` bigint(20) default NULL,
  PRIMARY KEY  (`photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8