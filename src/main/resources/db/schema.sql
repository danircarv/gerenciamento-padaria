-- Database Schema for Sistema Gestão Padaria

-- Drop tables if they exist (in reverse order due to foreign keys)
DROP TABLE IF EXISTS itens_encomenda;
DROP TABLE IF EXISTS encomendas;
DROP TABLE IF EXISTS itens_venda;
DROP TABLE IF EXISTS vendas;
DROP TABLE IF EXISTS estoque;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS fornecedores;

-- Tabela de Fornecedores
CREATE TABLE fornecedores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(200),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_fornecedor_nome (nome)
);

-- Tabela de Clientes
CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(200),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_cliente_nome (nome)
);

-- Tabela de Produtos
CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(50),
    preco_venda DECIMAL(10, 2) NOT NULL,
    preco_custo DECIMAL(10, 2),
    unidade_medida VARCHAR(20) DEFAULT 'UN',
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_produto_nome (nome),
    INDEX idx_produto_categoria (categoria)
);

-- Tabela de Estoque
CREATE TABLE estoque (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL DEFAULT 0,
    quantidade_minima DECIMAL(10, 2) DEFAULT 10,
    quantidade_maxima DECIMAL(10, 2) DEFAULT 100,
    localizacao VARCHAR(50),
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE,
    UNIQUE KEY unique_produto_estoque (produto_id),
    INDEX idx_estoque_produto (produto_id)
);

-- Tabela de Vendas
CREATE TABLE vendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL,
    desconto DECIMAL(10, 2) DEFAULT 0,
    valor_final DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(20),
    status VARCHAR(20) DEFAULT 'CONCLUIDA',
    observacoes TEXT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    INDEX idx_venda_data (data_venda),
    INDEX idx_venda_cliente (cliente_id)
);

-- Tabela de Itens de Venda
CREATE TABLE itens_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (venda_id) REFERENCES vendas(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id),
    INDEX idx_item_venda (venda_id),
    INDEX idx_item_produto (produto_id)
);

-- Tabela de Encomendas
CREATE TABLE encomendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    data_encomenda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_entrega DATE NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    valor_entrada DECIMAL(10, 2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDENTE',
    observacoes TEXT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    INDEX idx_encomenda_data_entrega (data_entrega),
    INDEX idx_encomenda_cliente (cliente_id),
    INDEX idx_encomenda_status (status)
);

-- Tabela de Itens de Encomenda
CREATE TABLE itens_encomenda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    encomenda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (encomenda_id) REFERENCES encomendas(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id),
    INDEX idx_item_encomenda (encomenda_id),
    INDEX idx_item_encomenda_produto (produto_id)
);
