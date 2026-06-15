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
                                                       (1, 'Bebidas', 'Bebidas em geral, com e sem gás'),
                                                       (2, 'Lanches', 'Sanduíches e hambúrgueres'),
                                                       (3, 'Sobremesas', 'Doces, pudins e tortas');

-- ---------------------------------------------------------------------
-- formaPagamento
-- ---------------------------------------------------------------------
INSERT INTO forma_pagamento (id, nome, descricao) VALUES
                                                       (1, 'Cartão de Crédito', 'Pagamento via cartão de crédito'),
                                                       (2, 'Cartão de Débito', 'Pagamento via cartão de débito'),
                                                       (3, 'Pix', 'Pagamento instantâneo via Pix');

-- ---------------------------------------------------------------------
-- cliente
-- ---------------------------------------------------------------------
INSERT INTO cliente (id, nome, cpf, telefone, email) VALUES
                                                         (1, 'João Silva', '12345678901', '11987654321', 'joao.silva@email.com'),
                                                         (2, 'Maria Oliveira', '98765432100', '11912345678', 'maria.oliveira@email.com'),
                                                         (3, 'Carlos Souza', '45678912300', '21998765432', 'carlos.souza@email.com');

-- ---------------------------------------------------------------------
-- endereco
-- ---------------------------------------------------------------------
INSERT INTO endereco (id, endereco, numero, complemento, bairro, cidade, estado, cep, cliente_id) VALUES
                                                                                                     (1, 'Rua das Flores', 100, 'Apto 12', 'Centro', 'São Paulo', 'SP', '01001000', 1),
                                                                                                     (2, 'Av. Paulista', 1500, NULL, 'Bela Vista', 'São Paulo', 'SP', '01310100', 1),
                                                                                                     (3, 'Rua do Comércio', 250, 'Casa', 'Centro', 'Rio de Janeiro', 'RJ', '20010000', 2),
                                                                                                     (4, 'Av. Atlântica', 3000, 'Bloco B', 'Copacabana', 'Rio de Janeiro', 'RJ', '22070001', 3);

-- ---------------------------------------------------------------------
-- produto
-- ---------------------------------------------------------------------
INSERT INTO produto (id, nome, descricao, preco, disponivel, categoria_id) VALUES
                                                                                     (1, 'Refrigerante Cola 350ml', 'Lata de refrigerante sabor cola', 5.50, 1, 1),
                                                                                     (2, 'Água Mineral 500ml', 'Água mineral sem gás', 3.00, 1, 1),
                                                                                     (3, 'Hambúrguer Clássico', 'Pão, carne, queijo, alface e tomate', 18.90, 1, 2),
                                                                                     (4, 'Sanduíche de Frango', 'Pão integral com frango grelhado', 16.50, 1, 2),
                                                                                     (5, 'Pudim de Leite', 'Pudim de leite condensado', 8.00, 1, 3),
                                                                                     (6, 'Brownie de Chocolate', 'Brownie com calda de chocolate', 9.50, 0, 3);

-- ---------------------------------------------------------------------
-- pedido
-- ---------------------------------------------------------------------
INSERT INTO pedido (id, data_hora, status, valor_total, cliente_id, endereco_id) VALUES
                                                                                        (1, '2026-06-01 12:30:00', 'Entregue', 32.90, 1, 1),
                                                                                        (2, '2026-06-05 19:15:00', 'Em preparo', 30.00, 2, 3),
                                                                                        (3, '2026-06-10 20:00:00', 'Cancelado', 18.90, 3, 4);

-- ---------------------------------------------------------------------
-- itensPedido
-- ---------------------------------------------------------------------
INSERT INTO itens_pedido (id, quantidade, preco_unitario, subtotal, pedido_id, produto_id) VALUES
                                                                                           (1, 2, 5.50, 11.00, 1, 1),
                                                                                           (2, 1, 3.00, 3.00, 1, 2),
                                                                                           (3, 1, 18.90, 18.90, 1, 3),
                                                                                           (4, 1, 16.50, 16.50, 2, 4),
                                                                                           (5, 1, 8.00, 8.00, 2, 5),
                                                                                           (6, 1, 5.50, 5.50, 2, 1),
                                                                                           (7, 1, 18.90, 18.90, 3, 3);

-- ---------------------------------------------------------------------
-- pagamento
-- ---------------------------------------------------------------------
INSERT INTO pagamento (id, valor_pago, data_hora, pedido_id, forma_pagamento_id) VALUES
                                                                                (1, 32.90, '2026-06-01 12:35:00', 1, 1),
                                                                                (2, 30.00, '2026-06-05 19:20:00', 2, 3),
                                                                                (3, 18.90, '2026-06-10 20:05:00', 3, 2);