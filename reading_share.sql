-- =============================================================
-- 读书笔记分享系统 —— 数据库一键初始化脚本（整合版）
-- 数据库：reading_share        适用：MySQL 5.7 / 8.0
-- 字符集：utf8mb4
--
-- 【整合说明】本文件合并了原先 4 个脚本，执行一次即可完成建库建表灌数据：
--     reading_share.sql   建库 + 7 张表结构        -> 第 1、2 节
--     book_audit.sql      书籍审核三字段          -> 已合并进 book 建表语句
--     cover_status.sql    封面审核三字段          -> 已合并进 book 建表语句
--     test_data.sql       演示数据                -> 第 3 节（并补全了缺失的 user/book 数据）
--
-- 【重要】本脚本包含 DROP TABLE，会清空 7 张表的原有数据，请先备份。
--         备份：mysqldump -uroot -p --default-character-set=utf8mb4 reading_share > backup.sql
--
-- 【执行方式】
--     mysql -uroot -p --default-character-set=utf8mb4 < reading_share.sql
--   或在 Navicat / IDEA Database 中打开本文件整体执行。
--
-- 【测试账号】
--     管理员   admin    / admin123
--     普通用户 zhangsan / 123456   （李四 lisi、王五 wangwu、test1 密码同为 123456）
-- =============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================================
-- 1. 创建数据库
-- =============================================================
CREATE DATABASE IF NOT EXISTS `reading_share`
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `reading_share`;

-- =============================================================
-- 2. 删除旧表（按外键依赖逆序，保证可重复执行）
-- =============================================================
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `collect`;
DROP TABLE IF EXISTS `note`;
DROP TABLE IF EXISTS `list_book`;
DROP TABLE IF EXISTS `book_list`;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `user`;

-- =============================================================
-- 3. 建表
-- =============================================================

-- 3.1 用户表：role 0=普通用户 1=管理员
CREATE TABLE `user` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `username`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录名',
    `password`    VARCHAR(64)  NOT NULL COMMENT 'MD5加密后的密码（32位大写）',
    `nickname`    VARCHAR(50)  NOT NULL COMMENT '昵称',
    `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像文件名',
    `role`        INT NOT NULL DEFAULT 0 COMMENT '0普通用户 1管理员',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 3.2 书籍表：两套独立审核
--     book_status  书籍审核：pending待审 / pass公开 / reject驳回
--     cover_status 封面审核：pending待审 / pass通过 / reject驳回（与书籍审核互不干扰）
--     user_id 分享者id，书籍审核通过时 = submit_user_id，封面/书单权限以此判定
CREATE TABLE `book` (
    `id`             INT AUTO_INCREMENT PRIMARY KEY,
    `name`           VARCHAR(100) NOT NULL COMMENT '书名',
    `author`         VARCHAR(100) NOT NULL COMMENT '作者',
    `isbn`           VARCHAR(20)  DEFAULT NULL COMMENT 'ISBN',
    `cover`          VARCHAR(255) DEFAULT NULL COMMENT '封面图片文件名',
    `intro`          TEXT COMMENT '简介',
    `submit_user_id` INT DEFAULT NULL COMMENT '提交人id',
    `book_status`    VARCHAR(10) NOT NULL DEFAULT 'pending' COMMENT 'pending待审核 pass通过 reject驳回',
    `user_id`        INT DEFAULT NULL COMMENT '分享者id（审核通过时=提交人id）',
    `cover_status`   VARCHAR(10) NOT NULL DEFAULT 'pending' COMMENT 'pending待审核 pass通过 reject驳回',
    `cover_user_id`  INT DEFAULT NULL COMMENT '封面最后上传人id',
    `cover_time`     DATETIME DEFAULT NULL COMMENT '封面上传时间',
    `create_time`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY `idx_book_status` (`book_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

-- 3.3 书单表：is_public 1=公开 0=私有
CREATE TABLE `book_list` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     INT NOT NULL COMMENT '创建者id',
    `name`        VARCHAR(100) NOT NULL COMMENT '书单名称',
    `intro`       VARCHAR(500) DEFAULT NULL COMMENT '书单简介',
    `is_public`   INT NOT NULL DEFAULT 1 COMMENT '1公开 0私有',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_list_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书单表';

-- 3.4 书单-书籍 关联表
CREATE TABLE `list_book` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `list_id`     INT NOT NULL,
    `book_id`     INT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_list_book` (`list_id`,`book_id`),
    CONSTRAINT `fk_lb_list` FOREIGN KEY (`list_id`) REFERENCES `book_list`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_lb_book` FOREIGN KEY (`book_id`) REFERENCES `book`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书单书籍关联表';

-- 3.5 读书笔记表
CREATE TABLE `note` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     INT NOT NULL COMMENT '作者id',
    `book_id`     INT NOT NULL COMMENT '对应书籍id',
    `title`       VARCHAR(100) NOT NULL COMMENT '笔记标题',
    `content`     TEXT NOT NULL COMMENT '笔记内容',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_note_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    CONSTRAINT `fk_note_book` FOREIGN KEY (`book_id`) REFERENCES `book`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='读书笔记表';

-- 3.6 收藏表：target_type 1=书单 2=笔记
CREATE TABLE `collect` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     INT NOT NULL,
    `target_type` INT NOT NULL COMMENT '1书单 2笔记',
    `target_id`   INT NOT NULL COMMENT '书单id或笔记id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_collect` (`user_id`,`target_type`,`target_id`),
    CONSTRAINT `fk_collect_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 3.7 评论表（评论读书笔记）
CREATE TABLE `comment` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     INT NOT NULL,
    `note_id`     INT NOT NULL,
    `content`     VARCHAR(500) NOT NULL COMMENT '评论内容',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    CONSTRAINT `fk_comment_note` FOREIGN KEY (`note_id`) REFERENCES `note`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- =============================================================
-- 4. 初始化数据（按外键依赖顺序插入）
-- =============================================================

-- 4.1 用户
--     admin  密码 admin123
--     其余用户 密码 123456
INSERT INTO `user` (`id`,`username`,`password`,`nickname`,`avatar`,`role`) VALUES
(1, 'admin',    '0192023A7BBD73250516F069DF18B500', '管理员', NULL, 1),
(2, 'test1',    'E10ADC3949BA59ABBE56E057F20F883E', '111',    NULL, 0),
(3, 'zhangsan', 'E10ADC3949BA59ABBE56E057F20F883E', '张三',   NULL, 0),
(4, 'lisi',     'E10ADC3949BA59ABBE56E057F20F883E', '李四',   NULL, 0),
(5, 'wangwu',   'E10ADC3949BA59ABBE56E057F20F883E', '王五',   NULL, 0);

-- 4.2 书籍
--     1-6：已审核通过（pass），封面已审核通过，分享者为 admin
--     封面文件名对应 src/main/webapp/upload/ 下的真实图片
--     7  ：book_status=pending，用于演示「书籍审核」流程，前台列表不显示
INSERT INTO `book`
(`id`,`name`,`author`,`isbn`,`cover`,`intro`,`submit_user_id`,`book_status`,`user_id`,`cover_status`,`cover_user_id`,`cover_time`) VALUES
(1, '活着', '余华', '9787506365437', 'huozhe.jpg',
 '讲述农村人福贵一生的苦难与坚韧，是余华最著名的代表作之一。',
 1, 'pass', 1, 'pass', 1, NOW()),
(2, '百年孤独', '加西亚·马尔克斯', '9787544253994', 'bainiangudu.jpg',
 '布恩迪亚家族七代人的传奇故事，魔幻现实主义文学经典。',
 1, 'pass', 1, 'pass', 1, NOW()),
(3, '三体', '刘慈欣', '9787536692930', 'santi.jpg',
 '地球文明与三体文明的生死博弈，中国科幻里程碑之作。',
 1, 'pass', 1, 'pass', 1, NOW()),
(4, '平凡的世界', '路遥', '9787530216781', 'pingfanshijie.jpg',
 '以孙少安、孙少平兄弟为主线，展现普通人在时代变迁中的奋斗。',
 1, 'pass', 1, 'pass', 1, NOW()),
(5, '围城', '钱钟书', '9787030100019', 'weicheng.jpg',
 '以方鸿渐的婚姻与事业为线索，刻画知识分子的众生相。',
 1, 'pass', 1, 'pass', 1, NOW()),
(6, '人类简史', '尤瓦尔·赫拉利', '9787508647357', 'renleijianshi.jpg',
 '从认知革命到科学革命，重新审视人类文明的演进历程。',
 1, 'pass', 1, 'pass', 1, NOW()),
(7, '小王子', '安托万・德・圣-埃克苏佩里', '9787020042490', NULL,
 '来自 B612 星球的小王子在星际旅行，遇见形形色色的大人，告诉我们用心才能看见事物本质。',
 2, 'pending', NULL, 'pending', NULL, NULL);

-- 4.3 书单（4 号为私有书单，用于演示 is_public 差异）
INSERT INTO `book_list` (`id`,`user_id`,`name`,`intro`,`is_public`) VALUES
(1, 3, '我的治愈书单',   '适合情绪低落的时候读，温柔有力量', 1),
(2, 4, '科幻爱好者合集', '硬核科幻，脑洞超大',               1),
(3, 5, '经典文学必看',   '文学入门，必读佳作',               1),
(4, 2, '闲来读书',       '随便读读，不求深刻，只为读书的乐趣', 0);

-- 4.4 书单-书籍关联
INSERT INTO `list_book` (`id`,`list_id`,`book_id`) VALUES
(1, 1, 1),
(2, 2, 3),
(3, 3, 2),
(4, 3, 4),
(5, 3, 5),
(6, 3, 6),
(7, 4, 6);

-- 4.5 读书笔记
INSERT INTO `note` (`id`,`user_id`,`book_id`,`title`,`content`) VALUES
(1, 3, 1, '活着读书笔记',
 '人是为活着本身而活着，而不是为活着之外的任何事物所活着。福贵一生饱经苦难，却依然平静地活下去，这本书让我重新思考生命的意义。'),
(2, 4, 3, '三体读后感',
 '宇宙浩瀚，人类渺小。黑暗森林法则是全书最震撼的设定，文明之间的猜忌与博弈，读完久久不能平静。'),
(3, 5, 2, '百年孤独摘抄感悟',
 '家族七代人循环往复的孤独，马孔多从繁荣到消散，孤独是所有人逃不开的宿命。'),
(4, 2, 5, '围城读后感',
 '很有年代文的感觉');

-- 4.6 收藏（target_type：1=书单 2=笔记）
INSERT INTO `collect` (`id`,`user_id`,`target_type`,`target_id`) VALUES
(1, 3, 1, 2),
(2, 4, 2, 1),
(3, 5, 1, 1);

-- 4.7 评论
INSERT INTO `comment` (`id`,`user_id`,`note_id`,`content`) VALUES
(1, 4, 1, '写得很有共鸣，活着这本书真的后劲很大。'),
(2, 3, 2, '黑暗森林那段确实震撼，值得反复回味。'),
(3, 4, 3, '孤独这个主题被写得太动人了。');

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================
-- 5. 执行结果校验
-- =============================================================
SELECT '用户' AS 表名, COUNT(*) AS 记录数 FROM `user`
UNION ALL SELECT '书籍（全部）',   COUNT(*) FROM `book`
UNION ALL SELECT '书籍（已公开）', COUNT(*) FROM `book`     WHERE `book_status`='pass'
UNION ALL SELECT '书籍（待审核）', COUNT(*) FROM `book`     WHERE `book_status`='pending'
UNION ALL SELECT '书单',           COUNT(*) FROM `book_list`
UNION ALL SELECT '书单书籍关联',   COUNT(*) FROM `list_book`
UNION ALL SELECT '读书笔记',       COUNT(*) FROM `note`
UNION ALL SELECT '收藏',           COUNT(*) FROM `collect`
UNION ALL SELECT '评论',           COUNT(*) FROM `comment`;
