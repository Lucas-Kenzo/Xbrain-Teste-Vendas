drop table venda_produto;

create table venda_produto (
    id bigint auto_increment primary key,
    subtotal numeric(38,2),
    fk_produto bigint not null,
    fk_venda bigint not null,
    quantidade integer not null,
    foreign key (fk_produto) references produto(id),
    foreign key (fk_venda) references venda(id)
);