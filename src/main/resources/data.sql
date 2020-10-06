--テストユーザーのデータが存在しない場合は追加する
INSERT INTO usr(user_id, password, role_name) 
SELECT 'testuser', '$2a$10$uCwUK7lhZ1Xr4kg1kWfhD.ZoIaVNbxelFthfNQEMhj3.5IqMmAyfq', 'TEST'
WHERE 
  NOT EXISTS(
    SELECT user_id FROM usr WHERE user_id= 'testuser'
    );