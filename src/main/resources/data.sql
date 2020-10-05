--テストユーザーのデータが存在しない場合は追加する
INSERT INTO usr(user_id, password, role_name) 
SELECT 'testuser', '$2a$10$SWXUVkBsmWH3sXEU5YNlt.wDVMEcQpPGKA0PP4Z9uTRU2IPj69Zxm', 'TEST'
WHERE 
  NOT EXISTS(
    SELECT user_id FROM usr WHERE user_id= 'testuser'
    );