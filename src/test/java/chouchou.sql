/*
 Navicat Premium Data Transfer

 Source Server         : 本地环境
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : chouchou

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 02/11/2020 14:00:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for atten_model
-- ----------------------------
DROP TABLE IF EXISTS `atten_model`;
CREATE TABLE `atten_model`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '开发主键',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '考勤模板名称',
  `month` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '月份',
  `rest` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '休息日，多个用逗号隔开',
  `vacation` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '假期，JSON格式数据',
  `work` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '补假工作日，JSON格式数据',
  `status` int(4) NULL DEFAULT NULL COMMENT '是否生成考勤记录：0-未生成，1-已生成',
  `generate_time` datetime(0) NULL DEFAULT NULL COMMENT '生成时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '考勤模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of atten_model
-- ----------------------------
INSERT INTO `atten_model` VALUES ('1321019418066038786', '2020-10考勤模板', '2020-10', '6,7', '[{\"begin\":\"2020-10-01\",\"end\":\"2020-10-08\"}]', '[{\"begin\":\"2020-10-10\",\"end\":\"2020-10-10\"}]', 0, NULL, '2020-10-27 17:22:27', '1278555180714426370', NULL, NULL);

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '开发主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司CODE',
  `rise` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司抬头',
  `duty_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司税号',
  `bank_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行账号',
  `bank_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开户银行',
  `address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司地址',
  `touch` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '联系方式，JSON格式，key：name，mobile',
  `status` int(4) NULL DEFAULT NULL COMMENT '公司状态：0-禁用，1-正常',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_unique`(`code`, `duty_no`, `bank_no`) USING BTREE COMMENT '唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公司信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES ('1278251575604174850', '杭州炎鼎家居设计有限公司', 'YDRZ', '杭州炎鼎家居设计有限公司', '91330108MA2H299A06', '1202022709800081019', '中国工商银行杭州市保俶支行', '杭州市滨江区长河街道长河路1318号中易新能源1幢14楼1403室', '[{\"name\":\"席总\",\"mobile\":\"15824180077\"},{\"name\":\"王总\",\"mobile\":\"13606801798\"}]', 1, '1278555180714426370', '2020-07-01 16:58:19', '1278555180714426370', '2020-10-14 17:54:07');

-- ----------------------------
-- Table structure for contract_model
-- ----------------------------
DROP TABLE IF EXISTS `contract_model`;
CREATE TABLE `contract_model`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同模板名称',
  `company_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司ID',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同编码',
  `sort` int(8) NULL DEFAULT NULL COMMENT '排序号',
  `file_id` varchar(23) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同文件ID',
  `file_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同文件名称',
  `file_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同文件地址',
  `status` int(2) NULL DEFAULT NULL COMMENT '合同状态：0-禁用，1-正常',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '合同模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contract_model
-- ----------------------------
INSERT INTO `contract_model` VALUES ('1308691638792052737', '炎鼎家居全案软装设计定金协议', '1278251575604174850', 'DJ', 1, '1308691630558633985', '01.《炎鼎家居全案软装设计定金协议》.doc', 'http://127.0.0.1:9000/chou/5f6b0dad3a1ae4f270a70f3c.doc', 1, '1278555180714426370', '2020-09-23 16:56:16', '1278555180714426370', '2020-09-24 16:50:05');

-- ----------------------------
-- Table structure for contract_receive
-- ----------------------------
DROP TABLE IF EXISTS `contract_receive`;
CREATE TABLE `contract_receive`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID，唯一标识',
  `treaty_no_a` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '协议编号甲',
  `treaty_no_b` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '协议编号乙',
  `contract_no` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同编号',
  `receive_time` date NULL DEFAULT NULL COMMENT '领用日期',
  `model_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同模板ID',
  `treaty_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '协议模板ID',
  `type_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同类型ID',
  `dept_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '领用部门ID',
  `post_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '领用岗位ID',
  `emp_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '领用人ID',
  `status` int(4) NULL DEFAULT NULL COMMENT '状态：0-作废，1-归档，2-领用，3-签订',
  `remarks` varchar(224) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '合同领用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contract_receive
-- ----------------------------
INSERT INTO `contract_receive` VALUES ('1309428964950323202', 'SJ-202001001', 'SJ-202001002', NULL, '2020-09-16', '1308691638792052737', '1302870041527115778', '1308703621029335041', '1285898303719415810', '1278260848728023041', '1283268371268767746', 2, '', '1278555180714426370', '2020-09-25 17:46:08', NULL, NULL);

-- ----------------------------
-- Table structure for contract_type
-- ----------------------------
DROP TABLE IF EXISTS `contract_type`;
CREATE TABLE `contract_type`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `sort` int(4) NULL DEFAULT NULL COMMENT '排序',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态：0-禁用，1-正常',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '合同类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contract_type
-- ----------------------------
INSERT INTO `contract_type` VALUES ('1308703621029335041', '定金协议', 1, 1, '1278555180714426370', '2020-09-23 17:43:52', NULL, NULL);
INSERT INTO `contract_type` VALUES ('1308703690407317505', '租赁合同', 2, 1, '1278555180714426370', '2020-09-23 17:44:09', '1278555180714426370', '2020-09-24 17:52:56');
INSERT INTO `contract_type` VALUES ('1308703767939026946', '设计协议', 3, 1, '1278555180714426370', '2020-09-23 17:44:27', NULL, NULL);

-- ----------------------------
-- Table structure for file_detail
-- ----------------------------
DROP TABLE IF EXISTS `file_detail`;
CREATE TABLE `file_detail`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键，唯一标识',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件原始中文名称',
  `bucket` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '存储桶名称',
  `file_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件地址',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `file_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件类型',
  `status` int(2) NULL DEFAULT NULL COMMENT '文件状态：0-临时文件，1-永久文件',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上传人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件上传明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_detail
-- ----------------------------
INSERT INTO `file_detail` VALUES ('1308691630558633985', '01.《炎鼎家居全案软装设计定金协议》.doc', 'chou', '5f6b0dad3a1ae4f270a70f3c.doc', 'http://127.0.0.1:9000/chou/5f6b0dad3a1ae4f270a70f3c.doc', 26670, 'doc', 1, '1278555180714426370', '2020-09-23 16:56:14');

-- ----------------------------
-- Table structure for order_basic
-- ----------------------------
DROP TABLE IF EXISTS `order_basic`;
CREATE TABLE `order_basic`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键，唯一标识',
  `talk_no` int(11) NULL DEFAULT NULL COMMENT '序号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `sex` int(4) NULL DEFAULT NULL COMMENT '性别：0-女，1-男',
  `mobile` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `wechat` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信',
  `talk_time` date NULL DEFAULT NULL COMMENT '派单日期',
  `customer_state` int(4) NULL DEFAULT NULL COMMENT '客户情况：1-产值可签单，2-产值已出，3-定金可转换，4-跟进中，5-无效，6-已退单，7-已完结，8-意向可签单',
  `treaty_type` int(4) NULL DEFAULT NULL COMMENT '协议类型：1-产品代购，2-产品代购/合作，3-产品合作，4-定金协议，5-设计协议',
  `treaty_detail` int(4) NULL DEFAULT NULL COMMENT '协议详情：1-可退，2-不可退，3-纯设计，4-全案设计，5-返点，6-全款，7-全款/返点',
  `design_time` date NULL DEFAULT NULL COMMENT '设计签订日期',
  `output_time` date NULL DEFAULT NULL COMMENT '产值签订日期',
  `hardcover` int(4) NULL DEFAULT NULL COMMENT '是否精装：0-否，1-是',
  `sale_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '楼盘名称',
  `sale_no` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '房号',
  `sale_area` decimal(10, 0) NULL DEFAULT NULL COMMENT '面积',
  `resident_num` int(4) NULL DEFAULT NULL COMMENT '常驻人口',
  `purpose` varchar(224) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '意向风格',
  `sale_price_min` decimal(10, 2) NULL DEFAULT NULL COMMENT '费用预算最小',
  `sale_price_max` decimal(10, 2) NULL DEFAULT NULL COMMENT '费用预算最大',
  `special_need` varchar(224) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '特殊需求',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '洽谈地点',
  `channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '渠道',
  `recommend` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '推荐人',
  `market_dept` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营销中心部门ID',
  `market_emp` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营销中心人员ID',
  `market_belong` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营销负责人ID',
  `design_dept` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设计中心部门ID',
  `design_emp` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设计中心人员ID，多个用逗号分隔',
  `design_belong` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设计负责人ID',
  `manager` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '客户经理，多个用逗号分隔',
  `remarks` varchar(224) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int(4) NULL DEFAULT NULL COMMENT '是否生成二见：0-否，1-是',
  `create_talk_time` datetime(0) NULL DEFAULT NULL COMMENT '生成洽谈时间',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '客户洽谈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_basic
-- ----------------------------
INSERT INTO `order_basic` VALUES ('1290951186856865794', 1, '王茂盛', 1, '18968183999', '', '2020-03-08', 1, 5, 4, '2020-03-12', NULL, 1, '首开金茂府', '11-1-3-1', 230, NULL, '', NULL, NULL, NULL, '房子现场', '邀约', '1309386030456610818', '1290925674356858882', '1290932591758204929', '1290932591758204929', '1278255029512876034', '1290950816696954881,1283248274567397378', '1290950816696954881', '1283248274567397378,1290950816696954881', '', 1, '2020-08-13 16:54:01', '1278555180714426370', '2020-08-05 18:02:02', '1278555180714426370', '2020-09-25 14:56:41');
INSERT INTO `order_basic` VALUES ('1298945914470727681', 2, '张三', 0, '', '', '2020-08-12', 3, NULL, NULL, NULL, NULL, 0, '', '', NULL, NULL, '', NULL, NULL, NULL, '', '', '', '', '', '', '', '', '', '', '', 0, NULL, '1278555180714426370', '2020-08-27 19:30:14', '1278555180714426370', '2020-09-16 11:21:43');

-- ----------------------------
-- Table structure for outsider
-- ----------------------------
DROP TABLE IF EXISTS `outsider`;
CREATE TABLE `outsider`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键，唯一标识',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `mobile` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `status` int(4) NULL DEFAULT NULL COMMENT '状态：0-禁用，1-正常',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '外部人员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of outsider
-- ----------------------------
INSERT INTO `outsider` VALUES ('1286160317712740353', '张三', '15020021001', 1, '1278555180714426370', '2020-07-23 12:44:50', '1278555180714426370', '2020-09-24 14:55:29');
INSERT INTO `outsider` VALUES ('1286160589067395074', '李四', '18011112222', 1, '1278555180714426370', '2020-07-23 12:45:55', '1278555180714426370', '2020-09-23 17:37:53');
INSERT INTO `outsider` VALUES ('1286160668666896386', '王五', '19010012002', 1, '1278555180714426370', '2020-07-23 12:46:14', '1278555180714426370', '2020-07-29 16:56:24');
INSERT INTO `outsider` VALUES ('1309385391458590722', '1', '13111112222', 1, '1278555180714426370', '2020-09-25 14:52:59', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385420269264897', '2', '13122221111', 1, '1278555180714426370', '2020-09-25 14:53:06', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385450673774593', '3', '13133331111', 0, '1278555180714426370', '2020-09-25 14:53:13', '1278555180714426370', '2020-10-15 12:47:12');
INSERT INTO `outsider` VALUES ('1309385511424073729', '4', '13111222211', 1, '1278555180714426370', '2020-09-25 14:53:28', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385552691830785', '5', '13122111122', 1, '1278555180714426370', '2020-09-25 14:53:38', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385597306642434', '6', '13112341234', 1, '1278555180714426370', '2020-09-25 14:53:48', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385649940963329', '11223344', '13111223344', 1, '1278555180714426370', '2020-09-25 14:54:01', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385694551580674', '8', '15111112222', 1, '1278555180714426370', '2020-09-25 14:54:11', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385738038124545', '14', '15123232323', 1, '1278555180714426370', '2020-09-25 14:54:22', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385775820414978', '11', '13112121212', 1, '1278555180714426370', '2020-09-25 14:54:31', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385857638703106', '1122', '13114141414', 0, '1278555180714426370', '2020-09-25 14:54:50', '1278555180714426370', '2020-10-21 16:23:04');
INSERT INTO `outsider` VALUES ('1309385894963814401', '3434', '13113131313', 1, '1278555180714426370', '2020-09-25 14:54:59', NULL, NULL);
INSERT INTO `outsider` VALUES ('1309385953369497602', '1441', '13112212112', 0, '1278555180714426370', '2020-09-25 14:55:13', '1278555180714426370', '2020-10-15 12:47:00');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '开发主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门名称',
  `sort` int(8) NULL DEFAULT NULL COMMENT '排序字段',
  `parent_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上级部门ID',
  `status` int(4) NULL DEFAULT NULL COMMENT '部门状态：0-禁用，1-正常',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1278253529889767425', '综合部', 2, NULL, 1, '1278555180714426370', '2020-07-01 17:06:05', '1278555180714426370', '2020-07-22 21:28:19');
INSERT INTO `sys_dept` VALUES ('1278253584604463105', '产品部', 3, NULL, 1, '1278555180714426370', '2020-07-01 17:06:18', '1278555180714426370', '2020-07-22 21:28:30');
INSERT INTO `sys_dept` VALUES ('1278254616600379394', '设计部', 4, NULL, 1, '1278555180714426370', '2020-07-01 17:10:24', '1278555180714426370', '2020-07-22 21:28:39');
INSERT INTO `sys_dept` VALUES ('1278255029512876034', '设计一中心', 2, '1278254616600379394', 1, '1278555180714426370', '2020-07-01 17:12:03', NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1278255185813614594', '设计二中心', 3, '1278254616600379394', 1, '1278555180714426370', '2020-07-01 17:12:40', '1278555180714426370', '2020-07-22 21:28:55');
INSERT INTO `sys_dept` VALUES ('1285839545790156801', '采购中心', 8, '1278253584604463105', 1, '1278555180714426370', '2020-07-22 15:30:12', '1278555180714426370', '2020-07-22 19:26:54');
INSERT INTO `sys_dept` VALUES ('1285898303719415810', '人事中心', 0, '1278253529889767425', 1, '1278555180714426370', '2020-07-22 19:23:41', '1278555180714426370', '2020-08-11 10:51:05');
INSERT INTO `sys_dept` VALUES ('1290925613568811009', '营销中心', 5, NULL, 1, '1278555180714426370', '2020-08-05 16:20:25', NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1290925674356858882', '营销一中心', 1, '1290925613568811009', 1, '1278555180714426370', '2020-08-05 16:20:40', NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1290925725632225282', '营销二中心', 2, '1290925613568811009', 1, '1278555180714426370', '2020-08-05 16:20:52', NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1293427140506214402', '总经办', 1, NULL, 1, '1278555180714426370', '2020-08-12 14:00:36', NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1318798534953680898', '行政中心', 1, '1278253529889767425', 0, '1278555180714426370', '2020-10-21 14:17:27', '1278555180714426370', '2020-10-21 14:22:11');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '开发主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '岗位名称',
  `dept_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '所属部门ID',
  `sort` int(8) NULL DEFAULT NULL COMMENT '排序字段',
  `status` int(4) NULL DEFAULT NULL COMMENT '岗位状态：0-禁用，1-正常',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES ('1278260635497996289', 'COO', '1293427140506214402', 2, 1, '1278555180714426370', '2020-07-01 17:34:19', '1278555180714426370', '2020-08-12 14:00:48');
INSERT INTO `sys_post` VALUES ('1278260848728023041', '人事专员', '1285898303719415810', 5, 1, '1278555180714426370', '2020-07-01 17:35:10', '1278555180714426370', '2020-07-22 21:32:13');
INSERT INTO `sys_post` VALUES ('1285847353910677505', '采购员', '1285839545790156801', 100, 1, '1278555180714426370', '2020-07-22 16:01:14', '1278555180714426370', '2020-08-04 13:23:46');
INSERT INTO `sys_post` VALUES ('1285898386624028674', '人事经理', '1285898303719415810', 3, 1, '1278555180714426370', '2020-07-22 19:24:01', '1278555180714426370', '2020-07-22 21:30:12');
INSERT INTO `sys_post` VALUES ('1290931242748403714', '营销专员', '1290925674356858882', 11, 1, '1278555180714426370', '2020-08-05 16:42:47', '1278555180714426370', '2020-08-05 17:52:18');
INSERT INTO `sys_post` VALUES ('1290948815049256961', '软装设计师', '1278255029512876034', 12, 1, '1278555180714426370', '2020-08-05 17:52:37', NULL, NULL);
INSERT INTO `sys_post` VALUES ('1293427692417900546', 'CEO', '1293427140506214402', 1, 1, '1278555180714426370', '2020-08-12 14:02:47', NULL, NULL);

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开发主键',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源编码',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源地址',
  `method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问方式',
  `type` int(4) NULL DEFAULT NULL COMMENT '资源类型：1-菜单，2-按钮，3-功能',
  `sort` int(8) NULL DEFAULT NULL COMMENT '排序',
  `parent_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级资源ID',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源描述',
  `status` int(4) NULL DEFAULT NULL COMMENT '资源状态：0-禁用，1-正常',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES ('1278981598636179458', '系统管理', 'SYS_MANAGE', '', 'GET', 1, 100, NULL, '', 1, '1278555180714426370', '2020-07-03 17:19:10', '1278555180714426370', '2020-07-22 21:22:00');
INSERT INTO `sys_resource` VALUES ('1278982449626906626', '角色管理', 'SYS_ROLE', '/sysRole', 'GET', 1, 2, '1278981598636179458', NULL, 1, '1278555180714426370', '2020-07-03 17:22:33', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1278982692498079746', '资源管理', 'SYS_RESOURCE', '/sysResource', 'GET', 1, 3, '1278981598636179458', '', 1, '1278555180714426370', '2020-07-03 17:23:31', '1278555180714426370', '2020-07-22 21:24:39');
INSERT INTO `sys_resource` VALUES ('1278983556180770817', '资源查看', 'SYS_RESOURCE:LIST', '/sysResource/query', 'GET', 2, 0, '1278982692498079746', '', 1, '1278555180714426370', '2020-07-03 17:26:57', '1278555180714426370', '2020-10-23 15:17:38');
INSERT INTO `sys_resource` VALUES ('1278983758534967297', '资源新增', 'SYS_RESOURCE:ADD', '/sysResource', 'POST', 2, 4, '1278982692498079746', '', 1, '1278555180714426370', '2020-07-03 17:27:45', '1278555180714426370', '2020-07-11 20:55:53');
INSERT INTO `sys_resource` VALUES ('1278983870233477122', '资源修改', 'SYS_RESOURCE:EDIT', '/sysResource', 'PUT', 2, 3, '1278982692498079746', '', 1, '1278555180714426370', '2020-07-03 17:28:12', '1278555180714426370', '2020-07-11 21:00:30');
INSERT INTO `sys_resource` VALUES ('1278983977393750017', '资源删除', 'SYS_RESOURCE:REMOVE', '/sysResource', 'DELETE', 2, 2, '1278982692498079746', '', 1, '1278555180714426370', '2020-07-03 17:28:37', '1278555180714426370', '2020-07-11 21:00:40');
INSERT INTO `sys_resource` VALUES ('1278984265819258882', '资源CODE验证', 'SYS_RESOURCE:CODE', '/sysResource/check/code', 'POST', 3, 1, '1278982692498079746', '', 1, '1278555180714426370', '2020-07-03 17:29:46', '1278555180714426370', '2020-07-21 13:14:58');
INSERT INTO `sys_resource` VALUES ('1281149138422505473', '资源名称验证', 'SYS_RESOURCE:NAME', '/sysResource/check/name', 'POST', 3, 0, '1278982692498079746', '', 1, '1278555180714426370', '2020-07-09 16:52:12', NULL, '2020-07-09 19:40:28');
INSERT INTO `sys_resource` VALUES ('1281205519208960001', '公司信息', 'COMPANY', '/company', 'GET', 1, 5, '1282564052173307905', '', 1, '1278555180714426370', '2020-07-09 20:36:14', '1278555180714426370', '2020-07-22 21:23:56');
INSERT INTO `sys_resource` VALUES ('1281213295616372738', '员工管理', 'SYS_USER', '/sysUser', 'GET', 1, 8, '1282564052173307905', '', 1, '1278555180714426370', '2020-07-09 21:07:08', '1278555180714426370', '2020-07-22 21:24:25');
INSERT INTO `sys_resource` VALUES ('1281936912939548673', '角色查看', 'SYS_ROLE:LIST', '/sysRole/query', 'GET', 2, 1, '1278982449626906626', '', 1, '1278555180714426370', '2020-07-11 21:02:32', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1281937460166197250', '角色新增', 'SYS_ROLE:ADD', '/sysRole', 'POST', 2, 2, '1278982449626906626', '', 1, '1278555180714426370', '2020-07-11 21:04:42', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1281937659026538498', '角色修改', 'SYS_ROLE:EDIT', '/sysRole', 'PUT', 2, 3, '1278982449626906626', '', 1, '1278555180714426370', '2020-07-11 21:05:30', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1281938002812665858', '角色删除', 'SYS_ROLE:REMOVE', '/sysRole', 'DELETE', 2, 4, '1278982449626906626', '', 1, '1278555180714426370', '2020-07-11 21:06:52', '1278555180714426370', '2020-07-11 21:09:15');
INSERT INTO `sys_resource` VALUES ('1281940829031170049', '获取资源树菜单', 'SYS_RESOURCE:TREE', '/sysResource/tree', 'POST', 3, 1, '1278982692498079746', '', 1, '1278555180714426370', '2020-07-11 21:18:06', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1282519591921704962', '员工查看', 'SYS_USER:LIST', '/sysUser/query', 'GET', 2, 1, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-13 11:37:53', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1282519800420556802', '公司查看', 'COMPANY:LIST', '/company/query', 'GET', 2, 1, '1281205519208960001', '', 1, '1278555180714426370', '2020-07-13 11:38:43', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1282564052173307905', '公司管理', 'ORG_MANAGE', '', 'GET', 1, 10, NULL, '', 1, '1278555180714426370', '2020-07-13 14:34:34', '1278555180714426370', '2020-07-22 21:22:11');
INSERT INTO `sys_resource` VALUES ('1282609463516807169', '组织架构', 'SYS_DEPT', '/sysDept', 'GET', 1, 6, '1282564052173307905', '', 1, '1278555180714426370', '2020-07-13 17:35:00', '1278555180714426370', '2020-07-22 21:24:11');
INSERT INTO `sys_resource` VALUES ('1282609674825842690', '部门查看', 'SYS_DEPT:LIST', '/sysDept/query', 'GET', 2, 1, '1282609463516807169', '', 1, '1278555180714426370', '2020-07-13 17:35:51', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1282918320499601409', '岗位管理', 'SYS_POST', '/sysPost', 'GET', 1, 7, '1282564052173307905', '', 1, '1278555180714426370', '2020-07-14 14:02:18', '1278555180714426370', '2020-07-22 21:23:31');
INSERT INTO `sys_resource` VALUES ('1282918481963528194', '岗位查看', 'SYS_POST:LIST', '/sysPost/query', 'GET', 2, 1, '1282918320499601409', '', 1, '1278555180714426370', '2020-07-14 14:02:56', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1282920381979131905', '员工新增', 'SYS_USER:ADD', '/sysUser', 'POST', 2, 2, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-14 14:10:29', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1282920654822801409', '员工修改', 'SYS_USER:EDIT', '/sysUser', 'PUT', 2, 2, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-14 14:11:34', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1282921306340818946', '员工授权', 'SYS_USER:AUTH', '/sysUser/role', 'POST', 2, 3, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-14 14:14:10', '1278555180714426370', '2020-07-29 17:00:32');
INSERT INTO `sys_resource` VALUES ('1282921493318696961', '员工授权页面', 'SYS_USER:AUTH:PAGE', '/sysUser/auth', 'GET', 3, 0, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-14 14:14:54', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1283297899940732930', '员工密码重置', 'SYS_USER:PWD', '/sysUser/password', 'POST', 2, 6, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-15 15:10:37', '1278555180714426370', '2020-07-29 17:01:23');
INSERT INTO `sys_resource` VALUES ('1283298102534004738', '员工开启/禁用', 'SYS_USER:ENABLE', '/sysUser/enable', 'POST', 2, 7, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-15 15:11:25', '1278555180714426370', '2020-07-29 17:01:32');
INSERT INTO `sys_resource` VALUES ('1285402891346558978', '部门新增', 'SYS_DEPT:ADD', '/sysDept', 'POST', 2, 2, '1282609463516807169', '', 1, '1278555180714426370', '2020-07-21 10:35:06', '1278555180714426370', '2020-07-21 10:45:47');
INSERT INTO `sys_resource` VALUES ('1285443665094279170', '公司新增', 'COMPANY:ADD', '/company', 'POST', 2, 1, '1281205519208960001', '', 1, '1278555180714426370', '2020-07-21 13:17:07', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285443956032176129', '公司编辑', 'COMPANY:EDIT', '/company', 'PUT', 2, 3, '1281205519208960001', '', 1, '1278555180714426370', '2020-07-21 13:18:16', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285444147804143617', '公司删除', 'COMPANY:REMOVE', '/company', 'DELETE', 2, 4, '1281205519208960001', '', 1, '1278555180714426370', '2020-07-21 13:19:02', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285444410380156929', '部门编辑', 'SYS_DEPT:EDIT', '/sysDept', 'PUT', 2, 3, '1282609463516807169', '', 1, '1278555180714426370', '2020-07-21 13:20:04', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285444560825647105', '部门删除', 'SYS_DEPT:REMOVE', '/sysDept', 'DELETE', 2, 1, '1282609463516807169', '', 1, '1278555180714426370', '2020-07-21 13:20:40', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285445230337228801', '岗位新增', 'SYS_POST:ADD', '/sysPost', 'POST', 2, 2, '1282918320499601409', '', 1, '1278555180714426370', '2020-07-21 13:23:20', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285445372373139457', '岗位编辑', 'SYS_POST:EDIT', '/sysPost', 'PUT', 2, 3, '1282918320499601409', '', 1, '1278555180714426370', '2020-07-21 13:23:54', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285445563746648066', '岗位删除', 'SYS_POST:REMOVE', '/sysPost', 'DELETE', 2, 1, '1282918320499601409', '', 1, '1278555180714426370', '2020-07-21 13:24:39', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285834407805505538', '员工部门树', 'SYS_USER:DEPT:TREE', '/sysUser/dept/tree', 'POST', 3, 0, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-22 15:09:47', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285835728327593985', '部门树', 'SYS_DEPT:TREE', '/sysDept/tree', 'POST', 3, 1, '1282609463516807169', '', 1, '1278555180714426370', '2020-07-22 15:15:02', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285836055441362946', '员工岗位', 'SYS_USER:POST', '/sysUser/post', 'POST', 3, 1, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-22 15:16:20', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1285865104456863745', '岗位部门树', 'SYS_POST:DEPT:TREE', '/sysPost/dept/tree', 'POST', 3, 1, '1282918320499601409', '', 1, '1278555180714426370', '2020-07-22 17:11:46', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1286158428459827202', '外员管理', 'OUTSIDER', '/outsider', 'GET', 1, 9, '1282564052173307905', '', 1, '1278555180714426370', '2020-07-23 12:37:20', '1278555180714426370', '2020-07-23 16:07:40');
INSERT INTO `sys_resource` VALUES ('1286158623285248002', '外部人员查看', 'OUTSIDER:LIST', '/outsider/query', 'GET', 2, 1, '1286158428459827202', '', 1, '1278555180714426370', '2020-07-23 12:38:06', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1286158980233101313', '外部人员新增', 'OUTSIDER:ADD', '/outsider', 'POST', 2, 2, '1286158428459827202', '', 1, '1278555180714426370', '2020-07-23 12:39:31', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1286159142296813570', '外部人员编辑', 'OUTSIDER:EDIT', '/outsider', 'PUT', 2, 3, '1286158428459827202', '', 1, '1278555180714426370', '2020-07-23 12:40:10', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1286159271129055234', '外部人员删除', 'OUTSIDER:REMOVE', '/outsider', 'DELETE', 2, 4, '1286158428459827202', '', 1, '1278555180714426370', '2020-07-23 12:40:41', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1287633877320912898', '角色操作页面', 'SYS_ROLE:PAGE', '/sysRole/saveOrUpdate', 'GET', 3, 10, '1278982449626906626', '', 1, '1278555180714426370', '2020-07-27 14:20:14', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1287634644698189826', '角色资源树', 'SYS_ROLE:RESOURCE:TREE', '/sysRole/resource/tree', 'POST', 3, 9, '1278982449626906626', '', 1, '1278555180714426370', '2020-07-27 14:23:17', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1288399584845393922', '员工角色选择', 'SYS_USER:ROLE:BOX', '/sysUser/role', 'GET', 3, 10, '1281213295616372738', '', 1, '1278555180714426370', '2020-07-29 17:02:53', '1278555180714426370', '2020-07-29 18:27:00');
INSERT INTO `sys_resource` VALUES ('1289100461386260481', '客户管理', 'CUSTOMER:MANAGE', '', 'GET', 1, 15, NULL, '', 1, '1278555180714426370', '2020-07-31 15:27:55', '1278555180714426370', '2020-08-14 10:19:43');
INSERT INTO `sys_resource` VALUES ('1289100654869504001', '派单管理', 'ORDER_BASIC', '/orderBasic', 'GET', 1, 1, '1289100461386260481', '', 1, '1278555180714426370', '2020-07-31 15:28:41', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1289106538781749250', '派单查询', 'ORDER_BASIC:LIST', '/orderBasic/query', 'GET', 2, 0, '1289100654869504001', '', 1, '1278555180714426370', '2020-07-31 15:52:04', '1278555180714426370', '2020-10-23 15:16:50');
INSERT INTO `sys_resource` VALUES ('1289133467509534721', '派单新增', 'ORDER_BASIC:ADD', '/orderBasic', 'POST', 2, 1, '1289100654869504001', '', 1, '1278555180714426370', '2020-07-31 17:39:04', '1278555180714426370', '2020-08-11 10:50:14');
INSERT INTO `sys_resource` VALUES ('1289133639484387330', '派单操作页面', 'ORDER_BASIC:PAGE', '/orderBasic/saveOrUpdate', 'GET', 3, 10, '1289100654869504001', '', 1, '1278555180714426370', '2020-07-31 17:39:45', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1291218552446566401', '派单编辑', 'ORDER_BASIC:EDIT', '/orderBasic', 'PUT', 2, 2, '1289100654869504001', '', 1, '1278555180714426370', '2020-08-06 11:44:27', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1291219048750170114', '派单所有人员选择', 'ORDER_BASIC:USER', '/orderBasic/allUser', 'POST', 3, 8, '1289100654869504001', '', 1, '1278555180714426370', '2020-08-06 11:46:26', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1291219269655773186', '派单部门人员选择', 'ORDER_BASIC:DEPT:USER', '/orderBasic/deptUser', 'POST', 3, 9, '1289100654869504001', '', 1, '1278555180714426370', '2020-08-06 11:47:18', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1291219470651015170', '派单部门选择', 'ORDER_BASIC:DEPT', '/orderBasic/deptTree', 'POST', 3, 9, '1289100654869504001', '', 1, '1278555180714426370', '2020-08-06 11:48:06', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293086877006970882', '洽谈管理', 'TALK:PAGE', '/orderBasic/talk', 'GET', 1, 2, '1289100461386260481', '', 1, '1278555180714426370', '2020-08-11 15:28:31', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293087215298560002', '洽谈查询', 'TALK:LIST', '/orderBasic/query', 'GET', 2, 1, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-11 15:29:51', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293115091532120066', '洽谈生成', 'ORDER_BASIC:TALK', '/orderBasic/talk', 'POST', 2, 4, '1289100654869504001', '', 1, '1278555180714426370', '2020-08-11 17:20:37', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293365652638707713', '删除洽谈', 'TALK:REMOVE', '/orderBasic/talk', 'POST', 2, 2, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-12 09:56:16', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293418861126250498', '部门使用验证', 'SYS_DEPT:CHECK', '/sysDept/check', 'POST', 3, 7, '1282609463516807169', '', 1, '1278555180714426370', '2020-08-12 13:27:42', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293426013014368258', '岗位删除验证', 'SYS_POST:CHECK', '/sysPost/check', 'POST', 3, 8, '1282918320499601409', '', 1, '1278555180714426370', '2020-08-12 13:56:07', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293801369537888257', '洽谈进度列表页面', 'TALK_DETAIL:LIST:PAGE', '/talkDetail/page', 'GET', 3, 3, '1293806472227520513', '', 1, '1278555180714426370', '2020-08-13 14:47:39', '1278555180714426370', '2020-08-14 14:16:40');
INSERT INTO `sys_resource` VALUES ('1293802372349837313', '查看洽谈进度页面', 'TALK_DETAIL:VIEW', '/talkDetail', 'GET', 2, 5, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-13 14:51:38', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293806472227520513', '洽谈明细列表查询', 'TALK_DETAIL:LIST', '/talkDetail/query', 'GET', 2, 2, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-13 15:07:55', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293808242009899009', '洽谈明细新增', 'TALK_DETAIL:ADD', '/talkDetail', 'POST', 2, 3, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-13 15:14:57', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293808448793280514', '洽谈明细修改', 'TALK_DETAIL:EDIT', '/talkDetail', 'PUT', 2, 4, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-13 15:15:47', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293808656386162689', '洽谈明细删除', 'TALK_DETAIL:REMOVE', '/talkDetail', 'DELETE', 2, 6, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-13 15:16:36', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1293818453177208833', '洽谈明细操作页面', 'TALK_DETAIL:SAVEORUPDATE', '/talkDetail/saveOrUpdate', 'GET', 3, 1, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-13 15:55:32', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1294141152269242370', '邀约人员选择', 'TALK_DETAIL:USER', '/orderBasic/allUser', 'POST', 3, 9, '1293086877006970882', '', 1, '1278555180714426370', '2020-08-14 13:17:49', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1302867660613644289', '合同管理', 'CONTRACT_MANAGE', '', 'GET', 1, 30, NULL, '', 1, '1278555180714426370', '2020-09-07 15:13:51', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1302868150365773825', '协议模板', 'TREATY_MODEL', '/treatyModel', 'GET', 1, 1, '1302867660613644289', '', 1, '1278555180714426370', '2020-09-07 15:15:48', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1302868686678843393', '协议模板查看', 'TREATY_MODEL:LIST', '/treatyModel/query', 'GET', 2, 1, '1302868150365773825', '', 1, '1278555180714426370', '2020-09-07 15:17:56', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1302868862952857601', '协议模板新增', 'TREATY_MODEL:ADD', '/treatyModel', 'POST', 2, 2, '1302868150365773825', '', 1, '1278555180714426370', '2020-09-07 15:18:38', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1302869087557836802', '协议模板修改', 'TREATY_MODEL:EDIT', '/treatyModel', 'PUT', 2, 3, '1302868150365773825', '', 1, '1278555180714426370', '2020-09-07 15:19:31', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1302869227282685954', '协议模板删除', 'TREATY_MODEL:REMOVE', '/treatyModel', 'DELETE', 2, 4, '1302868150365773825', '', 1, '1278555180714426370', '2020-09-07 15:20:05', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1302869732058783746', '协议模板操作页面', 'TREATY_MODEL:SAVEORUPDATE', '/treatyModel/saveOrUpdate', 'GET', 3, 5, '1302868150365773825', '', 1, '1278555180714426370', '2020-09-07 15:22:05', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1305749964696608770', '考勤管理', 'ATTEN_MANAGE', '', 'GET', 1, 11, NULL, '', 1, '1278555180714426370', '2020-09-15 14:07:06', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1305750213041348610', '考勤模板', 'ATTEN_MODEL', '/attenModel', 'GET', 1, 1, '1305749964696608770', '', 1, '1278555180714426370', '2020-09-15 14:08:05', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1305750471808933889', '考勤模板查看', 'ATTEN_MODEL:LIST', '/attenModel/query', 'GET', 2, 1, '1305750213041348610', '', 1, '1278555180714426370', '2020-09-15 14:09:07', '1278555180714426370', '2020-09-15 14:09:23');
INSERT INTO `sys_resource` VALUES ('1305750773240979457', '考勤模板新增', 'ATTEN_MODEL:ADD', '/attenModel', 'POST', 2, 2, '1305750213041348610', '', 1, '1278555180714426370', '2020-09-15 14:10:19', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1305750930430910466', '考勤模板修改', 'ATTEN_MODEL:EDIT', '/attenModel', 'PUT', 2, 3, '1305750213041348610', '', 1, '1278555180714426370', '2020-09-15 14:10:56', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1305751081564266498', '考勤模板删除', 'ATTEN_MODEL:REMOVE', '/attenModel', 'DELETE', 2, 4, '1305750213041348610', '', 1, '1278555180714426370', '2020-09-15 14:11:32', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1305751317380620290', '考勤模板操作页面', 'ATTEN_MODEL:SAVEORUPDATE', '/attenModel/saveOrUpdate', 'GET', 3, 5, '1305750213041348610', '', 1, '1278555180714426370', '2020-09-15 14:12:28', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1305802455517827073', '生成考勤', 'ATTEN_MODEL:GENERATE', '/attenModel/generate', 'POST', 2, 4, '1305750213041348610', '', 1, '1278555180714426370', '2020-09-15 17:35:41', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1306867185808330754', '合同模板', 'CONTRACT_MODEL', '/contractModel', 'GET', 1, 2, '1302867660613644289', '', 1, '1278555180714426370', '2020-09-18 16:06:32', '1278555180714426370', '2020-09-18 16:07:29');
INSERT INTO `sys_resource` VALUES ('1306867667037605890', '合同模板查看', 'CONTRACT_MODEL:LIST', '/contractModel/query', 'GET', 2, 1, '1306867185808330754', '', 1, '1278555180714426370', '2020-09-18 16:08:27', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1306867893559382018', '合同模板新增', 'CONTRACT_MODEL:ADD', '/contractModel', 'POST', 2, 2, '1306867185808330754', '', 1, '1278555180714426370', '2020-09-18 16:09:21', '1278555180714426370', '2020-09-18 16:09:42');
INSERT INTO `sys_resource` VALUES ('1306868101181624322', '合同模板修改', 'CONTRACT_MODEL:EDIT', '/contractModel', 'PUT', 2, 3, '1306867185808330754', '', 1, '1278555180714426370', '2020-09-18 16:10:10', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1306868326419943425', '合同模板下载', 'CONTRACT_MODEL:DOWNLOAD', '/contractModel/download', 'POST', 2, 4, '1306867185808330754', '', 1, '1278555180714426370', '2020-09-18 16:11:04', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1306868485648306177', '合同模板删除', 'CONTRACT_MODEL:REMOVE', '/contractModel', 'DELETE', 2, 5, '1306867185808330754', '', 1, '1278555180714426370', '2020-09-18 16:11:42', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1306868737533038593', '合同模板操作页面', 'CONTRACT_MODEL:SAVEORUPDATE', '/contractModel/saveOrUpdate', 'GET', 3, 7, '1306867185808330754', '', 1, '1278555180714426370', '2020-09-18 16:12:42', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1308702406367285249', '合同类型', 'CONTRACT_TYPE', '/contractType', 'GET', 1, 3, '1302867660613644289', '', 1, '1278555180714426370', '2020-09-23 17:39:03', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1308702645388087297', '合同类型查看', 'CONTRACT_TYPE:LIST', '/contractType/query', 'GET', 2, 1, '1308702406367285249', '', 1, '1278555180714426370', '2020-09-23 17:40:00', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1308702772983009281', '合同类型新增', 'CONTRACT_TYPE:ADD', '/contractType', 'POST', 2, 2, '1308702406367285249', '', 1, '1278555180714426370', '2020-09-23 17:40:30', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1308702923252338690', '合同类型修改', 'CONTRACT_TYPE:EDIT', '/contractType', 'PUT', 2, 3, '1308702406367285249', '', 1, '1278555180714426370', '2020-09-23 17:41:06', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1308703048112574465', '合同类型删除', 'CONTRACT_TYPE:REMOVE', '/contractType', 'DELETE', 2, 4, '1308702406367285249', '', 1, '1278555180714426370', '2020-09-23 17:41:36', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1308703209979154433', '合同类型操作页面', 'CONTRACT_TYPE:SAVEORUPDATE', '/contractType/saveOrUpdate', 'GET', 3, 5, '1308702406367285249', '', 1, '1278555180714426370', '2020-09-23 17:42:14', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1309031879524605954', '合同领用', 'CONTRACT_RECEIVE', '/contractReceive', 'GET', 1, 4, '1302867660613644289', '', 1, '1278555180714426370', '2020-09-24 15:28:15', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1309032059783208961', '合同领用查看', 'CONTRACT_RECEIVE:LIST', '/contractReceive/query', 'GET', 2, 1, '1309031879524605954', '', 1, '1278555180714426370', '2020-09-24 15:28:58', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1309032377602400258', '合同领用部门树', 'CONTRACT_RECEIVE:DEPT:TREE', '/contractReceive/dept/tree', 'POST', 3, 2, '1309031879524605954', '', 1, '1278555180714426370', '2020-09-24 15:30:14', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1309032526751850498', '合同领用新增', 'CONTRACT_RECEIVE:ADD', '/contractReceive', 'POST', 2, 3, '1309031879524605954', '', 1, '1278555180714426370', '2020-09-24 15:30:50', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1309032677344141313', '合同领用修改', 'CONTRACT_RECEIVE:EDIT', '/contractReceive', 'PUT', 2, 4, '1309031879524605954', '', 1, '1278555180714426370', '2020-09-24 15:31:26', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1309032919040909313', '合同领用操作页面', 'CONTRACT_RECEIVE:SAVEORUPDATE', '/contractReceive/saveOrUpdate', 'GET', 3, 5, '1309031879524605954', '', 1, '1278555180714426370', '2020-09-24 15:32:23', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1309051143132319745', '合同领用岗位框', 'CONTRACT_RECEIVE:POST', '/contractReceive/post', 'POST', 3, 6, '1309031879524605954', '', 1, '1278555180714426370', '2020-09-24 16:44:48', NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1309051465368113153', '合同领用员工', 'CONTRACT_RECEIVE:USER', '/contractReceive/user', 'POST', 3, 7, '1309031879524605954', '', 1, '1278555180714426370', '2020-09-24 16:46:05', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '开发主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色CODE',
  `status` int(4) NULL DEFAULT NULL COMMENT '角色状态：0-禁用，1-正常',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '后台角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1278563125074386946', '超级管理员', 'ADMINISTRATOR', 1, '1278555180714426370', '2020-07-02 13:36:18', NULL, NULL);
INSERT INTO `sys_role` VALUES ('1282522401115598849', '人事助手', 'HR:HELPER', 1, '1278555180714426370', '2020-07-13 11:49:03', NULL, '2020-08-12 14:53:01');
INSERT INTO `sys_role` VALUES ('1294497674912399361', '产品助手', 'PT:HELPER', 1, '1278555180714426370', '2020-08-15 12:54:31', NULL, '2020-08-17 15:54:40');
INSERT INTO `sys_role` VALUES ('1319538520959311873', '测试-只读', 'TEST:READONLY', 1, '1278555180714426370', '2020-10-23 15:17:54', NULL, '2020-10-23 15:25:20');

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开发主键',
  `sys_role_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后台角色ID',
  `sys_resource_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后台资源ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台角色 - 后台资源 关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO `sys_role_resource` VALUES ('1293440332418572290', '1282522401115598849', '1289100461386260481', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332418572291', '1282522401115598849', '1289100654869504001', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332418572292', '1282522401115598849', '1289133467509534721', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332418572293', '1282522401115598849', '1291218552446566401', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332418572294', '1282522401115598849', '1293115091532120066', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332435349505', '1282522401115598849', '1289106538781749250', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332435349506', '1282522401115598849', '1282564052173307905', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332435349507', '1282522401115598849', '1281205519208960001', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332439543810', '1282522401115598849', '1282519800420556802', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332439543811', '1282522401115598849', '1285443665094279170', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332439543812', '1282522401115598849', '1285443956032176129', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332439543813', '1282522401115598849', '1282609463516807169', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332439543814', '1282522401115598849', '1282609674825842690', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332443738113', '1282522401115598849', '1285402891346558978', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332443738114', '1282522401115598849', '1285444410380156929', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332443738115', '1282522401115598849', '1282918320499601409', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332443738116', '1282522401115598849', '1282918481963528194', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332443738117', '1282522401115598849', '1285445230337228801', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332443738118', '1282522401115598849', '1285445372373139457', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332443738119', '1282522401115598849', '1281213295616372738', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126721', '1282522401115598849', '1282519591921704962', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126722', '1282522401115598849', '1282920381979131905', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126723', '1282522401115598849', '1282920654822801409', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126724', '1282522401115598849', '1286158428459827202', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126725', '1282522401115598849', '1286158623285248002', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126726', '1282522401115598849', '1286158980233101313', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126727', '1282522401115598849', '1286159142296813570', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126729', '1282522401115598849', '1282921493318696961', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126730', '1282522401115598849', '1285834407805505538', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332452126733', '1282522401115598849', '1285835728327593985', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515329', '1282522401115598849', '1285836055441362946', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515331', '1282522401115598849', '1285865104456863745', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515333', '1282522401115598849', '1288399584845393922', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515334', '1282522401115598849', '1289133639484387330', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515335', '1282522401115598849', '1291219048750170114', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515336', '1282522401115598849', '1291219269655773186', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515337', '1282522401115598849', '1291219470651015170', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515338', '1282522401115598849', '1293418861126250498', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1293440332460515339', '1282522401115598849', '1293426013014368258', '2020-08-12 14:53:01');
INSERT INTO `sys_role_resource` VALUES ('1295267788729470978', '1294497674912399361', '1282564052173307905', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788737859586', '1294497674912399361', '1281205519208960001', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788737859587', '1294497674912399361', '1282519800420556802', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788742053890', '1294497674912399361', '1282609463516807169', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788742053891', '1294497674912399361', '1282609674825842690', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788742053892', '1294497674912399361', '1282918320499601409', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788742053893', '1294497674912399361', '1282918481963528194', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788742053894', '1294497674912399361', '1289100461386260481', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788750442497', '1294497674912399361', '1289100654869504001', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788750442498', '1294497674912399361', '1289133467509534721', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788750442499', '1294497674912399361', '1291218552446566401', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788750442500', '1294497674912399361', '1293115091532120066', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788750442501', '1294497674912399361', '1289106538781749250', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788754636801', '1294497674912399361', '1293086877006970882', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788754636802', '1294497674912399361', '1293087215298560002', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788754636803', '1294497674912399361', '1293806472227520513', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788754636804', '1294497674912399361', '1293802372349837313', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788763025412', '1294497674912399361', '1285835728327593985', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788763025414', '1294497674912399361', '1285865104456863745', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788763025415', '1294497674912399361', '1289133639484387330', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788767219713', '1294497674912399361', '1291219048750170114', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788767219714', '1294497674912399361', '1291219269655773186', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788767219715', '1294497674912399361', '1291219470651015170', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788767219716', '1294497674912399361', '1293418861126250498', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788767219717', '1294497674912399361', '1293426013014368258', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788767219718', '1294497674912399361', '1293801369537888257', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788767219719', '1294497674912399361', '1293818453177208833', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1295267788767219720', '1294497674912399361', '1294141152269242370', '2020-08-17 15:54:40');
INSERT INTO `sys_role_resource` VALUES ('1319540392919093249', '1319538520959311873', '1282564052173307905', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392944259074', '1319538520959311873', '1281205519208960001', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392952647682', '1319538520959311873', '1282519800420556802', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392952647683', '1319538520959311873', '1282609463516807169', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392952647684', '1319538520959311873', '1282609674825842690', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392961036289', '1319538520959311873', '1282918320499601409', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392969424898', '1319538520959311873', '1282918481963528194', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392969424899', '1319538520959311873', '1281213295616372738', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392973619202', '1319538520959311873', '1282519591921704962', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392973619203', '1319538520959311873', '1286158428459827202', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392982007810', '1319538520959311873', '1286158623285248002', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392990396417', '1319538520959311873', '1305749964696608770', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392994590721', '1319538520959311873', '1305750213041348610', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540392994590722', '1319538520959311873', '1305750471808933889', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393011367937', '1319538520959311873', '1289100461386260481', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393011367938', '1319538520959311873', '1289100654869504001', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393023950850', '1319538520959311873', '1289106538781749250', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393028145154', '1319538520959311873', '1293086877006970882', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393036533762', '1319538520959311873', '1293087215298560002', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393036533763', '1319538520959311873', '1302867660613644289', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393040728066', '1319538520959311873', '1302868150365773825', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393040728067', '1319538520959311873', '1302868686678843393', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393049116674', '1319538520959311873', '1306867185808330754', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393049116675', '1319538520959311873', '1306867667037605890', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393053310978', '1319538520959311873', '1308702406367285249', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393057505281', '1319538520959311873', '1308702645388087297', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393057505282', '1319538520959311873', '1309031879524605954', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393057505283', '1319538520959311873', '1309032059783208961', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393070088194', '1319538520959311873', '1278981598636179458', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393070088195', '1319538520959311873', '1278982449626906626', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393070088196', '1319538520959311873', '1281936912939548673', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393074282498', '1319538520959311873', '1278982692498079746', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393074282499', '1319538520959311873', '1278983556180770817', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393074282500', '1319538520959311873', '1278984265819258882', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393082671106', '1319538520959311873', '1281149138422505473', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393086865410', '1319538520959311873', '1281940829031170049', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393086865411', '1319538520959311873', '1282921493318696961', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393086865412', '1319538520959311873', '1285834407805505538', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393095254018', '1319538520959311873', '1285835728327593985', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393095254019', '1319538520959311873', '1285836055441362946', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393099448321', '1319538520959311873', '1285865104456863745', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393099448322', '1319538520959311873', '1287633877320912898', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393103642625', '1319538520959311873', '1287634644698189826', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393103642626', '1319538520959311873', '1288399584845393922', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393103642627', '1319538520959311873', '1289133639484387330', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393112031234', '1319538520959311873', '1291219048750170114', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393112031235', '1319538520959311873', '1291219269655773186', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393112031236', '1319538520959311873', '1291219470651015170', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393112031237', '1319538520959311873', '1293418861126250498', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393120419842', '1319538520959311873', '1293426013014368258', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393124614146', '1319538520959311873', '1293818453177208833', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393124614147', '1319538520959311873', '1294141152269242370', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393124614148', '1319538520959311873', '1302869732058783746', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393133002753', '1319538520959311873', '1305751317380620290', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393133002754', '1319538520959311873', '1306868737533038593', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393137197058', '1319538520959311873', '1308703209979154433', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393141391361', '1319538520959311873', '1309032377602400258', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393141391362', '1319538520959311873', '1309032919040909313', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393145585665', '1319538520959311873', '1309051143132319745', '2020-10-23 15:25:20');
INSERT INTO `sys_role_resource` VALUES ('1319540393145585666', '1319538520959311873', '1309051465368113153', '2020-10-23 15:25:20');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '开发主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '英文名称',
  `sex` int(2) NULL DEFAULT 0 COMMENT '性别：0-女，1-男',
  `job_no` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工号',
  `mobile` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `card_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `bank_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行卡号',
  `bank_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '办理地',
  `bank_account_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开户行名称',
  `dept_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '现任部门ID',
  `native_place` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '籍贯',
  `household` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '户别',
  `social_start_time` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '社保缴纳起始月份',
  `social_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '社保缴纳缴费基数',
  `housing_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '公积金基数',
  `social_end_time` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '社保缴纳最后月份',
  `education` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学历',
  `school` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '毕业学校',
  `major` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业',
  `entry_time` date NULL DEFAULT NULL COMMENT '入职时间',
  `contract_time` date NULL DEFAULT NULL COMMENT '合同时间',
  `entry_dept_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '入职部门ID',
  `entry_post_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '入职岗位ID',
  `post_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '现任岗位ID',
  `entry_salary` decimal(10, 2) NULL DEFAULT NULL COMMENT '入职薪资',
  `worker_salary` decimal(10, 2) NULL DEFAULT NULL COMMENT '转正薪资',
  `salary` decimal(10, 2) NULL DEFAULT NULL COMMENT '实时薪资',
  `merits_salary` decimal(10, 2) NULL DEFAULT NULL COMMENT '绩效薪资',
  `post_salary` decimal(10, 2) NULL DEFAULT NULL COMMENT '岗位薪资',
  `worker_time` date NULL DEFAULT NULL COMMENT '转正日期',
  `quit_time` date NULL DEFAULT NULL COMMENT '离职日期',
  `status` int(4) NULL DEFAULT NULL COMMENT '员工状态：0-离职，1-在职',
  `is_disable` int(4) NULL DEFAULT NULL COMMENT '是否禁用：0-否，1-是',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mobile_unique`(`job_no`, `mobile`, `email`, `card_no`, `bank_no`) USING BTREE COMMENT '唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '员工信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1278555180714426370', '吴雅静', 'Gemini.Wu', 0, '014', '15024424787', '$2a$10$nvUG/esvIMNhtetSusetguuaRdefjz2BYt2vL0rbZBNGMFoA8kJ1G', '172415673@qq.com', '331081198706233028', '6222021202017172785', '中国工商银行浙江省杭州市杭州钱江支行', '杭州', '工商银行', '1285898303719415810', '浙江台州', '居民', '2020-04', 3321.60, 0.00, '', '本科', '浙江万里学院', '新闻学', '2020-03-24', '2021-03-23', '1285898303719415810', '1285898386624028674', '1285898386624028674', 15000.00, 15000.00, 15000.00, 0.00, 15000.00, '2020-04-24', NULL, 1, 0, '1278555180714426370', '2020-07-02 13:04:44', '1278555180714426370', '2020-08-14 11:27:52');
INSERT INTO `sys_user` VALUES ('1283248274567397378', '席辉霖', 'Felix.Xi', 1, '001', '15824180077', '$2a$10$JkjCVf/QBf1BbjczgXxPlelpPLnTYmiYH9snise80LK23.1zMbj76', '229638061@qq.com', '362523198601010039', '6222081202010271086', '中国工商银行浙江省杭州市杭州拱宸支行', '杭州', '工商银行', '1293427140506214402', '浙江杭州', '居民', '2020-02', 3321.60, 2010.00, '', '', '', '', '2020-01-07', '2023-01-07', '1293427140506214402', '1293427692417900546', '1293427692417900546', 3500.00, 3500.00, 3500.00, 0.00, 3500.00, '2020-01-07', NULL, 1, 0, '1278555180714426370', '2020-07-15 11:53:25', '1278555180714426370', '2020-10-23 15:59:57');
INSERT INTO `sys_user` VALUES ('1283268371268767746', '刘露露', 'Fancy.Liu', 0, '003', '15551759807', '$2a$10$YmWAkgEjhEUTDLBeFOZkvOzHVK8.yX012tKv18wQcKmhxkyBjTRUe', '410303264@qq.com', '34120219930105236X', '6222021306003978822', '中国工商银行马鞍山团结广场支行', '马鞍山', '工商银行', '1285898303719415810', '安徽阜阳', '农业', '2020-02', 3321.60, 0.00, '', '本科', '河海大学文天学院', '财务管理', '2020-02-29', '2021-02-28', '1285898303719415810', '1278260848728023041', '1278260848728023041', 4500.00, 4500.00, 4500.00, 0.00, 4500.00, '2020-03-29', NULL, 1, 0, '1278555180714426370', '2020-07-15 13:13:16', '1278555180714426370', '2020-08-14 11:27:38');
INSERT INTO `sys_user` VALUES ('1290932591758204929', '徐梦幻', 'Sally.Xu', 0, '006', '18868084571', '$2a$10$Eb0HRe863NKIsWhKGuY5VuuJ/NgtNUEYS5HtJGDGaTteamLVF1o3C', '1194642813@qq.com', '332528199504290041', '6212261202050781575', '中国工商银行杭州祥符支行', '杭州', '工商银行', '1290925674356858882', '浙江丽水', '居民', '2020-04', 3321.60, 0.00, '', '大专', '衢州学院', '市场营销', '2020-03-08', '2020-03-07', '1290925674356858882', '1290931242748403714', '1290931242748403714', 3000.00, 3000.00, 3000.00, 1000.00, 4000.00, '2020-04-08', NULL, 1, 1, '1278555180714426370', '2020-08-05 16:48:09', '1278555180714426370', '2020-10-23 15:26:33');
INSERT INTO `sys_user` VALUES ('1290950816696954881', '张超艳', 'Cherry.Zhang', 0, '013', '18768035768', '$2a$10$CVFSWQSdz3kUx3bVV9z6gutBXg0ve2IxVNgpixIFEJUXt3i8cDIc2', '752850661@qq.com', '330903199705081820', '6212261202026433152', '中国工商银行杭州高新支行', '杭州', '工商银行', '1278255029512876034', '浙江舟山', '集体', '2020-04', 3321.60, 0.00, '', '本科', '中国计量大学', '环境设计', '2020-03-20', '2020-03-19', '1278255029512876034', '1290948815049256961', '1290948815049256961', 4000.00, 4000.00, 4000.00, 1000.00, 5000.00, '2020-04-20', NULL, 1, 1, '1278555180714426370', '2020-08-05 18:00:34', '1278555180714426370', '2020-08-05 18:01:17');
INSERT INTO `sys_user` VALUES ('1318452942721277954', '王琪', 'Echo.Wong', 0, '034', '15372065374', '$2a$10$5JKURw.xj1ntKnJrsxYK0uRKyut66WN6WqsGQapLujCpCNoiVozX2', '1315429782@qq.com', '411527199810107025', '6222021202047585923', '工商银行', '杭州', '中国工商银行杭州经开支行', '1285839545790156801', '河南信阳', '农业', '2020-06', 3321.60, 0.00, '', '大专', '浙江金融职业学院', '投资与理财', '2020-05-26', '2020-05-25', '1285839545790156801', '1285847353910677505', '1285847353910677505', 4500.00, 4500.00, 4500.00, 0.00, 4500.00, '2020-08-26', NULL, 1, 0, '1278555180714426370', '2020-10-20 15:24:12', '1278555180714426370', '2020-10-20 16:35:33');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '开发主键',
  `sys_user_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '员工用户ID',
  `sys_role_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '后台角色ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '员工用户 - 后台角色 关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1278908351899004929', '1278555180714426370', '1278563125074386946', '2020-07-03 12:28:07');
INSERT INTO `sys_user_role` VALUES ('1283285295625207809', '1283268371268767746', '1282522401115598849', '2020-07-15 14:20:31');
INSERT INTO `sys_user_role` VALUES ('1318472601789063169', '1318452942721277954', '1294497674912399361', '2020-10-20 16:42:19');
INSERT INTO `sys_user_role` VALUES ('1319540797627486209', '1283248274567397378', '1319538520959311873', '2020-10-23 15:26:57');

-- ----------------------------
-- Table structure for talk_detail
-- ----------------------------
DROP TABLE IF EXISTS `talk_detail`;
CREATE TABLE `talk_detail`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键，唯一标识',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邀约人员ID',
  `rate_time` date NULL DEFAULT NULL COMMENT '洽谈日期',
  `remarks` varchar(224) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `talk_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '洽谈ID',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '洽谈明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of talk_detail
-- ----------------------------
INSERT INTO `talk_detail` VALUES ('1294099438066229249', '1290950816696954881', '2020-08-03', '约谈', '1290951186856865794', '1278555180714426370', '2020-08-14 10:32:04', '1278555180714426370', '2020-08-14 13:19:27');
INSERT INTO `talk_detail` VALUES ('1294142561551122433', '1283268371268767746', '2020-08-05', '再次约谈', '1290951186856865794', '1278555180714426370', '2020-08-14 13:23:25', NULL, NULL);
INSERT INTO `talk_detail` VALUES ('1294143005719654402', '1278555180714426370', '2020-08-10', '成功签单', '1290951186856865794', '1278555180714426370', '2020-08-14 13:25:11', NULL, NULL);

-- ----------------------------
-- Table structure for treaty_model
-- ----------------------------
DROP TABLE IF EXISTS `treaty_model`;
CREATE TABLE `treaty_model`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键，唯一标识',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '协议名称',
  `code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '协议编码',
  `code_no` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '协议编号',
  `sort` int(8) NULL DEFAULT NULL COMMENT '排序号',
  `status` int(4) NULL DEFAULT NULL COMMENT '协议状态：0-禁用，1-启用',
  `create_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '协议模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of treaty_model
-- ----------------------------
INSERT INTO `treaty_model` VALUES ('1302869948824608770', '定金协议', 'DJ', '00', 1, 1, '1278555180714426370', '2020-09-07 15:22:57', NULL, NULL);
INSERT INTO `treaty_model` VALUES ('1302870041527115778', '设计协议', 'SJ', '01', 2, 1, '1278555180714426370', '2020-09-07 15:23:19', NULL, NULL);
INSERT INTO `treaty_model` VALUES ('1302872217938845698', '产品代购协议', 'CP', '02', 3, 1, '1278555180714426370', '2020-09-07 15:31:58', '1278555180714426370', '2020-09-07 16:23:40');
INSERT INTO `treaty_model` VALUES ('1302885283460046850', '产品合作协议', 'HZ', '03', 4, 1, '1278555180714426370', '2020-09-07 16:23:53', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
