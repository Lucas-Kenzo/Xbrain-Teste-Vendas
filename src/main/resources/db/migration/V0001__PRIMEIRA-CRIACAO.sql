create table produto (
    id bigint auto_increment,
    quantidade integer,
    valor numeric(38,2),
    categoria varchar(255),
    descricao varchar(255),
    primary key (id)
);

create table venda (
    id bigint auto_increment,
    valor numeric(38,2),
    data_da_venda timestamp(6),
    vendedor_id bigint,
    nome_vendedor varchar(255),
    primary key (id)
);

create table venda_produto (
    fk_produto bigint not null,
    fk_venda bigint not null,
    quantidade integer not null,
    primary key (fk_produto, fk_venda),
    foreign key (fk_produto) references produto(id),
    foreign key (fk_venda) references venda(id)
);

create table vendedor (
    id bigint auto_increment,
    cpf varchar(255) not null unique,
    email varchar(255) not null,
    nome varchar(255) not null,
    primary key (id)
);
