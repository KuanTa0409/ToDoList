-- 先刪除已存在的表（由於有外鍵關係，需要按順序刪除）
DROP TABLE IF EXISTS todo;
DROP TABLE IF EXISTS user_detail;
DROP TABLE IF EXISTS user;

-- 建立 user
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '使用者帳號',
    password VARCHAR(20) NOT NULL COMMENT '使用者密碼',
    CONSTRAINT chk_username CHECK (username REGEXP '^[a-zA-Z0-9]+$'),
    CONSTRAINT chk_username_length CHECK (LENGTH(username) >= 5 AND LENGTH(username) <= 50),
    CONSTRAINT chk_password_length CHECK (LENGTH(password) >= 6 AND LENGTH(password) <= 20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶帳號表';

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
    CONSTRAINT fk_user_detail FOREIGN KEY (uid) REFERENCES user(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT chk_name_length CHECK (LENGTH(name) <= 12),
    CONSTRAINT chk_age CHECK (age >= 12 AND age <= 100),
    CONSTRAINT chk_resume_length CHECK (LENGTH(resume) <= 500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶詳細資料表';

-- 建立 todo
CREATE TABLE todo (
    tid BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'tid',
    tusername VARCHAR(50) NOT NULL COMMENT '使用者帳號',
    description TEXT COMMENT '待辦事項描述',
    completed BOOLEAN DEFAULT FALSE COMMENT '是否完成',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    user_id BIGINT NOT NULL COMMENT '使用者ID',
    CONSTRAINT fk_todo_user FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX idx_tusername (tusername)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待辦事項表';