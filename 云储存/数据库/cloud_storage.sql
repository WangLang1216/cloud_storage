/*
 Navicat Premium Data Transfer

 Source Server         : win_mysql
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : cloud_storage

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 09/10/2023 13:01:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for catalog
-- ----------------------------
DROP TABLE IF EXISTS `catalog`;
CREATE TABLE `catalog`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `catalog_id` bigint NOT NULL DEFAULT 0,
  `catalog_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '目录名称',
  `catalog_pid` bigint NULL DEFAULT NULL COMMENT '父目录id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `catalog_level` int NULL DEFAULT NULL COMMENT '目录层级，0为根目录',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '目录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of catalog
-- ----------------------------
INSERT INTO `catalog` VALUES (1, 0, '全部', NULL, NULL, NULL, '2023-08-17 09:57:17');
INSERT INTO `catalog` VALUES (2, 0, '全部', NULL, 11, NULL, '2023-08-17 10:04:15');
INSERT INTO `catalog` VALUES (3, 0, '全部', NULL, 12, NULL, '2023-08-17 10:06:51');
INSERT INTO `catalog` VALUES (4, 0, '全部', NULL, 13, NULL, '2023-08-17 10:08:37');
INSERT INTO `catalog` VALUES (5, 0, '全部', NULL, 14, NULL, '2023-08-17 10:11:10');
INSERT INTO `catalog` VALUES (6, 0, '全部', NULL, 104, NULL, '2023-08-17 10:13:20');
INSERT INTO `catalog` VALUES (7, 0, '全部', NULL, 105, NULL, '2023-08-18 07:17:55');
INSERT INTO `catalog` VALUES (15, 1692446648636026881, '321', 0, 104, NULL, '2023-08-18 16:01:40');
INSERT INTO `catalog` VALUES (20, 1692451992389099521, '123', 0, 104, NULL, '2023-08-18 16:22:54');
INSERT INTO `catalog` VALUES (31, 1692454915139510274, '123', 1692451992389099521, 104, NULL, '2023-08-18 16:34:31');
INSERT INTO `catalog` VALUES (32, 0, '全部', NULL, 106, NULL, '2023-08-18 17:13:30');
INSERT INTO `catalog` VALUES (33, 1692464831120105473, '123', 0, 106, NULL, '2023-08-18 17:13:55');
INSERT INTO `catalog` VALUES (34, 1692465224801673218, '1234', 0, 106, NULL, '2023-08-18 17:15:29');
INSERT INTO `catalog` VALUES (35, 0, '全部', NULL, 107, NULL, '2023-08-21 17:09:09');
INSERT INTO `catalog` VALUES (39, 1693552290574639105, '123', 0, 100, NULL, '2023-08-21 17:15:05');
INSERT INTO `catalog` VALUES (40, 0, '全部', NULL, 108, NULL, '2023-08-30 11:30:28');
INSERT INTO `catalog` VALUES (41, 1696764367644672002, '测试', 0, 108, NULL, '2023-08-30 13:58:44');
INSERT INTO `catalog` VALUES (42, 1696764413370974209, '再测试', 1696764367644672002, 108, NULL, '2023-08-30 13:58:55');
INSERT INTO `catalog` VALUES (45, 0, '全部', NULL, 109, NULL, '2023-09-06 14:36:53');
INSERT INTO `catalog` VALUES (46, 0, '全部', NULL, 110, NULL, '2023-09-06 14:37:59');
INSERT INTO `catalog` VALUES (47, 0, '全部', NULL, 111, NULL, '2023-09-06 15:20:58');

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_id` bigint NOT NULL DEFAULT 0,
  `catalog_id` bigint NOT NULL COMMENT '目录id',
  `user_id` bigint NULL DEFAULT NULL,
  `file_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件名',
  `file_size` decimal(10, 2) NULL DEFAULT NULL COMMENT '文件大小',
  `file_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '大小单位',
  `file_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_source` int NULL DEFAULT 0 COMMENT '文件来源，0代表自定义，默认0',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `version` int NOT NULL DEFAULT 1 COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '文件信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file
-- ----------------------------
INSERT INTO `file` VALUES (8, 1692002810167570433, 0, 104, 'eclipse.exe', NULL, NULL, NULL, 0, '2023-08-17 10:38:00', 1);
INSERT INTO `file` VALUES (9, 1692004172561395713, 0, 104, 'index_icon_HL.png', NULL, NULL, NULL, 0, '2023-08-17 10:43:25', 1);
INSERT INTO `file` VALUES (10, 1692004172829831170, 0, 104, 'index_icon.png', NULL, NULL, NULL, 0, '2023-08-17 10:43:25', 1);
INSERT INTO `file` VALUES (11, 1692424608822693889, 0, 100, '12112.txt', NULL, NULL, NULL, 104, '2023-08-18 14:34:05', 1);
INSERT INTO `file` VALUES (12, 1692424608856248322, 0, 100, 'jpg.jpg', NULL, NULL, NULL, 104, '2023-08-18 14:34:05', 1);
INSERT INTO `file` VALUES (14, 1692464775872733186, 0, 106, '忘记时间-《仙剑奇侠传》电视剧插曲.mp3', NULL, NULL, NULL, 0, '2023-08-18 17:13:42', 1);
INSERT INTO `file` VALUES (15, 1692466077868580866, 0, 106, '生命.mp3', NULL, NULL, NULL, 0, '2023-08-18 17:18:52', 1);
INSERT INTO `file` VALUES (16, 1692466096801669122, 0, 106, '偏爱.mp3', NULL, NULL, NULL, 0, '2023-08-18 17:18:57', 1);
INSERT INTO `file` VALUES (19, 1693518644383842306, 0, 106, 'a0028faee285457740544b1e2c2fbdbb.jpg', NULL, NULL, NULL, 0, '2023-08-21 15:01:24', 1);
INSERT INTO `file` VALUES (21, 1693550949173317633, 0, 107, 'QQ图片20200509123552.jpg', NULL, NULL, NULL, 0, '2023-08-21 17:09:46', 1);
INSERT INTO `file` VALUES (24, 1693552044448686081, 0, 100, '忘记时间-《仙剑奇侠传》电视剧插曲.mp3', NULL, NULL, NULL, 104, '2023-08-21 17:14:07', 1);
INSERT INTO `file` VALUES (25, 1693552044486434817, 0, 100, '生命.mp3', NULL, NULL, NULL, 104, '2023-08-21 17:14:07', 1);
INSERT INTO `file` VALUES (30, 1696764475551531009, 1696764367644672002, 108, '1636516701323.jpg', NULL, NULL, NULL, 0, '2023-08-30 13:59:10', 1);
INSERT INTO `file` VALUES (31, 1696764538713554946, 0, 107, '1636516701323.jpg', NULL, NULL, NULL, 108, '2023-08-30 13:59:25', 1);
INSERT INTO `file` VALUES (35, 1697431482005467138, 0, 108, '20211119_18222694.png.jpg', NULL, NULL, NULL, 0, '2023-09-01 10:09:37', 1);
INSERT INTO `file` VALUES (36, 1697431482349400066, 0, 108, '20210605_22425354.png', NULL, NULL, NULL, 0, '2023-09-01 10:09:37', 1);
INSERT INTO `file` VALUES (37, 1697431483389587457, 0, 108, '232029-16098600297597.jpg', NULL, NULL, NULL, 0, '2023-09-01 10:09:37', 1);
INSERT INTO `file` VALUES (39, 1698585796036067330, 0, 108, '云服务器-技术方案（修订版）-黄孝武评审.docx', NULL, NULL, NULL, 0, '2023-09-04 14:36:27', 1);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `m_id` bigint NOT NULL AUTO_INCREMENT,
  `m_name` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '菜单名称',
  `m_desc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单描述',
  `m_level` int NULL DEFAULT NULL COMMENT '菜单级别',
  `m_pid` bigint NULL DEFAULT NULL COMMENT '父id，0为根级',
  `m_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `m_state` int NULL DEFAULT 0 COMMENT '菜单状态，0正常，1失效',
  PRIMARY KEY (`m_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '全部', '查看全部文件信息', 1, NULL, '0', 0);
INSERT INTO `menu` VALUES (2, '文档', '查看所有文档信息', 1, NULL, '0', 0);
INSERT INTO `menu` VALUES (3, '图片', '查看所有图片信息', 1, NULL, '0', 0);
INSERT INTO `menu` VALUES (4, '视频', '查看所有视频信息', 1, NULL, '0', 0);
INSERT INTO `menu` VALUES (5, '音乐', '查看所有音乐信息', 1, NULL, '0', 0);
INSERT INTO `menu` VALUES (6, '我的分享', '查看分享给我的文件信息', 1, NULL, '0', 0);
INSERT INTO `menu` VALUES (7, '用户管理', '管理员管理用户信息', 1, NULL, '0', 0);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `p_id` bigint NOT NULL AUTO_INCREMENT,
  `p_name` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限名称',
  `p_desc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  PRIMARY KEY (`p_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '管理员', '赋予全部权限');
INSERT INTO `permission` VALUES (2, '普通用户', '仅限于文件操作');

-- ----------------------------
-- Table structure for permission_menu
-- ----------------------------
DROP TABLE IF EXISTS `permission_menu`;
CREATE TABLE `permission_menu`  (
  `pm_id` bigint NOT NULL AUTO_INCREMENT,
  `p_id` bigint NOT NULL COMMENT '权限id',
  `m_id` bigint NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`pm_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '权限菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission_menu
-- ----------------------------
INSERT INTO `permission_menu` VALUES (1, 1, 1);
INSERT INTO `permission_menu` VALUES (2, 1, 2);
INSERT INTO `permission_menu` VALUES (3, 1, 3);
INSERT INTO `permission_menu` VALUES (4, 1, 4);
INSERT INTO `permission_menu` VALUES (5, 1, 5);
INSERT INTO `permission_menu` VALUES (6, 1, 6);
INSERT INTO `permission_menu` VALUES (7, 1, 7);
INSERT INTO `permission_menu` VALUES (8, 2, 1);
INSERT INTO `permission_menu` VALUES (9, 2, 2);
INSERT INTO `permission_menu` VALUES (10, 2, 3);
INSERT INTO `permission_menu` VALUES (11, 2, 4);
INSERT INTO `permission_menu` VALUES (12, 2, 5);
INSERT INTO `permission_menu` VALUES (13, 2, 6);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `r_id` bigint NOT NULL AUTO_INCREMENT,
  `r_name` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `r_desc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员', '管理员角色');
INSERT INTO `role` VALUES (2, '普通用户', '普通用户角色');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `rp_id` bigint NOT NULL AUTO_INCREMENT,
  `r_id` bigint NOT NULL COMMENT '角色id',
  `p_id` bigint NOT NULL COMMENT '权限id',
  PRIMARY KEY (`rp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1, 1);
INSERT INTO `role_permission` VALUES (2, 2, 2);

-- ----------------------------
-- Table structure for sonlog
-- ----------------------------
DROP TABLE IF EXISTS `sonlog`;
CREATE TABLE `sonlog`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sonlog_id` bigint NOT NULL DEFAULT 0,
  `catalog_id` bigint NOT NULL DEFAULT 0,
  `user_id` bigint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 40 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户目录表' ROW_FORMAT = Fixed;

-- ----------------------------
-- Records of sonlog
-- ----------------------------
INSERT INTO `sonlog` VALUES (18, 1692451992389099521, 0, 104);
INSERT INTO `sonlog` VALUES (7, 1692443938159341569, 1692442401005645826, 104);
INSERT INTO `sonlog` VALUES (29, 1692454915139510274, 1692451992389099521, 104);
INSERT INTO `sonlog` VALUES (30, 1692464831120105473, 0, 106);
INSERT INTO `sonlog` VALUES (31, 1692465224801673218, 0, 106);
INSERT INTO `sonlog` VALUES (35, 1693552290574639105, 0, 100);
INSERT INTO `sonlog` VALUES (33, 1693551048494436353, 1693551001774084097, 107);
INSERT INTO `sonlog` VALUES (34, 1693551096963813378, 1693551001774084097, 107);
INSERT INTO `sonlog` VALUES (36, 1696764367644672002, 0, 108);
INSERT INTO `sonlog` VALUES (37, 1696764413370974209, 1696764367644672002, 108);
INSERT INTO `sonlog` VALUES (39, 1696792062403293185, 1696792046926311425, 108);

-- ----------------------------
-- Table structure for user_directory
-- ----------------------------
DROP TABLE IF EXISTS `user_directory`;
CREATE TABLE `user_directory`  (
  `ud_id` bigint NOT NULL AUTO_INCREMENT,
  `u_id` bigint NOT NULL COMMENT '用户id',
  `d_id` bigint NOT NULL COMMENT '目录id',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`ud_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户目录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_directory
-- ----------------------------

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `ur_id` bigint NOT NULL AUTO_INCREMENT,
  `u_id` bigint NOT NULL COMMENT '用户id',
  `r_id` bigint NOT NULL DEFAULT 2 COMMENT '角色id',
  PRIMARY KEY (`ur_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 2, 1);
INSERT INTO `user_role` VALUES (3, 3, 2);
INSERT INTO `user_role` VALUES (4, 4, 1);
INSERT INTO `user_role` VALUES (5, 5, 1);
INSERT INTO `user_role` VALUES (6, 6, 2);
INSERT INTO `user_role` VALUES (7, 7, 2);
INSERT INTO `user_role` VALUES (8, 8, 2);
INSERT INTO `user_role` VALUES (9, 9, 2);
INSERT INTO `user_role` VALUES (10, 10, 2);
INSERT INTO `user_role` VALUES (11, 11, 2);
INSERT INTO `user_role` VALUES (12, 12, 2);
INSERT INTO `user_role` VALUES (13, 13, 2);
INSERT INTO `user_role` VALUES (14, 14, 2);
INSERT INTO `user_role` VALUES (15, 104, 1);
INSERT INTO `user_role` VALUES (16, 105, 2);
INSERT INTO `user_role` VALUES (17, 106, 1);
INSERT INTO `user_role` VALUES (18, 100, 1);
INSERT INTO `user_role` VALUES (19, 107, 2);
INSERT INTO `user_role` VALUES (20, 108, 1);
INSERT INTO `user_role` VALUES (21, 109, 1);
INSERT INTO `user_role` VALUES (22, 110, 1);
INSERT INTO `user_role` VALUES (23, 111, 1);

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo`  (
  `u_id` bigint NOT NULL AUTO_INCREMENT,
  `u_name` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `u_password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `u_state` int NULL DEFAULT 0 COMMENT '用户状态，0正常，1异常',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES (100, 'wanglang', '123', 1, '2023-08-08 10:37:00', '2023-08-08 10:37:00');
INSERT INTO `userinfo` VALUES (104, '1234', '123', 1, '2023-08-17 18:13:19', '2023-08-17 18:13:19');
INSERT INTO `userinfo` VALUES (105, 'whp', '123', 1, '2023-08-18 15:17:54', '2023-08-18 15:17:54');
INSERT INTO `userinfo` VALUES (106, 'wxy', '123', 0, '2023-08-19 01:13:30', '2023-08-19 01:13:30');
INSERT INTO `userinfo` VALUES (107, 'zxc', '123', 0, '2023-08-22 01:09:08', '2023-08-22 01:09:08');
INSERT INTO `userinfo` VALUES (108, 'abc', '123', 0, '2023-08-30 19:30:27', '2023-08-30 19:30:27');
INSERT INTO `userinfo` VALUES (109, 'qwe', '123', 0, '2023-09-06 22:36:53', '2023-09-06 22:36:53');
INSERT INTO `userinfo` VALUES (110, 'rty', '123', 0, '2023-09-06 22:37:59', '2023-09-06 22:37:59');
INSERT INTO `userinfo` VALUES (111, 'qaz', '$2a$06$XuvSlHBQ66Ot3qQ6hALXKuAOcx/LvNAu0kUBcOTdDRMFheeBp98Be', 0, '2023-09-06 23:20:57', '2023-09-06 23:20:57');

SET FOREIGN_KEY_CHECKS = 1;
