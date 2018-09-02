/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : syn

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-09-02 23:46:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activation_code
-- ----------------------------
DROP TABLE IF EXISTS `activation_code`;
CREATE TABLE `activation_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of activation_code
-- ----------------------------
INSERT INTO `activation_code` VALUES ('1', 'OK');

-- ----------------------------
-- Table structure for auto_syn_log
-- ----------------------------
DROP TABLE IF EXISTS `auto_syn_log`;
CREATE TABLE `auto_syn_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auto_syn_log
-- ----------------------------
INSERT INTO `auto_syn_log` VALUES ('1', '2018-09-02 22:31:46');
INSERT INTO `auto_syn_log` VALUES ('3', '2018-09-02 22:29:46');

-- ----------------------------
-- Table structure for auto_syn_result
-- ----------------------------
DROP TABLE IF EXISTS `auto_syn_result`;
CREATE TABLE `auto_syn_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `is_more` bit(1) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `rule_key` varchar(255) NOT NULL,
  `rule_name` varchar(255) NOT NULL,
  `severity` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7sxb4mg7j1yi48wvhe6byt0h7` (`create_date`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auto_syn_result
-- ----------------------------
INSERT INTO `auto_syn_result` VALUES ('1', '2018-09-02 22:29:46', '', 'Java', 'squid:CallToDeprecatedMethod', '\"@Deprecated\" code should not be used', 'INFO');
INSERT INTO `auto_syn_result` VALUES ('2', '2018-09-02 22:31:46', '', 'Java', 'squid:S2637', '\"@NonNull\" values should not be set to null', 'BLOCKER');
INSERT INTO `auto_syn_result` VALUES ('3', '2018-09-02 22:31:46', '', 'Java', 'squid:S2737', '\"catch\" clauses should do more than rethrow', 'MINOR');
INSERT INTO `auto_syn_result` VALUES ('4', '2018-09-02 22:31:46', '', 'Java', 'checkstyle:com.puppycrawl.tools.checkstyle.checks.sizes.AnonInnerLengthCheck', 'Anon Inner Length', 'BLOCKER');
INSERT INTO `auto_syn_result` VALUES ('5', '2018-09-02 22:33:13', '', 'Java', 'squid:S2203', '\"collect\" should be used with \"Streams\" instead of \"list::add\"', 'MINOR');
INSERT INTO `auto_syn_result` VALUES ('6', '2018-09-02 22:33:13', '', 'Java', 'pmd:AbstractClassWithoutAnyMethod', 'Abstract class without any methods', 'MAJOR');
INSERT INTO `auto_syn_result` VALUES ('7', '2018-09-02 22:35:56', '\0', 'Java', 'pmd:AbstractClassWithoutAnyMethod', 'Abstract class without any methods', 'MAJOR');
INSERT INTO `auto_syn_result` VALUES ('8', '2018-09-02 22:54:29', '', 'Java', 'checkstyle:com.puppycrawl.tools.checkstyle.checks.ArrayTypeStyleCheck', 'Array Type Style', 'MINOR');

-- ----------------------------
-- Table structure for base_profile
-- ----------------------------
DROP TABLE IF EXISTS `base_profile`;
CREATE TABLE `base_profile` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `profiles` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of base_profile
-- ----------------------------
INSERT INTO `base_profile` VALUES ('1', 'Java');
INSERT INTO `base_profile` VALUES ('5', 'CSS');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '2018-08-25 02:37:39', 'ok', 'test');
INSERT INTO `user` VALUES ('2', '2018-08-25 22:40:10', 'ok', 'test');
INSERT INTO `user` VALUES ('3', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('4', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('5', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('6', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('7', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('8', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('9', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('10', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('11', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('12', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('13', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('14', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('15', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('16', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('17', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('18', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('19', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('20', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('21', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('22', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('23', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('24', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('25', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('26', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('27', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('28', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('29', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('30', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('31', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('32', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('33', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('34', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('35', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('36', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('37', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('38', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('39', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('40', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('41', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('42', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('43', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('44', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('45', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('46', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('47', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('48', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('49', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('50', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('51', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('52', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('53', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('54', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('55', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('56', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('57', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('58', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('59', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('60', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('61', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('62', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('63', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('64', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('65', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('66', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('67', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('68', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('69', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('70', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('71', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('72', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('73', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('74', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('75', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('76', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('77', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('78', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('79', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('80', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('81', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('82', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('83', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('84', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('85', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('86', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('87', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('88', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('89', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('90', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('91', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('92', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('93', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('94', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('95', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('96', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('97', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('98', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('99', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('100', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('101', '2018-08-25 22:40:46', 'ok', 'test');
INSERT INTO `user` VALUES ('102', '2018-08-25 22:40:46', 'ok', 'test');