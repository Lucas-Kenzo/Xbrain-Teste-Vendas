INSERT INTO vendedor (id, cpf, nome, email) VALUES (1, '123.123.123-81', 'Vendedor 01', 'VENDEDOR01@TESTE.COM');
INSERT INTO vendedor (id, cpf, nome, email) VALUES (2, '123.123.123-82', 'Vendedor 02', 'VENDEDOR02@TESTE.COM');
INSERT INTO vendedor (id, cpf, nome, email) VALUES (3, '123.123.123-83', 'Vendedor 03', 'VENDEDOR03@TESTE.COM');
INSERT INTO vendedor (id, cpf, nome, email) VALUES (4, '123.123.123-84', 'Vendedor 04', 'VENDEDOR04@TESTE.COM');

INSERT INTO produto (id, valor, descricao, categoria, quantidade) VALUES (1, 120, 'Gabinete Gamemax Draco Xd', 'PERIFERICOS', 10);
INSERT INTO produto (id, valor, descricao, categoria, quantidade) VALUES (2, 1350, 'Ryzen 7 5700x3d', 'HARDWARE', 5);
INSERT INTO produto (id, valor, descricao, categoria, quantidade) VALUES (3, 3500, 'Iphone 13 128GB', 'CELULARES', 1);
INSERT INTO produto (id, valor, descricao, categoria, quantidade) VALUES (4, 89, 'Pasta Térmica Cooler MAster 4g', 'OUTROS', 23);
INSERT INTO produto (id, valor, descricao, categoria, quantidade) VALUES (5, 230, 'Hogwarts Legacy PS5', 'GAMES', 5);

INSERT INTO venda (id, data_da_venda, valor, vendedor_id, vendedor_nome) VALUES (1, CURRENT_TIMESTAMP, '123', 1, 'Vendedor 01');
INSERT INTO venda (id, data_da_venda, valor, vendedor_id, vendedor_nome) VALUES (2, CURRENT_TIMESTAMP, '123', 2, 'Vendedor 02');
INSERT INTO venda (id, data_da_venda, valor, vendedor_id, vendedor_nome) VALUES (3, CURRENT_TIMESTAMP, '123', 3, 'Vendedor 03');

INSERT INTO venda_produto (id, fk_venda, fk_produto, quantidade) VALUES (1, 1, 1, 2);
INSERT INTO venda_produto (id, fk_venda, fk_produto, quantidade) VALUES (2, 1, 2, 1);
INSERT INTO venda_produto (id, fk_venda, fk_produto, quantidade) VALUES (3, 1, 4, 3);
INSERT INTO venda_produto (id, fk_venda, fk_produto, quantidade) VALUES (4, 2, 5, 5);
INSERT INTO venda_produto (id, fk_venda, fk_produto, quantidade) VALUES (5, 3, 3, 2);
