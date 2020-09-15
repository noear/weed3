CREATE TABLE `appx` (
  `app_id` int NOT NULL AUTO_INCREMENT ,
  `app_key` varchar(40) DEFAULT NULL ,
  `akey` varchar(40) DEFAULT NULL ,
  `ugroup_id` int DEFAULT '0' ,
  `agroup_id` int DEFAULT NULL ,
  `name` varchar(50) DEFAULT NULL ,
  `note` varchar(50) DEFAULT NULL ,
  `ar_is_setting` int NOT NULL DEFAULT '0' ,
  `ar_is_examine` int NOT NULL DEFAULT '0' ,
  `ar_examine_ver` int NOT NULL DEFAULT '0' ,
  `log_fulltime` datetime DEFAULT NULL,
  PRIMARY KEY (`app_id`)
) ;

CREATE TABLE `appx_copy` (
  `app_id` int NOT NULL AUTO_INCREMENT,
  `app_key` varchar(40) DEFAULT NULL ,
  `akey` varchar(40) DEFAULT NULL ,
  `ugroup_id` int DEFAULT '0' ,
  `agroup_id` int DEFAULT NULL ,
  `name` varchar(50) DEFAULT NULL ,
  `note` varchar(50) DEFAULT NULL ,
  `ar_is_setting` int NOT NULL DEFAULT '0' ,
  `ar_is_examine` int NOT NULL DEFAULT '0' ,
  `ar_examine_ver` int NOT NULL DEFAULT '0' ,
  `log_fulltime` datetime DEFAULT NULL,
  PRIMARY KEY (`app_id`)
)  ;



INSERT INTO `appx` VALUES ('1', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', '测试', '1', '0', '0', '2017-08-25 10:59:44');
INSERT INTO `appx` VALUES ('2', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', '测试', '0', '0', '0', '2017-08-11 18:05:27');
INSERT INTO `appx` VALUES ('3', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '1', '0', '0', '2017-08-25 10:59:44');
INSERT INTO `appx` VALUES ('4', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', '', '0', '0', '0', '2017-08-04 17:19:41');
INSERT INTO `appx` VALUES ('5', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-91', '', '0', '0', '0', '2017-08-04 17:16:18');
INSERT INTO `appx` VALUES ('6', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-anzhuo', '', '0', '0', '0', '2017-08-04 17:23:45');
INSERT INTO `appx` VALUES ('7', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-xiaomi', '', '0', '0', '0', '2017-08-25 10:47:44');
INSERT INTO `appx` VALUES ('8', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '1', '0', '0', '2017-08-25 10:59:44');
INSERT INTO `appx` VALUES ('9', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-oppo', '', '0', '0', '0', '2017-08-25 10:47:44');
INSERT INTO `appx` VALUES ('10', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', '', '0', '0', '0', '2017-08-04 17:36:20');
INSERT INTO `appx` VALUES ('11', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-chuizi', '', '0', '0', '0', '2017-08-04 17:36:32');
INSERT INTO `appx` VALUES ('12', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-sougou', '', '0', '0', '0', '2017-08-04 17:36:46');
INSERT INTO `appx` VALUES ('13', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-lianxiang', '', '0', '0', '0', '2017-08-04 17:37:00');
INSERT INTO `appx` VALUES ('14', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-meizu', '', '0', '0', '0', '2017-08-25 10:55:37');
INSERT INTO `appx` VALUES ('15', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-360', '', '0', '0', '0', '2017-08-25 10:55:37');
INSERT INTO `appx` VALUES ('16', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'iOS-Pro', null, '0', '0', '0', '2017-08-04 17:38:15');
INSERT INTO `appx` VALUES ('17', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'iOS-shangwu', null, '0', '0', '0', '2017-08-04 17:38:26');
INSERT INTO `appx` VALUES ('18', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'iOS-jisu', null, '0', '0', '0', '2017-08-04 17:38:40');
INSERT INTO `appx` VALUES ('19', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-86wifi', null, '0', '0', '0', '2017-08-04 17:38:57');
INSERT INTO `appx` VALUES ('20', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-dns1', null, '0', '0', '0', '2017-08-04 17:39:09');
INSERT INTO `appx` VALUES ('21', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-tuia', null, '0', '0', '0', '2017-08-04 17:39:23');
INSERT INTO `appx` VALUES ('22', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-duoduomao', null, '1', '0', '0', '2017-08-04 17:39:39');
INSERT INTO `appx` VALUES ('23', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-25 10:59:45');
INSERT INTO `appx` VALUES ('24', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:40:06');
INSERT INTO `appx` VALUES ('25', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-zhubo1', null, '0', '0', '0', '2017-08-04 17:40:17');
INSERT INTO `appx` VALUES ('26', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-group1', null, '0', '0', '0', '2017-08-04 17:40:31');
INSERT INTO `appx` VALUES ('27', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-zhubo2', null, '0', '0', '0', '2017-08-04 17:40:44');
INSERT INTO `appx` VALUES ('28', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-zhubo3', null, '0', '0', '0', '2017-08-04 17:41:00');
INSERT INTO `appx` VALUES ('29', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-zhubo4', null, '0', '0', '0', '2017-08-04 17:41:13');
INSERT INTO `appx` VALUES ('30', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-wap1', null, '0', '0', '0', '2017-08-04 17:41:26');
INSERT INTO `appx` VALUES ('31', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-ditui1', null, '0', '0', '0', '2017-08-04 17:42:07');
INSERT INTO `appx` VALUES ('32', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-ditui2', null, '0', '0', '0', '2017-08-04 17:42:18');
INSERT INTO `appx` VALUES ('33', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-ditui3', null, '0', '0', '0', '2017-08-04 17:42:29');
INSERT INTO `appx` VALUES ('34', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-360-1', null, '0', '0', '0', '2017-08-04 17:42:47');
INSERT INTO `appx` VALUES ('35', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-qqopen-1', null, '0', '0', '0', '2017-08-25 10:47:45');
INSERT INTO `appx` VALUES ('36', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-weixin1', null, '0', '0', '0', '2017-08-04 17:43:08');
INSERT INTO `appx` VALUES ('37', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'Android-wap2', null, '0', '0', '0', '2017-08-04 17:43:24');
INSERT INTO `appx` VALUES ('38', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-25 10:59:45');
INSERT INTO `appx` VALUES ('39', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:44:05');
INSERT INTO `appx` VALUES ('40', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:44:19');
INSERT INTO `appx` VALUES ('41', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:44:35');
INSERT INTO `appx` VALUES ('42', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'iOS-ddd', null, '0', '0', '0', '2017-08-04 17:44:50');
INSERT INTO `appx` VALUES ('43', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:45:03');
INSERT INTO `appx` VALUES ('44', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:45:15');
INSERT INTO `appx` VALUES ('45', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'bbb', null, '0', '0', '0', '2017-08-04 17:45:27');
INSERT INTO `appx` VALUES ('46', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:45:39');
INSERT INTO `appx` VALUES ('47', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:47:56');
INSERT INTO `appx` VALUES ('48', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd - 发现', '', '0', '0', '0', '2017-08-04 17:48:41');
INSERT INTO `appx` VALUES ('49', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd - 贷款', null, '0', '0', '0', '2017-08-25 10:59:45');
INSERT INTO `appx` VALUES ('50', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'bbb', null, '0', '0', '0', '2017-08-25 10:49:49');
INSERT INTO `appx` VALUES ('51', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:49:22');
INSERT INTO `appx` VALUES ('52', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd-1', null, '0', '0', '0', '2017-08-25 10:59:45');
INSERT INTO `appx` VALUES ('53', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-04 17:49:54');
INSERT INTO `appx` VALUES ('54', 'aaaaaaaaa', 'zzzzzzzzzzzzzzzzzz', '1', '1', 'ddd', null, '0', '0', '0', '2017-08-25 10:59:45');



CREATE TABLE `appx_agroup` (
  `agroup_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL ,
  `tag` varchar(40) DEFAULT '' ,
  `ugroup_id` int NOT NULL DEFAULT '0' ,
  `is_disabled` int NOT NULL DEFAULT '0' ,
  `enable_track` int NOT NULL DEFAULT '1' ,
  `enable_setting` int NOT NULL DEFAULT '1',
  `log_fulltime` datetime DEFAULT NULL,
  PRIMARY KEY (`agroup_id`)
) ;

INSERT INTO `appx_agroup` VALUES ('1', 'a', 'hold', '1', '0', '1', '1', '2018-07-24 11:06:31');
INSERT INTO `appx_agroup` VALUES ('2', 'b', '', '2', '1', '1', '1', '2020-06-18 09:46:13');
INSERT INTO `appx_agroup` VALUES ('3', 'c', 'hope', '3', '0', '1', '1', '2020-06-09 12:40:30');
INSERT INTO `appx_agroup` VALUES ('4', 'd', 'beauty', '4', '0', '1', '1', '2018-07-24 12:02:46');
INSERT INTO `appx_agroup` VALUES ('5', 'e', 'beast', '5', '0', '1', '1', '2018-07-24 12:02:21');
INSERT INTO `appx_agroup` VALUES ('6', 'f', 'dobbin', '6', '0', '1', '1', '2019-01-04 14:40:27');
INSERT INTO `appx_agroup` VALUES ('8', 'g', '', '8', '0', '1', '1', '2018-06-01 18:26:48');
INSERT INTO `appx_agroup` VALUES ('9', 'h', 'zebra', '1', '0', '1', '1', '2018-09-04 10:39:05');
INSERT INTO `appx_agroup` VALUES ('10', 'a1', 'angel', '10', '0', '1', '1', '2018-08-28 10:28:28');
INSERT INTO `appx_agroup` VALUES ('11', 'b1', 'steak', '10016', '0', '1', '1', '2018-09-03 11:02:26');
INSERT INTO `appx_agroup` VALUES ('12', 'c1', 'bigfish', '10031', '0', '1', '1', '2018-09-04 10:09:18');
INSERT INTO `appx_agroup` VALUES ('13', 'd1', '', '1', '0', '1', '1', null);
INSERT INTO `appx_agroup` VALUES ('14', 'e1', '', '10010', '0', '1', '1', null);
INSERT INTO `appx_agroup` VALUES ('15', 'f1', '', '10015', '0', '1', '1', '2018-05-18 16:47:04');
INSERT INTO `appx_agroup` VALUES ('16', 'g1', '', '10013', '0', '1', '1', null);
INSERT INTO `appx_agroup` VALUES ('17', 'g1', 'test1', '10015', '0', '1', '1', '2019-01-16 16:46:45');
INSERT INTO `appx_agroup` VALUES ('19', 'h1', '', '10017', '0', '1', '1', '2018-06-04 10:22:04');
