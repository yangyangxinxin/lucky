CREATE TABLE `store_data` (
  `id` bigint(20) NOT NULL auto_increment,
  `access_url` varchar(255) default NULL,
  `cos_path` varchar(255) default NULL,
  `create_time` datetime default NULL,
  `delete_status` int(11) NOT NULL,
  `request_id` varchar(255) default NULL,
  `resource_path` varchar(255) default NULL,
  `source_url` varchar(255) default NULL,
  `update_time` datetime default NULL,
  `url` varchar(255) default NULL,
  `v_id` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8