#创建数据库(member)
CREATE DATABASE IF NOT EXISTS member;

#切换为member数据库
USE member;

#创建用户表(user)
CREATE TABLE user
(
    id           INTEGER AUTO_INCREMENT PRIMARY KEY
        COMMENT '主鍵',
    account      VARCHAR(32) NOT NULL UNIQUE
        COMMENT '账号',
    password     VARCHAR(32) NOT NULL
        COMMENT '密码',
    salt         VARCHAR(32) NOT NULL
        COMMENT '密码盐',
    phone        VARCHAR(11)
        COMMENT '手机号',
    nickname     VARCHAR(32) COMMENT '昵称',
    sex          TINYINT COMMENT '性别(1为男性;2为女性)',
    head_img_url VARCHAR(255) COMMENT '用户头像',
    cash         DECIMAL(16, 2) DEFAULT 0
        COMMENT '现金',
    points       INTEGER        DEFAULT 0
        COMMENT '积分',
    update_time  DATETIME       DEFAULT NOW()
        COMMENT '更新时间',
    create_time  DATETIME       DEFAULT NOW()
        COMMENT '记录创建时间'
);

#创建系统用户表(admin)
CREATE TABLE admin
(
    id          INTEGER AUTO_INCREMENT PRIMARY KEY
        COMMENT '主鍵',
    account     VARCHAR(32) NOT NULL UNIQUE
        COMMENT '账号',
    password    VARCHAR(32) NOT NULL
        COMMENT '密码',
    nickname    VARCHAR(32) COMMENT '昵称',
    salt        VARCHAR(32) NOT NULL
        COMMENT '密码盐',
    create_time DATETIME DEFAULT NOW()
        COMMENT '记录创建时间'
);

#新增系统管理员账号
INSERT INTO admin (account, password, nickname, salt)
VALUES ('billon', 'c3b2273e2a7a943fa4ef07e1a9ffa7fd', 'billon', 'bixvsadrx1yqaet0jsc3uwybmftb4a1h');