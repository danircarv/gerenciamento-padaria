-- Sample data for Sistema Gestão Padaria

-- Inserir Fornecedores
INSERT INTO fornecedores (nome, cnpj, telefone, email, endereco, ativo) VALUES
('Moinho São Paulo', '12.345.678/0001-90', '(11) 98765-4321', 'contato@moinhosaopaulo.com.br', 'Av. Paulista, 1000 - São Paulo, SP', TRUE),
('Laticínios Bom Leite', '23.456.789/0001-80', '(11) 97654-3210', 'vendas@bomleite.com.br', 'Rua das Flores, 500 - São Paulo, SP', TRUE),
('Ovos Caipira Ltda', '34.567.890/0001-70', '(11) 96543-2109', 'contato@ovoscaipira.com.br', 'Rua Rural, 200 - Campinas, SP', TRUE);

-- Inserir Clientes
INSERT INTO clientes (nome, cpf, telefone, email, endereco, ativo) VALUES
('Maria Silva', '123.456.789-00', '(11) 91234-5678', 'maria.silva@email.com', 'Rua A, 123 - São Paulo, SP', TRUE),
('João Santos', '234.567.890-11', '(11) 92345-6789', 'joao.santos@email.com', 'Rua B, 456 - São Paulo, SP', TRUE),
('Ana Paula Costa', '345.678.901-22', '(11) 93456-7890', 'ana.costa@email.com', 'Av. C, 789 - São Paulo, SP', TRUE),
('Carlos Eduardo', '456.789.012-33', '(11) 94567-8901', 'carlos.eduardo@email.com', 'Rua D, 321 - São Paulo, SP', TRUE);

-- Inserir Produtos
INSERT INTO produtos (nome, descricao, categoria, preco_venda, preco_custo, unidade_medida, ativo) VALUES
('Pão Francês', 'Pão francês tradicional', 'PAES', 0.60, 0.30, 'UN', TRUE),
('Pão de Forma', 'Pão de forma integral 500g', 'PAES', 8.50, 5.00, 'UN', TRUE),
('Bolo de Chocolate', 'Bolo de chocolate com cobertura', 'BOLOS', 35.00, 20.00, 'UN', TRUE),
('Sonho', 'Sonho recheado com creme', 'DOCES', 4.50, 2.50, 'UN', TRUE),
('Croissant', 'Croissant de manteiga', 'PAES', 6.00, 3.50, 'UN', TRUE),
('Pão de Queijo', 'Pão de queijo mineiro', 'SALGADOS', 2.50, 1.20, 'UN', TRUE),
('Bolo de Cenoura', 'Bolo de cenoura com chocolate', 'BOLOS', 30.00, 18.00, 'UN', TRUE),
('Torta de Frango', 'Torta de frango com catupiry', 'SALGADOS', 8.00, 5.00, 'UN', TRUE),
('Brigadeiro', 'Brigadeiro gourmet', 'DOCES', 3.00, 1.50, 'UN', TRUE),
('Café', 'Café expresso', 'BEBIDAS', 5.00, 2.00, 'UN', TRUE);

-- Inserir Estoque
INSERT INTO estoque (produto_id, quantidade, quantidade_minima, quantidade_maxima, localizacao) VALUES
(1, 150, 50, 300, 'Prateleira A1'),
(2, 40, 20, 80, 'Prateleira A2'),
(3, 15, 5, 30, 'Vitrine B1'),
(4, 30, 10, 50, 'Vitrine B2'),
(5, 25, 10, 50, 'Prateleira A3'),
(6, 60, 20, 100, 'Vitrine C1'),
(7, 12, 5, 25, 'Vitrine B1'),
(8, 20, 10, 40, 'Vitrine C2'),
(9, 45, 15, 80, 'Vitrine B3'),
(10, 100, 30, 150, 'Balcão');

-- Inserir Vendas
INSERT INTO vendas (cliente_id, data_venda, valor_total, desconto, valor_final, forma_pagamento, status) VALUES
(1, NOW() - INTERVAL 2 DAY, 45.60, 0.00, 45.60, 'DINHEIRO', 'CONCLUIDA'),
(2, NOW() - INTERVAL 1 DAY, 28.50, 2.50, 26.00, 'CARTAO_CREDITO', 'CONCLUIDA'),
(3, NOW(), 95.00, 5.00, 90.00, 'PIX', 'CONCLUIDA');

-- Inserir Itens de Venda
INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal) VALUES
(1, 1, 20, 0.60, 12.00),
(1, 4, 5, 4.50, 22.50),
(1, 10, 2, 5.00, 10.00),
(2, 2, 2, 8.50, 17.00),
(2, 6, 5, 2.50, 12.50),
(3, 3, 2, 35.00, 70.00),
(3, 5, 4, 6.00, 24.00);

-- Inserir Encomendas
INSERT INTO encomendas (cliente_id, data_encomenda, data_entrega, valor_total, valor_entrada, status, observacoes) VALUES
(1, NOW(), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 150.00, 50.00, 'PENDENTE', 'Bolo para aniversário - 15 anos'),
(4, NOW(), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 200.00, 100.00, 'CONFIRMADA', 'Festa de casamento - 100 salgados variados');

-- Inserir Itens de Encomenda
INSERT INTO itens_encomenda (encomenda_id, produto_id, quantidade, preco_unitario, subtotal) VALUES
(1, 3, 3, 35.00, 105.00),
(1, 9, 15, 3.00, 45.00),
(2, 6, 50, 2.50, 125.00),
(2, 8, 50, 1.50, 75.00);
