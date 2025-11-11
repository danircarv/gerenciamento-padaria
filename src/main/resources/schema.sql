-- Database Schema for Sistema Gestão Padaria

-- Create database
CREATE DATABASE IF NOT EXISTS padaria_db;
USE padaria_db;

-- Table: produtos
CREATE TABLE IF NOT EXISTS produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    categoria VARCHAR(50),
    unidade_medida VARCHAR(20) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: estoque
CREATE TABLE IF NOT EXISTS estoque (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL DEFAULT 0,
    quantidade_minima DECIMAL(10, 2) NOT NULL DEFAULT 0,
    quantidade_maxima DECIMAL(10, 2),
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE,
    UNIQUE KEY uk_produto_estoque (produto_id)
);

-- Table: clientes
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: fornecedores
CREATE TABLE IF NOT EXISTS fornecedores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco TEXT,
    contato VARCHAR(100),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: vendas
CREATE TABLE IF NOT EXISTS vendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(50),
    status VARCHAR(20) DEFAULT 'FINALIZADA',
    observacoes TEXT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL
);

-- Table: itens_venda
CREATE TABLE IF NOT EXISTS itens_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (venda_id) REFERENCES vendas(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE RESTRICT
);

-- Table: encomendas
CREATE TABLE IF NOT EXISTS encomendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_entrega DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDENTE',
    valor_total DECIMAL(10, 2) NOT NULL,
    valor_entrada DECIMAL(10, 2) DEFAULT 0,
    observacoes TEXT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE RESTRICT
);

-- Table: itens_encomenda
CREATE TABLE IF NOT EXISTS itens_encomenda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    encomenda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    observacoes TEXT,
    FOREIGN KEY (encomenda_id) REFERENCES encomendas(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE RESTRICT
);

-- Insert sample data for produtos
INSERT INTO produtos (nome, descricao, preco, categoria, unidade_medida) VALUES
('Pão Francês', 'Pão francês tradicional', 0.50, 'Pães', 'unidade'),
('Pão de Forma', 'Pão de forma integral', 8.50, 'Pães', 'unidade'),
('Croissant', 'Croissant de manteiga', 5.00, 'Pães', 'unidade'),
('Bolo de Chocolate', 'Bolo de chocolate com cobertura', 35.00, 'Bolos', 'unidade'),
('Torta de Morango', 'Torta de morango fresca', 45.00, 'Tortas', 'unidade'),
('Sonho', 'Sonho recheado com creme', 4.50, 'Doces', 'unidade'),
('Café Espresso', 'Café espresso tradicional', 3.50, 'Bebidas', 'unidade'),
('Suco Natural', 'Suco natural de laranja', 6.00, 'Bebidas', 'unidade');

-- Insert sample data for estoque
INSERT INTO estoque (produto_id, quantidade, quantidade_minima, quantidade_maxima) VALUES
(1, 100, 50, 200),
(2, 20, 10, 50),
(3, 30, 15, 60),
(4, 5, 2, 15),
(5, 3, 2, 10),
(6, 25, 10, 50),
(7, 50, 20, 100),
(8, 15, 10, 30);

-- Insert sample data for clientes
INSERT INTO clientes (nome, cpf, telefone, email, endereco) VALUES
('Maria Silva', '123.456.789-00', '(11) 98765-4321', 'maria@email.com', 'Rua A, 123'),
('João Santos', '987.654.321-00', '(11) 91234-5678', 'joao@email.com', 'Rua B, 456'),
('Ana Costa', '456.789.123-00', '(11) 99876-5432', 'ana@email.com', 'Rua C, 789');

-- Insert sample data for fornecedores
INSERT INTO fornecedores (nome, cnpj, telefone, email, endereco, contato) VALUES
('Fornecedor Farinha Ltda', '12.345.678/0001-00', '(11) 3000-0001', 'contato@farinha.com', 'Av. Industrial, 100', 'Carlos'),
('Distribuidora Açúcar', '98.765.432/0001-00', '(11) 3000-0002', 'vendas@acucar.com', 'Av. Comercial, 200', 'Paula');
