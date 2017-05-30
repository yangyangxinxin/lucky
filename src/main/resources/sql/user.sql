CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL auto_increment,
  `create_time` datetime default NULL,
  `delete_status` int(11) NOT NULL,
  `img_path` varchar(255) default NULL,
  `mobile_phone` varchar(255) default NULL,
  `password` varchar(255) default NULL,
  `update_time` datetime default NULL,
  `username` varchar(255) default NULL,
  PRIMARY KEY  (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8