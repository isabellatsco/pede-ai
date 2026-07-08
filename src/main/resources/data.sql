-- =====================================================================
-- data.sql
-- Script de população (dados dummy) baseado no DER fornecido
-- Ordem de inserção respeita as dependências de chave estrangeira:
-- categoriaProduto, formaPagamento, cliente -> endereco, produto,
-- pedido -> itensPedido, pagamento
-- =====================================================================

-- ---------------------------------------------------------------------
-- categoriaProduto
-- ---------------------------------------------------------------------
INSERT INTO categoria_produto (id, nome, descricao) VALUES
                                                       (101, 'Bebidas', 'Bebidas em geral, com e sem gás'),
                                                       (102, 'Lanches', 'Sanduíches e hambúrgueres'),
                                                       (103, 'Sobremesas', 'Doces, pudins e tortas');

-- ---------------------------------------------------------------------
-- formaPagamento
-- ---------------------------------------------------------------------
INSERT INTO forma_pagamento (id, nome, descricao) VALUES
                                                       (101, 'Cartão de Crédito', 'Pagamento via cartão de crédito'),
                                                       (102, 'Cartão de Débito', 'Pagamento via cartão de débito'),
                                                       (103, 'Pix', 'Pagamento instantâneo via Pix');

-- ---------------------------------------------------------------------
-- cliente
-- ---------------------------------------------------------------------
INSERT INTO cliente (id, nome, cpf, telefone, email) VALUES
                                                         (101, 'João Silva', '71993903275', '11987654321', 'joao.silva@email.com'),
                                                         (102, 'Maria Oliveira', '01978525214', '11912345678', 'maria.oliveira@email.com'),
                                                         (103, 'Carlos Souza', '80941757293', '21998765432', 'carlos.souza@email.com');

-- ---------------------------------------------------------------------
-- endereco
-- ---------------------------------------------------------------------
INSERT INTO endereco (id, endereco, numero, complemento, bairro, cidade, estado, cep, cliente_id, padrao) VALUES
                                                                                                     (101, 'Rua das Flores', 100, 'Apto 12', 'Centro', 'São Paulo', 'SP', '01001000', 101, true),
                                                                                                     (102, 'Av. Paulista', 1500, NULL, 'Bela Vista', 'São Paulo', 'SP', '01310100', 101, false),
                                                                                                     (103, 'Rua do Comércio', 250, 'Casa', 'Centro', 'Rio de Janeiro', 'RJ', '20010000', 102, true),
                                                                                                     (104, 'Av. Atlântica', 3000, 'Bloco B', 'Copacabana', 'Rio de Janeiro', 'RJ', '22070001', 103, true);

-- ---------------------------------------------------------------------
-- produto
-- ---------------------------------------------------------------------
INSERT INTO produto (id, nome, descricao, preco, disponivel, categoria_id) VALUES
                                                                                     (101, 'Refrigerante Cola 350ml', 'Lata de refrigerante sabor cola', 5.50, 1, 101),
                                                                                     (102, 'Água Mineral 500ml', 'Água mineral sem gás', 3.00, 1, 101),
                                                                                     (103, 'Hambúrguer Clássico', 'Pão, carne, queijo, alface e tomate', 18.90, 1, 102),
                                                                                     (104, 'Sanduíche de Frango', 'Pão integral com frango grelhado', 16.50, 1, 102),
                                                                                     (105, 'Pudim de Leite', 'Pudim de leite condensado', 8.00, 1, 103),
                                                                                     (106, 'Brownie de Chocolate', 'Brownie com calda de chocolate', 9.50, 0, 103);

-- ---------------------------------------------------------------------
-- pedido
-- ---------------------------------------------------------------------
INSERT INTO pedido (id, data_hora, status, valor_total, cliente_id, endereco_id) VALUES
                                                                                        (101, '2026-06-01 12:30:00', 'AGUARDANDO_CONFIRMACAO', 32.90, 101, 101),
                                                                                        (102, '2026-06-05 19:15:00', 'EM_PREPARO', 30.00, 102, 103),
                                                                                        (103, '2026-06-10 20:00:00', 'CANCELADO', 18.90, 103, 104);

-- ---------------------------------------------------------------------
-- itensPedido
-- ---------------------------------------------------------------------
INSERT INTO itens_pedido (id, quantidade, preco_unitario, subtotal, pedido_id, produto_id) VALUES
                                                                                           (1, 2, 5.50, 11.00, 101, 101),
                                                                                           (2, 1, 3.00, 3.00, 101, 102),
                                                                                           (3, 1, 18.90, 18.90, 101, 103),
                                                                                           (4, 1, 16.50, 16.50, 102, 104),
                                                                                           (5, 1, 8.00, 8.00, 102, 105),
                                                                                           (6, 1, 5.50, 5.50, 102, 101),
                                                                                           (7, 1, 18.90, 18.90, 103, 103);

-- ---------------------------------------------------------------------
-- pagamento
-- ---------------------------------------------------------------------
INSERT INTO pagamento (id, valor_pago, data_hora, pedido_id, forma_pagamento_id) VALUES
                                                                                (1, 32.90, '2026-06-01 12:35:00', 101, 101),
                                                                                (2, 30.00, '2026-06-05 19:20:00', 102, 103),
                                                                                (3, 18.90, '2026-06-10 20:05:00', 103, 102);