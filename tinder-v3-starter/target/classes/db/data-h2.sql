DELETE FROM t_user;

INSERT INTO t_user (id, name, username, password, age, email, type, org_id, create_at) VALUES
(1, 'admin', 'admin', '123456', 18, 'test1@baomidou.com', 0, 1, '2023-04-01 00:00:00'),
(2, 'root', 'root', '123456', 20, 'test2@baomidou.com', 1, 2, '2023-04-02 00:00:00'),
(3, 'Tom', 'Tom', '123456', 20, 'test3@baomidou.com', 1, 2, '2023-04-03 00:00:00'),
(4, 'Sandy', 'Sandy', '123456', 20, 'test4@baomidou.com', 1, 3, '2023-04-04 00:00:00'),
(5, 'Billie', 'Billie', '123456', 20, 'test5@baomidou.com', 1, 3, '2023-04-05 00:00:00');

INSERT INTO t_organization (id, name, parent_id) VALUES
(1, '研发部门', 0),
(2, '软件开发部门', 1),
(3, '软件测试部门', 1),
(4, '算法开发部门', 1);