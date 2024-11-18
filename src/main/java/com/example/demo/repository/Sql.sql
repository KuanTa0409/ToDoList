-- 先刪除已存在的表（由於有外鍵關係，需要按順序刪除）
DROP TABLE IF EXISTS todo;
DROP TABLE IF EXISTS user_detail;
DROP TABLE IF EXISTS user;

-- 建立 user
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '使用者帳號',
    password VARCHAR(20) NOT NULL COMMENT '使用者密碼'
);

-- 建立 user_detail
CREATE TABLE user_detail (
    uid BIGINT PRIMARY KEY COMMENT 'uid',
    name VARCHAR(12) NOT NULL COMMENT '姓名',
    age INT NOT NULL COMMENT '年齡',
    birth DATE NOT NULL COMMENT '生日',
    gender VARCHAR(10) NOT NULL COMMENT '性別',
    education VARCHAR(50) NOT NULL COMMENT '教育程度',
    interest JSON NOT NULL COMMENT '興趣',
    resume TEXT COMMENT '簡歷',
    createtime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    FOREIGN KEY (uid) REFERENCES user(id)
);

-- 建立 todo
CREATE TABLE todo (
    tid BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'tid',
    tusername VARCHAR(50) NOT NULL COMMENT '使用者帳號',
    description TEXT NOT NULL COMMENT '待辦事項描述',
    completed BOOLEAN DEFAULT FALSE COMMENT '是否完成',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    user_id BIGINT NOT NULL COMMENT '使用者ID',
    FOREIGN KEY (user_id) REFERENCES user(id)
);