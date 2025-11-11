# Sistema de Gestão de Padaria

Sistema web completo para gestão de padaria desenvolvido com Spring Boot 3.2, JDBC puro (sem JPA) e MySQL 8.

## 📋 Descrição

Projeto que visa automatizar os processos da padaria para que Dona Maria possa melhorar o controle de produção, gerenciar melhor o estoque de ingredientes, integrar os pedidos recebidos com o sistema de produção, e facilitar o processo desde o recebimento do pedido até a entrega ao cliente.

## 🏗️ Arquitetura

Sistema desenvolvido em **arquitetura de 3 camadas**:

- **Model**: Entidades de domínio (POJOs)
- **DAO (Data Access Object)**: Acesso a dados usando JdbcTemplate
- **Service**: Lógica de negócio e validações
- **Controller**: Apresentação (MVC + REST API)

## 🛠️ Stack Tecnológica

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring JDBC** (JdbcTemplate - sem JPA)
- **MySQL 8**
- **Thymeleaf** (Template Engine)
- **Bootstrap 5** (Frontend)
- **Maven** (Gerenciamento de dependências)

## 📦 Módulos do Sistema

### 1. Produtos
- CRUD completo de produtos
- Categorização de produtos
- Controle de preços (venda e custo)
- Diferentes unidades de medida
- Status ativo/inativo

### 2. Estoque
- Controle de quantidade em estoque
- Alertas de estoque baixo
- Níveis mínimo e máximo configuráveis
- Localização de produtos no estoque
- Atualização automática nas vendas

### 3. Vendas
- Registro de vendas com múltiplos itens
- Diferentes formas de pagamento
- Aplicação de descontos
- Baixa automática de estoque
- Histórico de vendas

### 4. Encomendas
- Gestão de pedidos antecipados
- Controle de status (Pendente, Confirmada, Em Produção, Pronta, Entregue, Cancelada)
- Valor de entrada e saldo
- Data de entrega programada
- Acompanhamento de produção

### 5. Clientes
- Cadastro de clientes
- Validação de CPF
- Histórico de compras
- Status ativo/inativo

### 6. Fornecedores
- Cadastro de fornecedores
- Validação de CNPJ
- Dados de contato
- Status ativo/inativo

### 7. Relatórios
- Vendas por período
- Produtos mais vendidos
- Vendas por forma de pagamento
- Alertas de estoque baixo
- Encomendas por status
- Clientes mais ativos

## 🗄️ Estrutura do Banco de Dados

O sistema utiliza as seguintes tabelas:

- **produtos**: Catálogo de produtos
- **estoque**: Controle de inventário
- **vendas**: Registro de vendas
- **itens_venda**: Itens de cada venda
- **encomendas**: Pedidos antecipados
- **itens_encomenda**: Itens de cada encomenda
- **clientes**: Cadastro de clientes
- **fornecedores**: Cadastro de fornecedores

## 🚀 Como Executar

### Pré-requisitos

1. Java 17 ou superior
2. Maven 3.6+
3. MySQL 8.0+

### Configuração do Banco de Dados

1. Criar banco de dados MySQL:
```sql
CREATE DATABASE padaria_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Configurar credenciais em `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/padaria_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=sua_senha
```

### Executar a Aplicação

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📡 API REST Endpoints

### Produtos
- `GET /api/produtos` - Listar todos os produtos
- `GET /api/produtos/{id}` - Buscar produto por ID
- `GET /api/produtos/ativos` - Listar produtos ativos
- `GET /api/produtos/categoria/{categoria}` - Buscar por categoria
- `POST /api/produtos` - Criar novo produto
- `PUT /api/produtos/{id}` - Atualizar produto
- `DELETE /api/produtos/{id}` - Excluir produto
- `PATCH /api/produtos/{id}/inativar` - Inativar produto

### Estoque
- `GET /api/estoque` - Listar todo o estoque
- `GET /api/estoque/alertas` - Produtos abaixo do estoque mínimo
- `GET /api/estoque/{id}` - Buscar estoque por ID
- `GET /api/estoque/produto/{produtoId}` - Buscar estoque de um produto
- `POST /api/estoque` - Criar registro de estoque
- `PUT /api/estoque/{id}` - Atualizar estoque
- `PATCH /api/estoque/produto/{produtoId}/adicionar` - Adicionar quantidade
- `PATCH /api/estoque/produto/{produtoId}/remover` - Remover quantidade
- `DELETE /api/estoque/{id}` - Excluir registro

### Vendas
- `GET /api/vendas` - Listar todas as vendas
- `GET /api/vendas/{id}` - Buscar venda por ID
- `GET /api/vendas/periodo?inicio={datetime}&fim={datetime}` - Vendas por período
- `POST /api/vendas` - Realizar nova venda
- `DELETE /api/vendas/{id}` - Cancelar venda

### Encomendas
- `GET /api/encomendas` - Listar todas as encomendas
- `GET /api/encomendas/{id}` - Buscar encomenda por ID
- `GET /api/encomendas/status/{status}` - Buscar por status
- `GET /api/encomendas/data-entrega?data={date}` - Buscar por data de entrega
- `POST /api/encomendas` - Criar nova encomenda
- `PATCH /api/encomendas/{id}/status` - Atualizar status
- `DELETE /api/encomendas/{id}` - Excluir encomenda

### Clientes
- `GET /api/clientes` - Listar todos os clientes
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `GET /api/clientes/ativos` - Listar clientes ativos
- `POST /api/clientes` - Criar novo cliente
- `PUT /api/clientes/{id}` - Atualizar cliente
- `DELETE /api/clientes/{id}` - Excluir cliente
- `PATCH /api/clientes/{id}/inativar` - Inativar cliente

### Fornecedores
- `GET /api/fornecedores` - Listar todos os fornecedores
- `GET /api/fornecedores/{id}` - Buscar fornecedor por ID
- `GET /api/fornecedores/ativos` - Listar fornecedores ativos
- `POST /api/fornecedores` - Criar novo fornecedor
- `PUT /api/fornecedores/{id}` - Atualizar fornecedor
- `DELETE /api/fornecedores/{id}` - Excluir fornecedor
- `PATCH /api/fornecedores/{id}/inativar` - Inativar fornecedor

### Relatórios
- `GET /api/relatorios/vendas/periodo?inicio={datetime}&fim={datetime}` - Relatório de vendas
- `GET /api/relatorios/produtos/mais-vendidos?inicio={datetime}&fim={datetime}` - Top produtos
- `GET /api/relatorios/vendas/forma-pagamento?inicio={datetime}&fim={datetime}` - Por forma de pagamento
- `GET /api/relatorios/estoque/baixo` - Produtos com estoque baixo
- `GET /api/relatorios/encomendas/status` - Encomendas por status
- `GET /api/relatorios/clientes/ativos` - Clientes mais ativos

## 🌐 Interface Web

A aplicação possui uma interface web completa desenvolvida com Thymeleaf e Bootstrap:

- **Dashboard**: Visão geral do sistema com indicadores
- **Gestão de Produtos**: Listagem e formulário CRUD
- **Controle de Estoque**: Monitoramento e alertas
- **Registro de Vendas**: Interface para realizar vendas
- **Gestão de Encomendas**: Acompanhamento de pedidos
- **Cadastro de Clientes**: Gerenciamento de clientes
- **Cadastro de Fornecedores**: Gerenciamento de fornecedores

## 📊 Funcionalidades Principais

### Gestão de Produtos
- Cadastro completo com categorias
- Controle de margem (preço custo vs. venda)
- Múltiplas unidades de medida

### Controle de Estoque
- Sistema de alertas automáticos
- Níveis de estoque configuráveis
- Baixa automática em vendas
- Localização física dos produtos

### Sistema de Vendas
- Vendas multi-item
- Múltiplas formas de pagamento
- Sistema de descontos
- Integração automática com estoque
- Histórico completo

### Gestão de Encomendas
- Workflow de status completo
- Programação de entregas
- Controle de valores e entrada
- Integração com produção

### Relatórios
- Análise de vendas por período
- Top produtos vendidos
- Análise por forma de pagamento
- Alertas de reposição
- Performance de clientes

## 🔒 Validações Implementadas

### Produtos
- Nome obrigatório
- Preço de venda maior que zero
- Preço de custo não negativo
- Preço de custo não maior que preço de venda

### Estoque
- Quantidade não negativa
- Quantidade mínima configurável
- Quantidade máxima maior que mínima
- Verificação de disponibilidade antes de vendas

### Clientes
- Nome obrigatório
- Validação de formato CPF (11 dígitos)
- CPF único no sistema

### Fornecedores
- Nome obrigatório
- Validação de formato CNPJ (14 dígitos)
- CNPJ único no sistema

### Vendas
- Mínimo de um item
- Forma de pagamento obrigatória
- Quantidades positivas
- Verificação de estoque disponível
- Valor final não negativo

### Encomendas
- Cliente obrigatório
- Data de entrega futura
- Mínimo de um item
- Valor de entrada não maior que total
- Workflow de status controlado

## 📝 Dados de Exemplo

O sistema vem com dados de exemplo pré-cadastrados:

- 3 Fornecedores
- 4 Clientes
- 10 Produtos em diversas categorias
- Estoque inicial para todos os produtos
- 3 Vendas de exemplo
- 2 Encomendas de exemplo

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor, sinta-se à vontade para submeter pull requests.

## 📄 Licença

Este projeto é open source e está disponível sob a licença MIT.

## 👥 Autor

Sistema desenvolvido para automatizar processos de gestão de padarias.
