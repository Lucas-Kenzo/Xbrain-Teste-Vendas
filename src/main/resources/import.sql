INSERT INTO vendedor (cpf, nome, email) VALUES ('123.123.123-81', 'Vendedor 01', 'VENDEDOR01@TESTE.COM');
INSERT INTO vendedor (cpf, nome, email) VALUES ('123.123.123-82', 'Vendedor 02', 'VENDEDOR02@TESTE.COM');
INSERT INTO vendedor (cpf, nome, email) VALUES ('123.123.123-83', 'Vendedor 03', 'VENDEDOR03@TESTE.COM');
INSERT INTO vendedor (cpf, nome, email) VALUES ('123.123.123-84', 'Vendedor 04', 'VENDEDOR04@TESTE.COM');

INSERT INTO produto (valor, descricao, categoria, quantidade) VALUES (120, 'Gabinete Gamemax Draco Xd', 'PERIFERICOS', 10);
INSERT INTO produto (valor, descricao, categoria, quantidade) VALUES (1350, 'Ryzen 7 5700x3d', 'HARDWARE', 5);
INSERT INTO produto (valor, descricao, categoria, quantidade) VALUES (3500, 'Iphone 13 128GB', 'CELULARES', 1);
INSERT INTO produto (valor, descricao, categoria, quantidade) VALUES (89, 'Pasta Térmica Cooler MAster 4g', 'OUTROS', 23);
INSERT INTO produto (valor, descricao, categoria, quantidade) VALUES (230, 'Hogwarts Legacy PS5', 'GAMES', 5);

INSERT INTO venda (data_da_venda, valor, vendedor_id, vendedor_nome, situacao) VALUES (CURRENT_TIMESTAMP, '1857', 1, 'Vendedor 01', 'RASCUNHO');
INSERT INTO venda (data_da_venda, valor, vendedor_id, vendedor_nome, situacao) VALUES (CURRENT_TIMESTAMP, '1150', 2, 'Vendedor 02', 'RASCUNHO');
INSERT INTO venda (data_da_venda, valor, vendedor_id, vendedor_nome, situacao) VALUES (CURRENT_TIMESTAMP, '7000', 3, 'Vendedor 03', 'RASCUNHO');

INSERT INTO venda_produto (fk_venda, fk_produto, quantidade, subtotal) VALUES (1, 1, 2, 240);
INSERT INTO venda_produto (fk_venda, fk_produto, quantidade, subtotal) VALUES (1, 2, 1, 1350);
INSERT INTO venda_produto (fk_venda, fk_produto, quantidade, subtotal) VALUES (1, 4, 3, 267);
INSERT INTO venda_produto (fk_venda, fk_produto, quantidade, subtotal) VALUES (2, 5, 5, 1150);
INSERT INTO venda_produto (fk_venda, fk_produto, quantidade, subtotal) VALUES (3, 3, 2, 7000);
