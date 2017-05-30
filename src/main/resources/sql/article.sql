 CREATE TABLE `article` (
  `article_id` bigint(20) NOT NULL auto_increment,
  `comments_count` bigint(20) default NULL,
  `content` varchar(255) default NULL,
  `create_time` datetime default NULL,
  `delete_status` int(11) default NULL,
  `like_count` bigint(20) default NULL,
  `owner_user_id` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `update_time` datetime default NULL,
  `view_count` bigint(20) default NULL,
  PRIMARY KEY  (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8