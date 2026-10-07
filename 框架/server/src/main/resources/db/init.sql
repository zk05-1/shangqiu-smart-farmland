-- ============================================================================
-- 商丘市农业农村局智能农田管理系统 - 数据库初始化脚本
-- Shangqiu Agricultural and Rural Bureau Smart Farmland Management System
-- MySQL 8.0+
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. 创建数据库
-- ----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `shangqiu_farmland`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `shangqiu_farmland`;

-- ----------------------------------------------------------------------------
-- 2. 系统表 - 用户管理
-- ----------------------------------------------------------------------------

-- 2.1 系统用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`      VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`      VARCHAR(200) NOT NULL COMMENT '密码(BCrypt加密)',
    `real_name`     VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    `avatar`        VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `phone`         VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `email`         VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `gender`        TINYINT      DEFAULT NULL COMMENT '性别: 0-未知, 1-男, 2-女',
    `status`        TINYINT      DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `last_login_time` DATETIME   DEFAULT NULL COMMENT '最后登录时间',
    `created_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 2.2 系统角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_name`     VARCHAR(50)  NOT NULL COMMENT '角色名称',
    `role_code`     VARCHAR(50)  NOT NULL COMMENT '角色编码',
    `description`   VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
    `sort_order`    INT          DEFAULT 0 COMMENT '排序',
    `status`        TINYINT      DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_name` (`role_name`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- 2.3 系统菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id`     BIGINT       DEFAULT 0 COMMENT '父菜单ID, 0为顶级',
    `menu_name`     VARCHAR(50)  NOT NULL COMMENT '菜单名称',
    `menu_type`     VARCHAR(10)  NOT NULL COMMENT '菜单类型: CATALOG-目录, MENU-菜单, BUTTON-按钮',
    `path`          VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
    `component`     VARCHAR(200) DEFAULT NULL COMMENT '前端组件路径',
    `icon`          VARCHAR(50)  DEFAULT NULL COMMENT '图标',
    `sort_order`    INT          DEFAULT 0 COMMENT '排序',
    `permission`    VARCHAR(200) DEFAULT NULL COMMENT '权限标识',
    `status`        TINYINT      DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单表';

-- 2.4 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `id`            BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`       BIGINT   NOT NULL COMMENT '用户ID',
    `role_id`       BIGINT   NOT NULL COMMENT '角色ID',
    `created_time`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT  DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 2.5 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `id`            BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id`       BIGINT   NOT NULL COMMENT '角色ID',
    `menu_id`       BIGINT   NOT NULL COMMENT '菜单ID',
    `created_time`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT  DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- 2.6 操作日志表
DROP TABLE IF EXISTS `tb_operation_log`;
CREATE TABLE `tb_operation_log` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`         BIGINT       DEFAULT NULL COMMENT '操作用户ID',
    `username`        VARCHAR(50)  DEFAULT NULL COMMENT '操作用户名',
    `operation`       VARCHAR(100) DEFAULT NULL COMMENT '操作描述',
    `method`          VARCHAR(200) DEFAULT NULL COMMENT '请求方法',
    `params`          TEXT         DEFAULT NULL COMMENT '请求参数',
    `ip`              VARCHAR(50)  DEFAULT NULL COMMENT 'IP地址',
    `execution_time`  BIGINT       DEFAULT NULL COMMENT '执行耗时(毫秒)',
    `created_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ----------------------------------------------------------------------------
-- 3. 业务表 - 农田管理
-- ----------------------------------------------------------------------------

-- 3.1 农田地块表
DROP TABLE IF EXISTS `tb_farmland`;
CREATE TABLE `tb_farmland` (
    `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `farmland_code`   VARCHAR(50)   NOT NULL COMMENT '地块编号',
    `farmland_name`   VARCHAR(100)  NOT NULL COMMENT '地块名称',
    `area`            DECIMAL(10,2) NOT NULL COMMENT '面积(亩)',
    `location`        VARCHAR(255)  DEFAULT NULL COMMENT '位置',
    `longitude`       VARCHAR(20)   DEFAULT NULL COMMENT '经度',
    `latitude`        VARCHAR(20)   DEFAULT NULL COMMENT '纬度',
    `soil_type`       VARCHAR(50)   DEFAULT NULL COMMENT '土壤类型',
    `soil_ph`         DECIMAL(4,2)  DEFAULT NULL COMMENT '土壤pH值',
    `owner_name`      VARCHAR(50)   DEFAULT NULL COMMENT '负责人姓名',
    `owner_phone`     VARCHAR(20)   DEFAULT NULL COMMENT '负责人电话',
    `status`          VARCHAR(20)   DEFAULT 'USE' COMMENT '状态: USE-使用中, IDLE-闲置, FALLOW-休耕',
    `description`     TEXT          DEFAULT NULL COMMENT '描述',
    `image_url`       VARCHAR(255)  DEFAULT NULL COMMENT '图片URL',
    `created_time`    DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`    DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_farmland_code` (`farmland_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='农田地块表';

-- 3.2 作物类型表
DROP TABLE IF EXISTS `tb_crop_type`;
CREATE TABLE `tb_crop_type` (
    `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `crop_name`       VARCHAR(50)   NOT NULL COMMENT '作物名称',
    `crop_code`       VARCHAR(50)   NOT NULL COMMENT '作物编码',
    `category`        VARCHAR(50)   DEFAULT NULL COMMENT '分类',
    `growth_period`   INT           DEFAULT NULL COMMENT '生长周期(天)',
    `suitable_soil`   VARCHAR(100)  DEFAULT NULL COMMENT '适宜土壤',
    `suitable_temp`   VARCHAR(50)   DEFAULT NULL COMMENT '适宜温度',
    `description`     TEXT          DEFAULT NULL COMMENT '描述',
    `image_url`       VARCHAR(255)  DEFAULT NULL COMMENT '图片URL',
    `created_time`    DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`    DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_crop_code` (`crop_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作物类型表';

-- 3.3 种植记录表
DROP TABLE IF EXISTS `tb_planting_record`;
CREATE TABLE `tb_planting_record` (
    `id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `farmland_id`         BIGINT        NOT NULL COMMENT '农田地块ID',
    `crop_type_id`        BIGINT        NOT NULL COMMENT '作物类型ID',
    `plant_date`          DATE          DEFAULT NULL COMMENT '播种日期',
    `expect_harvest_date` DATE          DEFAULT NULL COMMENT '预计收获日期',
    `actual_harvest_date` DATE          DEFAULT NULL COMMENT '实际收获日期',
    `plant_area`          DECIMAL(10,2) NOT NULL COMMENT '种植面积(亩)',
    `seed_amount`         DECIMAL(10,2) DEFAULT NULL COMMENT '种子用量(kg)',
    `status`              VARCHAR(20)   DEFAULT 'PLANTING' COMMENT '状态: PLANTING-已播种, GROWING-生长中, HARVESTED-已收获',
    `description`         TEXT          DEFAULT NULL COMMENT '描述',
    `created_time`        DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`        DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`             TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_farmland_id` (`farmland_id`),
    KEY `idx_crop_type_id` (`crop_type_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='种植记录表';

-- 3.4 灌溉记录表
DROP TABLE IF EXISTS `tb_irrigation_record`;
CREATE TABLE `tb_irrigation_record` (
    `id`                 BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `farmland_id`        BIGINT        NOT NULL COMMENT '农田地块ID',
    `planting_record_id` BIGINT        DEFAULT NULL COMMENT '种植记录ID',
    `irrigate_date`      DATETIME      NOT NULL COMMENT '灌溉时间',
    `water_amount`       DECIMAL(10,2) NOT NULL COMMENT '用水量(立方米)',
    `irrigate_method`    VARCHAR(50)   DEFAULT NULL COMMENT '灌溉方式: DRIP-滴灌, SPRINKLER-喷灌, FLOOD-漫灌',
    `irrigate_duration`  INT           DEFAULT NULL COMMENT '灌溉时长(分钟)',
    `operator`           VARCHAR(50)   DEFAULT NULL COMMENT '操作人',
    `description`        TEXT          DEFAULT NULL COMMENT '描述',
    `created_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_farmland_id` (`farmland_id`),
    KEY `idx_planting_record_id` (`planting_record_id`),
    KEY `idx_irrigate_date` (`irrigate_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='灌溉记录表';

-- 3.5 施肥记录表
DROP TABLE IF EXISTS `tb_fertilization_record`;
CREATE TABLE `tb_fertilization_record` (
    `id`                 BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `farmland_id`        BIGINT        NOT NULL COMMENT '农田地块ID',
    `planting_record_id` BIGINT        DEFAULT NULL COMMENT '种植记录ID',
    `fertilizer_name`    VARCHAR(100)  NOT NULL COMMENT '肥料名称',
    `fertilizer_type`    VARCHAR(50)   DEFAULT NULL COMMENT '肥料类型: ORGANIC-有机肥, CHEMICAL-化肥, COMPOUND-复合肥',
    `apply_date`         DATETIME      NOT NULL COMMENT '施肥时间',
    `amount`             DECIMAL(10,2) NOT NULL COMMENT '用量(kg)',
    `apply_method`       VARCHAR(50)   DEFAULT NULL COMMENT '施肥方法',
    `operator`           VARCHAR(50)   DEFAULT NULL COMMENT '操作人',
    `description`        TEXT          DEFAULT NULL COMMENT '描述',
    `created_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_farmland_id` (`farmland_id`),
    KEY `idx_planting_record_id` (`planting_record_id`),
    KEY `idx_apply_date` (`apply_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='施肥记录表';

-- 3.6 病虫害监测表
DROP TABLE IF EXISTS `tb_pest_monitor`;
CREATE TABLE `tb_pest_monitor` (
    `id`                 BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `farmland_id`        BIGINT        NOT NULL COMMENT '农田地块ID',
    `planting_record_id` BIGINT        DEFAULT NULL COMMENT '种植记录ID',
    `pest_name`          VARCHAR(100)  NOT NULL COMMENT '病虫害名称',
    `pest_type`          VARCHAR(50)   DEFAULT NULL COMMENT '类型: INSECT-虫害, DISEASE-病害, WEED-草害',
    `severity`           VARCHAR(20)   DEFAULT NULL COMMENT '严重程度: LIGHT-轻度, MODERATE-中度, SEVERE-重度',
    `found_date`         DATE          NOT NULL COMMENT '发现日期',
    `affected_area`      DECIMAL(10,2) DEFAULT NULL COMMENT '受影响面积(亩)',
    `image_url`          VARCHAR(255)  DEFAULT NULL COMMENT '图片URL',
    `description`        TEXT          DEFAULT NULL COMMENT '描述',
    `created_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_farmland_id` (`farmland_id`),
    KEY `idx_planting_record_id` (`planting_record_id`),
    KEY `idx_found_date` (`found_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='病虫害监测表';

-- 3.7 病虫害防治表
DROP TABLE IF EXISTS `tb_pest_control`;
CREATE TABLE `tb_pest_control` (
    `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `pest_monitor_id` BIGINT        NOT NULL COMMENT '病虫害监测ID',
    `control_method`  VARCHAR(100)  DEFAULT NULL COMMENT '防治方法',
    `pesticide_name`  VARCHAR(100)  DEFAULT NULL COMMENT '农药名称',
    `dosage`          DECIMAL(10,2) DEFAULT NULL COMMENT '用量',
    `control_date`    DATETIME      NOT NULL COMMENT '防治时间',
    `effect`          VARCHAR(50)   DEFAULT NULL COMMENT '效果: EXCELLENT-极好, GOOD-好, FAIR-一般, POOR-差',
    `operator`        VARCHAR(50)   DEFAULT NULL COMMENT '操作人',
    `cost`            DECIMAL(10,2) DEFAULT NULL COMMENT '防治成本(元)',
    `description`     TEXT          DEFAULT NULL COMMENT '描述',
    `created_time`    DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`    DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_pest_monitor_id` (`pest_monitor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='病虫害防治表';

-- 3.8 气象数据表
DROP TABLE IF EXISTS `tb_weather_data`;
CREATE TABLE `tb_weather_data` (
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `farmland_id`      BIGINT        NOT NULL COMMENT '农田地块ID',
    `record_date`      DATE          NOT NULL COMMENT '记录日期',
    `temperature_max`  DECIMAL(5,2)  DEFAULT NULL COMMENT '最高温度(℃)',
    `temperature_min`  DECIMAL(5,2)  DEFAULT NULL COMMENT '最低温度(℃)',
    `humidity`         DECIMAL(5,2)  DEFAULT NULL COMMENT '湿度(%)',
    `rainfall`         DECIMAL(10,2) DEFAULT NULL COMMENT '降雨量(mm)',
    `wind_speed`       DECIMAL(5,2)  DEFAULT NULL COMMENT '风速(m/s)',
    `wind_direction`   VARCHAR(20)   DEFAULT NULL COMMENT '风向',
    `weather_type`     VARCHAR(50)   DEFAULT NULL COMMENT '天气类型: SUNNY-晴, CLOUDY-多云, RAINY-雨, SNOWY-雪, STORMY-暴风',
    `disaster_warning` VARCHAR(100)  DEFAULT NULL COMMENT '灾害预警',
    `created_time`     DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`     DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`          TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_farmland_id` (`farmland_id`),
    KEY `idx_record_date` (`record_date`),
    UNIQUE KEY `uk_farmland_date` (`farmland_id`, `record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='气象数据表';

-- 3.9 产量预测表
DROP TABLE IF EXISTS `tb_yield_prediction`;
CREATE TABLE `tb_yield_prediction` (
    `id`                 BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `farmland_id`        BIGINT        NOT NULL COMMENT '农田地块ID',
    `planting_record_id` BIGINT        DEFAULT NULL COMMENT '种植记录ID',
    `crop_type_id`       BIGINT        NOT NULL COMMENT '作物类型ID',
    `predict_year`       INT           NOT NULL COMMENT '预测年份',
    `predict_yield`      DECIMAL(10,2) DEFAULT NULL COMMENT '预测产量(kg/亩)',
    `actual_yield`       DECIMAL(10,2) DEFAULT NULL COMMENT '实际产量(kg/亩)',
    `predict_model`      VARCHAR(100)  DEFAULT NULL COMMENT '预测模型',
    `confidence`         DECIMAL(5,2)  DEFAULT NULL COMMENT '置信度',
    `factors`            TEXT          DEFAULT NULL COMMENT '影响因素(JSON)',
    `created_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            TINYINT       DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_farmland_id` (`farmland_id`),
    KEY `idx_planting_record_id` (`planting_record_id`),
    KEY `idx_crop_type_id` (`crop_type_id`),
    KEY `idx_predict_year` (`predict_year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产量预测表';

-- ----------------------------------------------------------------------------
-- 4. 插入测试数据
-- ----------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- 4.1 系统用户 (密码: 123456, BCrypt加密)
-- ----------------------------------------------------------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `status`, `last_login_time`) VALUES
(1, 'admin', '$2a$10$zG1pGmYLMnR4OQfKp0yMw.H6C5q.4c7q0eOqYZUZqL7u8pXPG5T7u', '系统管理员', 'https://api.dicebear.com/7.x/initials/svg?seed=admin', '13837001001', 'admin@shangqiu.gov.cn', 1, 1, '2024-06-15 08:30:00'),
(2, 'user',  '$2a$10$zG1pGmYLMnR4OQfKp0yMw.H6C5q.4c7q0eOqYZUZqL7u8pXPG5T7u', '普通用户',   'https://api.dicebear.com/7.x/initials/svg?seed=user',  '13837001002', 'user@shangqiu.gov.cn',  2, 1, '2024-06-14 17:20:00');

-- ----------------------------------------------------------------------------
-- 4.2 系统角色
-- ----------------------------------------------------------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `sort_order`, `status`) VALUES
(1, '系统管理员', 'ADMIN',   '系统最高权限，管理所有功能', 1, 1),
(2, '普通用户',   'USER',    '普通用户，查看农田信息',     2, 1),
(3, '农田管理员', 'MANAGER', '农田管理员，管理农田日常作业', 3, 1);

-- ----------------------------------------------------------------------------
-- 4.3 系统菜单 (9个一级目录 + 系统管理下4个子菜单 + 4个按钮权限)
-- ----------------------------------------------------------------------------
-- 一级目录 (CATALOG)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `icon`, `sort_order`, `permission`, `status`) VALUES
(1,  0, '数据看板',   'CATALOG', '/dashboard',    'dashboard/index',      'DataBoard',   1, NULL, 1),
(2,  0, '农田管理',   'CATALOG', '/farmland',     NULL,                   'Grid',        2, NULL, 1),
(3,  0, '种植管理',   'CATALOG', '/planting',     NULL,                   'Plant',       3, NULL, 1),
(4,  0, '灌溉管理',   'CATALOG', '/irrigation',   NULL,                   'Umbrella',    4, NULL, 1),
(5,  0, '施肥管理',   'CATALOG', '/fertilization',NULL,                   'SetUp',       5, NULL, 1),
(6,  0, '病虫害防治', 'CATALOG', '/pest',         NULL,                   'Warning',     6, NULL, 1),
(7,  0, '气象监测',   'CATALOG', '/weather',      NULL,                   'Sunny',       7, NULL, 1),
(8,  0, '产量分析',   'CATALOG', '/yield',        NULL,                   'TrendCharts', 8, NULL, 1),
(9,  0, '系统管理',   'CATALOG', '/system',       NULL,                   'Setting',     9, NULL, 1);

-- 系统管理 --> 子菜单 (MENU)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `icon`, `sort_order`, `permission`, `status`) VALUES
(10, 9, '用户管理', 'MENU', '/system/user', 'system/user/index', 'User',   1, 'sys:user:list', 1),
(11, 9, '角色管理', 'MENU', '/system/role', 'system/role/index', 'Avatar', 2, 'sys:role:list', 1),
(12, 9, '菜单管理', 'MENU', '/system/menu', 'system/menu/index', 'Menu',   3, 'sys:menu:list', 1),
(13, 9, '操作日志', 'MENU', '/system/log',  'system/log/index',  'Document',4, 'sys:log:list',  1);

-- 用户管理 --> 按钮权限 (BUTTON)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `icon`, `sort_order`, `permission`, `status`) VALUES
(14, 10, '新增用户', 'BUTTON', NULL, NULL, NULL, 1, 'sys:user:add',    1),
(15, 10, '编辑用户', 'BUTTON', NULL, NULL, NULL, 2, 'sys:user:edit',   1),
(16, 10, '删除用户', 'BUTTON', NULL, NULL, NULL, 3, 'sys:user:delete', 1);

-- ----------------------------------------------------------------------------
-- 4.4 用户角色关联 (admin -> ADMIN 角色)
-- ----------------------------------------------------------------------------
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES
(1, 1, 1),
(2, 2, 2);

-- ----------------------------------------------------------------------------
-- 4.5 角色菜单关联 (ADMIN角色拥有所有菜单权限)
-- ----------------------------------------------------------------------------
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES
(1,  1, 1),  (2,  1, 2),  (3,  1, 3),  (4,  1, 4),  (5,  1, 5),
(6,  1, 6),  (7,  1, 7),  (8,  1, 8),  (9,  1, 9),  (10, 1, 10),
(11, 1, 11), (12, 1, 12), (13, 1, 13), (14, 1, 14), (15, 1, 15),
(16, 1, 16);

-- USER角色拥有查看权限
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES
(17, 2, 1),  (18, 2, 2),  (19, 2, 3),  (20, 2, 4),  (21, 2, 5),
(22, 2, 6),  (23, 2, 7),  (24, 2, 8),  (25, 2, 9),  (26, 2, 10),
(27, 2, 11), (28, 2, 12), (29, 2, 13);

-- MANAGER角色拥有业务模块权限
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES
(30, 3, 1),  (31, 3, 2),  (32, 3, 3),  (33, 3, 4),  (34, 3, 5),
(35, 3, 6),  (36, 3, 7),  (37, 3, 8);

-- ----------------------------------------------------------------------------
-- 4.6 操作日志
-- ----------------------------------------------------------------------------
INSERT INTO `tb_operation_log` (`id`, `user_id`, `username`, `operation`, `method`, `params`, `ip`, `execution_time`, `created_time`) VALUES
(1, 1, 'admin', '用户登录',       'POST /api/login',          '{"username":"admin"}',         '192.168.1.100', 120, '2024-06-15 08:30:00'),
(2, 1, 'admin', '新增农田地块',   'POST /api/farmland',       '{"farmlandCode":"411402001"}', '192.168.1.100',  85, '2024-06-15 09:15:00'),
(3, 1, 'admin', '新增角色',       'POST /api/role',           '{"roleCode":"MANAGER"}',       '192.168.1.100',  45, '2024-06-15 10:30:00'),
(4, 2, 'user',  '查看农田列表',   'GET /api/farmland/list',   NULL,                           '192.168.1.101',  65, '2024-06-15 14:20:00');

-- ----------------------------------------------------------------------------
-- 4.7 农田地块数据 (5条 - 商丘市不同区县)
-- 商丘市坐标范围: 约 115.0°E ~ 116.5°E, 33.7°N ~ 34.6°N
-- ----------------------------------------------------------------------------
INSERT INTO `tb_farmland` (`id`, `farmland_code`, `farmland_name`, `area`, `location`, `longitude`, `latitude`, `soil_type`, `soil_ph`, `owner_name`, `owner_phone`, `status`, `description`) VALUES
(1, '411402001', '梁园区李庄乡高标准农田A区', 350.00, '河南省商丘市梁园区李庄乡',  '115.6582', '34.4483', '潮土',  7.20, '张建国', '13837010001', 'USE',    '梁园区核心高标准农田示范区，土壤肥沃，配套灌溉设施完善，适合冬小麦-夏玉米轮作。'),
(2, '411402002', '睢阳区坞墙镇基本农田B区', 200.00, '河南省商丘市睢阳区坞墙镇',  '115.6205', '34.3521', '砂姜黑土', 6.80, '李明辉', '13837010002', 'USE',    '睢阳区高标准基本农田，砂姜黑土改良区，排水条件良好。'),
(3, '411402003', '民权县林七乡生态农田C区', 500.00, '河南省商丘市民权县林七乡',  '115.1758', '34.7289', '沙土',  7.50, '王建设', '13837010003', 'USE',    '民权县黄河故道生态农田，面积广阔，适合花生、棉花等经济作物种植。'),
(4, '411402004', '柘城县张桥镇高标准农田D区', 120.00, '河南省商丘市柘城县张桥镇',  '115.3421', '34.0987', '两合土', 7.10, '赵志强', '13837010004', 'USE',    '柘城县优质小麦生产基地，两合土土壤保水保肥能力强。'),
(5, '411402005', '虞城县站集镇粮食主产区E区', 280.00, '河南省商丘市虞城县站集镇',  '115.8289', '34.3812', '淤土',  7.00, '刘永刚', '13837010005', 'USE',    '虞城县粮食主产区核心地块，淤土富含有机质，主要种植冬小麦和大豆。');

-- ----------------------------------------------------------------------------
-- 4.8 作物类型数据 (5条)
-- ----------------------------------------------------------------------------
INSERT INTO `tb_crop_type` (`id`, `crop_name`, `crop_code`, `category`, `growth_period`, `suitable_soil`, `suitable_temp`, `description`) VALUES
(1, '冬小麦', 'WINTER_WHEAT',  '粮食作物', 230, '潮土、砂姜黑土、两合土', '15-25℃',     '商丘地区主要越冬粮食作物，10月播种，次年6月收获，耐寒性强。'),
(2, '夏玉米', 'SUMMER_CORN',   '粮食作物', 110, '潮土、壤土、砂壤土',     '20-30℃',     '商丘地区主要秋粮作物，6月麦收后播种，9-10月收获。'),
(3, '大豆',   'SOYBEAN',       '油料作物', 120, '砂壤土、潮土、两合土',   '20-28℃',     '优质高蛋白大豆品种，适宜与冬小麦轮作，固氮养地。'),
(4, '棉花',   'COTTON',        '经济作物', 160, '沙土、砂壤土、潮土',     '25-30℃',     '黄河故道区域传统经济作物，4月播种，9-10月采收。'),
(5, '花生',   'PEANUT',        '油料作物', 130, '沙土、砂壤土',           '22-30℃',     '商丘黄河故道优质花生，沙质土壤适宜花生荚果发育。');

-- ----------------------------------------------------------------------------
-- 4.9 种植记录数据 (10条)
-- ----------------------------------------------------------------------------
INSERT INTO `tb_planting_record` (`id`, `farmland_id`, `crop_type_id`, `plant_date`, `expect_harvest_date`, `actual_harvest_date`, `plant_area`, `seed_amount`, `status`, `description`) VALUES
-- 冬小麦种植 (2023年秋季播种，2024年夏季收获)
(1,  1, 1, '2023-10-15', '2024-06-01', '2024-06-05', 300.00, 7500.00, 'HARVESTED', '梁园区李庄乡冬小麦，品种：郑麦9023，亩产约550kg。'),
(2,  4, 1, '2023-10-18', '2024-06-03', '2024-06-08', 120.00, 3000.00, 'HARVESTED', '柘城县冬小麦，品种：矮抗58，亩产约520kg。'),
(3,  5, 1, '2023-10-20', '2024-06-05', '2024-06-10', 250.00, 6250.00, 'HARVESTED', '虞城县冬小麦，品种：郑麦7698，亩产约530kg。'),

-- 夏玉米种植 (2024年夏季播种)
(4,  1, 2, '2024-06-10', '2024-09-25', NULL,        300.00, 900.00,  'GROWING',   '梁园区夏玉米，品种：郑单958，麦收后抢茬播种。'),
(5,  2, 2, '2024-06-12', '2024-09-27', NULL,        180.00, 540.00,  'GROWING',   '睢阳区夏玉米，品种：先玉335，宽窄行种植。'),
(6,  4, 2, '2024-06-15', '2024-09-30', NULL,        100.00, 300.00,  'GROWING',   '柘城县夏玉米，品种：郑单958，麦茬直播。'),

-- 大豆种植
(7,  5, 3, '2024-06-08', '2024-10-05', NULL,        30.00,  150.00,  'GROWING',   '虞城县大豆，品种：豫豆22，麦茬免耕精量播种。'),

-- 棉花种植
(8,  3, 4, '2024-04-15', '2024-10-01', NULL,        200.00, 400.00,  'GROWING',   '民权县棉花，品种：鲁棉研28，地膜覆盖栽培。'),

-- 花生种植
(9,  3, 5, '2024-05-01', '2024-09-10', NULL,        150.00, 3000.00, 'GROWING',   '民权县花生，品种：豫花37，起垄覆膜种植。'),

-- 冬小麦种植 (2024年秋季)
(10, 2, 1, '2024-10-12', '2025-06-01', NULL,        200.00, 5000.00, 'PLANTING',  '睢阳区冬小麦，品种：郑麦9023，深耕整地播种。');

-- ----------------------------------------------------------------------------
-- 4.10 灌溉记录数据 (15条)
-- ----------------------------------------------------------------------------
INSERT INTO `tb_irrigation_record` (`id`, `farmland_id`, `planting_record_id`, `irrigate_date`, `water_amount`, `irrigate_method`, `irrigate_duration`, `operator`, `description`) VALUES
-- 冬小麦灌溉 (越冬水、返青水、拔节水、灌浆水)
(1,  1, 1, '2023-11-20 09:00:00', 1800.00, 'SPRINKLER', 240, '张建国', '冬小麦越冬水灌溉，确保麦苗安全越冬。'),
(2,  1, 1, '2024-03-05 08:00:00', 1500.00, 'SPRINKLER', 210, '张建国', '冬小麦返青水灌溉，促进春季分蘖。'),
(3,  1, 1, '2024-04-10 07:00:00', 1650.00, 'SPRINKLER', 225, '张建国', '冬小麦拔节水灌溉，配合追肥。'),
(4,  4, 2, '2024-03-08 09:00:00', 720.00,  'DRIP',      180, '赵志强', '柘城县冬小麦返青水，滴灌精准施水。'),
(5,  4, 2, '2024-04-15 08:30:00', 600.00,  'DRIP',      150, '赵志强', '冬小麦拔节孕穗水灌溉。'),
(6,  5, 3, '2024-03-10 10:00:00', 1500.00, 'FLOOD',     120, '刘永刚', '虞城县冬小麦返青漫灌。'),
(7,  5, 3, '2024-04-18 08:00:00', 1375.00, 'FLOOD',     110, '刘永刚', '冬小麦灌浆水灌溉。'),

-- 夏玉米灌溉
(8,  1, 4, '2024-06-15 16:00:00', 1200.00, 'SPRINKLER', 180, '张建国', '夏玉米出苗水，确保苗齐苗壮。'),
(9,  2, 5, '2024-06-20 09:00:00', 900.00,  'DRIP',      160, '李明辉', '夏玉米苗期灌溉。'),
(10, 2, 5, '2024-06-25 10:00:00', 600.00,  'DRIP',      130, '李明辉', '夏玉米苗期第二次灌溉。'),
(11, 4, 6, '2024-06-22 07:30:00', 500.00,  'DRIP',      140, '赵志强', '柘城县夏玉米苗期灌溉。'),

-- 棉花灌溉
(12, 3, 8, '2024-05-10 08:00:00', 1200.00, 'DRIP',      240, '王建设', '棉花苗期滴灌。'),
(13, 3, 8, '2024-06-05 09:00:00', 1400.00, 'DRIP',      210, '王建设', '棉花蕾期灌溉。'),
(14, 3, 8, '2024-06-20 08:00:00', 1600.00, 'DRIP',      240, '王建设', '棉花花铃期灌溉。'),

-- 大豆灌溉
(15, 5, 7, '2024-06-18 09:00:00', 180.00,  'FLOOD',      60, '刘永刚', '大豆苗期漫灌。');

-- ----------------------------------------------------------------------------
-- 4.11 施肥记录数据 (10条)
-- ----------------------------------------------------------------------------
INSERT INTO `tb_fertilization_record` (`id`, `farmland_id`, `planting_record_id`, `fertilizer_name`, `fertilizer_type`, `apply_date`, `amount`, `apply_method`, `operator`, `description`) VALUES
-- 冬小麦施肥
(1,  1, 1, '复合肥(15-15-15)', 'COMPOUND',  '2023-10-10 08:00:00', 15000.00, '基施',   '张建国', '冬小麦播种前基肥，复合肥配合深耕翻入。'),
(2,  1, 1, '尿素',             'CHEMICAL',  '2024-03-05 09:00:00', 4500.00,  '追施',   '张建国', '冬小麦返青期追肥，尿素配合返青水施用。'),
(3,  4, 2, '有机肥(腐熟鸡粪)', 'ORGANIC',   '2023-10-05 08:00:00', 24000.00, '基施',   '赵志强', '柘城县冬小麦有机肥基施，改良砂姜黑土。'),
(4,  5, 3, '复合肥(18-12-10)', 'COMPOUND',  '2023-10-15 08:00:00', 12500.00, '基施',   '刘永刚', '虞城县冬小麦基肥。'),

-- 夏玉米施肥
(5,  1, 4, '复合肥(28-6-6)',   'COMPOUND',  '2024-06-10 10:00:00', 9000.00,  '种肥同播','张建国', '夏玉米种肥同播，高氮复合肥。'),
(6,  2, 5, '尿素',             'CHEMICAL',  '2024-06-20 09:00:00', 3600.00,  '追施',   '李明辉', '夏玉米苗期追肥。'),

-- 棉花施肥
(7,  3, 8, '有机肥(腐熟羊粪)', 'ORGANIC',   '2024-04-10 08:00:00', 40000.00, '基施',   '王建设', '棉花有机肥基施。'),
(8,  3, 8, '磷酸二铵',         'CHEMICAL',  '2024-06-05 09:00:00', 4000.00,  '追施',   '王建设', '棉花蕾期追施磷肥。'),

-- 花生施肥
(9,  3, 9, '复合肥(12-15-18)', 'COMPOUND',  '2024-04-28 08:00:00', 7500.00,  '基施',   '王建设', '花生播种前基肥。'),

-- 大豆施肥
(10, 5, 7, '磷酸二铵',         'CHEMICAL',  '2024-06-08 10:00:00', 600.00,   '种肥','刘永刚', '大豆种肥，少量磷肥促根瘤。');

-- ----------------------------------------------------------------------------
-- 4.12 病虫害监测数据 (8条)
-- ----------------------------------------------------------------------------
INSERT INTO `tb_pest_monitor` (`id`, `farmland_id`, `planting_record_id`, `pest_name`, `pest_type`, `severity`, `found_date`, `affected_area`, `description`) VALUES
-- 冬小麦病虫害
(1, 1, 1, '小麦条锈病', 'DISEASE', 'MODERATE', '2024-04-20', 80.00,  '梁园区冬小麦条锈病，叶片出现夏孢子堆，中等程度发生。'),
(2, 4, 2, '麦蚜',       'INSECT',  'LIGHT',    '2024-05-05', 25.00,  '柘城县冬小麦穗期蚜虫，轻度发生，及时防治。'),
(3, 5, 3, '小麦赤霉病', 'DISEASE', 'LIGHT',    '2024-05-10', 35.00,  '虞城县冬小麦扬花期赤霉病，轻度发生。'),

-- 夏玉米病虫害
(4, 1, 4, '玉米螟',     'INSECT',  'MODERATE', '2024-06-20', 60.00,  '梁园区夏玉米苗期玉米螟危害，中度发生。'),
(5, 2, 5, '田间杂草',   'WEED',    'MODERATE', '2024-06-25', 50.00,  '睢阳区夏玉米田杂草滋生，以马唐、狗尾草为主。'),

-- 棉花病虫害
(6, 3, 8, '棉铃虫',     'INSECT',  'SEVERE',   '2024-06-10', 120.00, '民权县棉花二代棉铃虫，重度发生，需紧急防治。'),
(7, 3, 8, '棉花枯萎病', 'DISEASE', 'LIGHT',    '2024-06-15', 30.00,  '民权县棉花枯萎病，零星发生。'),

-- 花生病虫害
(8, 3, 9, '花生蚜虫',   'INSECT',  'MODERATE', '2024-06-01', 40.00,  '民权县花生苗期蚜虫，中度发生。');

-- ----------------------------------------------------------------------------
-- 4.13 病虫害防治数据 (5条 - 对应上述监测记录)
-- ----------------------------------------------------------------------------
INSERT INTO `tb_pest_control` (`id`, `pest_monitor_id`, `control_method`, `pesticide_name`, `dosage`, `control_date`, `effect`, `operator`, `cost`, `description`) VALUES
(1, 1, '化学防治-喷雾', '戊唑醇悬浮剂',   15.00,  '2024-04-22 09:00:00', 'GOOD',      '张建国',  2400.00, '冬小麦条锈病喷药防治，戊唑醇每亩15ml兑水30kg喷雾。'),
(2, 2, '化学防治-喷雾', '吡虫啉可湿性粉剂', 10.00, '2024-05-06 08:30:00', 'EXCELLENT', '赵志强',  750.00,  '冬小麦蚜虫防治，吡虫啉每亩10g兑水喷雾。'),
(3, 3, '化学防治-喷雾', '戊唑·咪鲜胺',    40.00,  '2024-05-12 09:00:00', 'GOOD',      '刘永刚',  1050.00, '冬小麦赤霉病防治。'),
(4, 4, '生物防治+化学防治', '苏云金杆菌+高效氯氟氰菊酯', 30.00, '2024-06-22 08:00:00', 'FAIR', '张建国', 1800.00, '夏玉米玉米螟综合防治，Bt生物制剂配合低毒化学农药。'),
(5, 6, '化学防治-喷雾', '甲维盐·茚虫威', 20.00,  '2024-06-12 07:00:00', 'GOOD',      '王建设',  4800.00, '棉花二代棉铃虫应急防治，每亩20ml兑水40kg喷雾。');

-- ----------------------------------------------------------------------------
-- 4.14 气象数据 (30条 - 2024年1月至6月，每月5天抽样)
-- 商丘市属暖温带半湿润大陆性季风气候，冬季寒冷干燥，夏季高温多雨
-- ----------------------------------------------------------------------------
INSERT INTO `tb_weather_data` (`id`, `farmland_id`, `record_date`, `temperature_max`, `temperature_min`, `humidity`, `rainfall`, `wind_speed`, `wind_direction`, `weather_type`, `disaster_warning`) VALUES
-- 2024年1月 (冬季，梁园区)
(1,  1, '2024-01-05',  4.50,  -5.20, 45.00, 0.00,  3.20, '北风',     'SUNNY',  NULL),
(2,  1, '2024-01-10',  2.80,  -6.50, 52.00, 0.00,  4.10, '西北风',   'CLOUDY', NULL),
(3,  1, '2024-01-15',  1.20,  -8.00, 58.00, 2.50,  5.00, '北风',     'SNOWY',  '寒潮蓝色预警'),
(4,  1, '2024-01-20',  5.00,  -3.50, 48.00, 0.00,  2.80, '北风',     'SUNNY',  NULL),
(5,  1, '2024-01-25',  6.80,  -2.00, 42.00, 0.00,  3.50, '西南风',   'CLOUDY', NULL),

-- 2024年2月 (梁园区)
(6,  1, '2024-02-05',  8.20,  -1.50, 55.00, 5.20,  3.00, '东风',     'RAINY',  NULL),
(7,  1, '2024-02-10', 10.50,   0.50, 50.00, 0.00,  2.50, '南风',     'SUNNY',  NULL),
(8,  1, '2024-02-15',  7.00,  -3.00, 60.00, 0.00,  4.50, '北风',     'SNOWY',  '道路结冰黄色预警'),
(9,  1, '2024-02-20', 11.00,   2.00, 47.00, 0.00,  3.80, '西南风',   'SUNNY',  NULL),
(10, 1, '2024-02-25', 12.50,   3.50, 44.00, 0.00,  2.90, '南风',     'CLOUDY', NULL),

-- 2024年3月 (柘城县)
(11, 4, '2024-03-05', 15.00,   4.00, 50.00, 0.00,  3.50, '东南风',   'CLOUDY', NULL),
(12, 4, '2024-03-10', 18.20,   6.50, 42.00, 0.00,  4.00, '南风',     'SUNNY',  NULL),
(13, 4, '2024-03-15', 16.50,   7.00, 55.00, 8.30,  3.20, '东风',     'RAINY',  NULL),
(14, 4, '2024-03-20', 20.00,   8.50, 40.00, 0.00,  3.60, '西南风',   'SUNNY',  NULL),
(15, 4, '2024-03-25', 22.50,  10.00, 38.00, 0.00,  4.20, '南风',     'SUNNY',  NULL),

-- 2024年4月 (民权县)
(16, 3, '2024-04-05', 23.00,  11.00, 45.00, 0.00,  3.80, '南风',     'CLOUDY', NULL),
(17, 3, '2024-04-10', 25.50,  13.50, 48.00, 12.40, 4.50, '东南风',   'RAINY',  NULL),
(18, 3, '2024-04-15', 26.00,  14.00, 42.00, 0.00,  3.20, '西南风',   'SUNNY',  NULL),
(19, 3, '2024-04-20', 21.50,  12.00, 62.00, 18.50, 5.80, '东北风',   'RAINY',  '大风蓝色预警'),
(20, 3, '2024-04-25', 28.00,  15.50, 40.00, 0.00,  3.10, '南风',     'SUNNY',  NULL),

-- 2024年5月 (虞城县)
(21, 5, '2024-05-05', 30.50,  18.00, 52.00, 0.00,  3.50, '东南风',   'CLOUDY', NULL),
(22, 5, '2024-05-10', 28.00,  19.50, 68.00, 25.60, 4.80, '东风',     'RAINY',  '暴雨黄色预警'),
(23, 5, '2024-05-15', 32.00,  20.00, 45.00, 0.00,  3.00, '南风',     'SUNNY',  NULL),
(24, 5, '2024-05-20', 33.50,  21.50, 50.00, 3.20,  3.40, '东南风',   'RAINY',  NULL),
(25, 5, '2024-05-25', 35.00,  22.00, 42.00, 0.00,  2.80, '南风',     'SUNNY',  '高温橙色预警'),

-- 2024年6月 (睢阳区)
(26, 2, '2024-06-05', 36.50,  24.00, 55.00, 0.00,  3.00, '南风',     'SUNNY',  '高温橙色预警'),
(27, 2, '2024-06-10', 38.00,  25.50, 48.00, 0.00,  2.50, '西南风',   'SUNNY',  '高温红色预警'),
(28, 2, '2024-06-15', 34.00,  23.00, 65.00, 35.80, 5.50, '东风',     'STORMY', '暴雨橙色预警'),
(29, 2, '2024-06-20', 32.00,  22.50, 58.00, 8.50,  3.80, '东南风',   'RAINY',  NULL),
(30, 2, '2024-06-25', 35.50,  24.00, 50.00, 0.00,  2.90, '南风',     'SUNNY',  NULL);

-- ----------------------------------------------------------------------------
-- 4.15 产量预测数据 (5条)
-- ----------------------------------------------------------------------------
INSERT INTO `tb_yield_prediction` (`id`, `farmland_id`, `planting_record_id`, `crop_type_id`, `predict_year`, `predict_yield`, `actual_yield`, `predict_model`, `confidence`, `factors`) VALUES
-- 冬小麦产量预测
(1, 1, 1, 1, 2024, 540.00, 550.00, '多元线性回归模型', 0.89, '{"soil_ph":7.2,"rainfall_spring":156.4,"fertilizer_total":19500,"irrigation_times":3,"temperature_spring_avg":18.5}'),
(2, 4, 2, 1, 2024, 510.00, 520.00, '多元线性回归模型', 0.87, '{"soil_ph":7.1,"rainfall_spring":148.2,"fertilizer_total":24000,"irrigation_times":2,"temperature_spring_avg":18.5}'),
(3, 5, 3, 1, 2024, 520.00, 530.00, '随机森林预测模型', 0.91, '{"soil_ph":7.0,"rainfall_spring":152.0,"fertilizer_total":13100,"irrigation_times":2,"temperature_spring_avg":18.5}'),

-- 夏玉米产量预测 (基于历史数据预估)
(4, 1, 4, 2, 2024, 620.00, NULL,    '随机森林预测模型', 0.85, '{"soil_ph":7.2,"rainfall_summer":194.5,"fertilizer_total":12600,"irrigation_times":1,"temperature_summer_avg":31.2}'),
(5, 2, 5, 2, 2024, 600.00, NULL,    '多元线性回归模型', 0.82, '{"soil_ph":6.8,"rainfall_summer":194.5,"fertilizer_total":3600,"irrigation_times":2,"temperature_summer_avg":31.2}');
