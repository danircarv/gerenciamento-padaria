# Sistema de Gestão de Padaria

Sistema web completo para gestão de padaria desenvolvido com Spring Boot, JDBC puro e MySQL. Projeto que visa automatizar os processos da padaria para que Dona Maria possa melhorar o controle de produção, gerenciar melhor o estoque de ingredientes, integrar os pedidos recebidos com o sistema de produção, e facilitar o processo desde o recebimento do pedido até a entrega ao cliente.

## 🥖 Sobre o Projeto

Sistema de gerenciamento completo para padarias com arquitetura em 3 camadas, seguindo boas práticas de desenvolvimento e padrões de projeto.

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **JDBC puro com JdbcTemplate** (sem JPA/Hibernate)
- **MySQL 8**
- **Thymeleaf** (template engine)
- **Maven** (gerenciamento de dependências)
- **HTML5/CSS3** (interface web)

## 📋 Módulos do Sistema

### 1. Produtos
- CRUD completo de produtos
- Categorização de produtos
- Controle de produtos ativos/inativos
- Unidades de medida
- Gestão de preços

### 2. Estoque
- Controle de quantidade em estoque
- Alertas de estoque baixo
- Definição de estoque mínimo e máximo
- Integração automática com vendas
- Histórico de movimentações

### 3. Vendas
- Registro de vendas com múltiplos itens
- Formas de pagamento
- Cálculo automático de totais
- Baixa automática no estoque
- Associação com clientes (opcional)
- Histórico de vendas

### 4. Clientes
- Cadastro completo de clientes
- CPF, telefone, email e endereço
- Histórico de compras
- Busca por nome ou CPF

### 5. Fornecedores
- Cadastro de fornecedores
- CNPJ, contatos e endereço
- Controle de fornecedores ativos/inativos
- Gestão de contatos

### 6. Encomendas
- Gestão de pedidos especiais
- Data de entrega programada
- Status do pedido (Pendente, Em Produção, Pronta, Entregue)
- Controle de entrada/sinal
- Itens personalizados com observações

### 7. Relatórios
- Vendas diárias
- Vendas mensais
- Relatório de estoque
- Alertas de estoque baixo
- Encomendas por data
- Encomendas por status

## 🏗️ Arquitetura

O projeto segue arquitetura em 3 camadas:

```
src/main/java/com/padaria/
├── model/              # Entidades/Modelos
│   ├── Produto.java
│   ├── Estoque.java
│   ├── Cliente.java
│   ├── Fornecedor.java
│   ├── Venda.java
│   ├── ItemVenda.java
│   ├── Encomenda.java
│   └── ItemEncomenda.java
├── dao/                # Data Access Objects (JDBC)
│   ├── ProdutoDao.java
│   ├── EstoqueDao.java
│   ├── ClienteDao.java
│   ├── FornecedorDao.java
│   ├── VendaDao.java
│   └── EncomendaDao.java
├── service/            # Lógica de Negócio
│   ├── ProdutoService.java
│   ├── EstoqueService.java
│   ├── ClienteService.java
│   ├── FornecedorService.java
│   ├── VendaService.java
│   ├── EncomendaService.java
│   └── RelatorioService.java
└── controller/         # Controladores
    ├── api/           # REST Controllers
    │   ├── ProdutoRestController.java
    │   ├── EstoqueRestController.java
    │   ├── ClienteRestController.java
    │   ├── FornecedorRestController.java
    │   ├── VendaRestController.java
    │   ├── EncomendaRestController.java
    │   └── RelatorioRestController.java
    └── web/           # MVC Controllers
        ├── HomeController.java
        ├── ProdutoWebController.java
        └── EstoqueWebController.java
```

## 📊 Modelo de Dados

### Tabelas Principais:
- `produtos` - Produtos da padaria
- `estoque` - Controle de estoque
- `clientes` - Cadastro de clientes
- `fornecedores` - Cadastro de fornecedores
- `vendas` - Registro de vendas
- `itens_venda` - Itens de cada venda
- `encomendas` - Pedidos especiais
- `itens_encomenda` - Itens de cada encomenda

## 🔧 Configuração e Instalação

### Pré-requisitos
- Java 17 ou superior
- MySQL 8 ou superior
- Maven 3.6 ou superior

### Passos para Instalação

1. **Clone o repositório:**
```bash
git clone https://github.com/danircarv/gerenciamento-padaria.git
cd gerenciamento-padaria
```

2. **Configure o banco de dados MySQL:**

Crie o banco de dados:
```sql
CREATE DATABASE padaria_db;
```

Execute o script de schema (disponível em `src/main/resources/schema.sql`) para criar as tabelas e dados de exemplo.

3. **Configure as credenciais do banco:**

Edite o arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/padaria_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

4. **Compile e execute:**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Acesse o sistema:**
- Interface Web: http://localhost:8080
- API REST: http://localhost:8080/api

## 🌐 Interface Web

O sistema possui interface web desenvolvida com Thymeleaf:

- **Página Inicial**: Dashboard com acesso a todos os módulos
- **Produtos**: Listagem, cadastro e edição de produtos
- **Estoque**: Visualização e controle de estoque
- **Alertas**: Produtos com estoque abaixo do mínimo

## 🔌 API REST

### Endpoints Principais

#### Produtos
```
GET    /api/produtos              - Listar todos os produtos
GET    /api/produtos/ativos       - Listar produtos ativos
GET    /api/produtos/{id}         - Buscar produto por ID
GET    /api/produtos/categoria/{categoria} - Buscar por categoria
POST   /api/produtos              - Criar novo produto
PUT    /api/produtos/{id}         - Atualizar produto
DELETE /api/produtos/{id}         - Excluir produto
PATCH  /api/produtos/{id}/inativar - Inativar produto
```

#### Estoque
```
GET    /api/estoque               - Listar todo o estoque
GET    /api/estoque/alertas       - Listar estoque baixo
GET    /api/estoque/{id}          - Buscar por ID
GET    /api/estoque/produto/{produtoId} - Buscar estoque do produto
POST   /api/estoque               - Criar estoque
PUT    /api/estoque/{id}          - Atualizar estoque
PATCH  /api/estoque/produto/{produtoId}/adicionar?quantidade={qtd} - Adicionar quantidade
PATCH  /api/estoque/produto/{produtoId}/remover?quantidade={qtd} - Remover quantidade
DELETE /api/estoque/{id}          - Excluir estoque
```

#### Vendas
```
GET    /api/vendas                - Listar todas as vendas
GET    /api/vendas/{id}           - Buscar venda por ID
GET    /api/vendas/periodo?inicio={data}&fim={data} - Buscar por período
POST   /api/vendas                - Registrar nova venda
DELETE /api/vendas/{id}           - Excluir venda
GET    /api/vendas/total?inicio={data}&fim={data} - Total de vendas no período
```

#### Clientes
```
GET    /api/clientes              - Listar todos os clientes
GET    /api/clientes/{id}         - Buscar cliente por ID
GET    /api/clientes/cpf/{cpf}    - Buscar por CPF
GET    /api/clientes/buscar?nome={nome} - Buscar por nome
POST   /api/clientes              - Criar novo cliente
PUT    /api/clientes/{id}         - Atualizar cliente
DELETE /api/clientes/{id}         - Excluir cliente
```

#### Fornecedores
```
GET    /api/fornecedores          - Listar todos os fornecedores
GET    /api/fornecedores/ativos   - Listar fornecedores ativos
GET    /api/fornecedores/{id}     - Buscar por ID
POST   /api/fornecedores          - Criar novo fornecedor
PUT    /api/fornecedores/{id}     - Atualizar fornecedor
DELETE /api/fornecedores/{id}     - Excluir fornecedor
PATCH  /api/fornecedores/{id}/inativar - Inativar fornecedor
```

#### Encomendas
```
GET    /api/encomendas            - Listar todas as encomendas
GET    /api/encomendas/{id}       - Buscar por ID
GET    /api/encomendas/status/{status} - Buscar por status
GET    /api/encomendas/entrega/{data} - Buscar por data de entrega
POST   /api/encomendas            - Criar nova encomenda
PUT    /api/encomendas/{id}       - Atualizar encomenda
PATCH  /api/encomendas/{id}/status?status={status} - Atualizar status
DELETE /api/encomendas/{id}       - Excluir encomenda
```

#### Relatórios
```
GET    /api/relatorios/vendas/diario?data={data} - Relatório de vendas diário
GET    /api/relatorios/vendas/mensal?ano={ano}&mes={mes} - Relatório mensal
GET    /api/relatorios/estoque    - Relatório completo de estoque
GET    /api/relatorios/encomendas - Relatório de encomendas por status
GET    /api/relatorios/encomendas/dia?data={data} - Encomendas do dia
```

## 📝 Exemplos de Uso da API

### Criar um Produto
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pão Francês",
    "descricao": "Pão francês tradicional",
    "preco": 0.50,
    "categoria": "Pães",
    "unidadeMedida": "unidade",
    "ativo": true
  }'
```

### Registrar uma Venda
```bash
curl -X POST http://localhost:8080/api/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "formaPagamento": "Dinheiro",
    "observacoes": "Venda balcão",
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 10
      },
      {
        "produtoId": 2,
        "quantidade": 2
      }
    ]
  }'
```

### Criar uma Encomenda
```bash
curl -X POST http://localhost:8080/api/encomendas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "dataEntrega": "2024-12-25",
    "valorEntrada": 50.00,
    "observacoes": "Festa de aniversário",
    "itens": [
      {
        "produtoId": 4,
        "quantidade": 2,
        "observacoes": "Cobertura de chocolate"
      }
    ]
  }'
```

## 🎯 Funcionalidades Especiais

- **Validações de Negócio**: Todas as operações são validadas no Service layer
- **Transações**: Operações complexas são transacionais
- **Integração Estoque-Vendas**: Ao registrar uma venda, o estoque é atualizado automaticamente
- **Alertas Automáticos**: Sistema identifica produtos com estoque abaixo do mínimo
- **Cálculos Automáticos**: Totais de vendas e encomendas são calculados automaticamente
- **JDBC Puro**: Uso de JdbcTemplate sem JPA, conforme especificação

## 📦 Estrutura de Pacotes

```
com.padaria
├── PadariaApplication.java       # Classe principal
├── controller/
│   ├── api/                      # REST Controllers
│   └── web/                      # MVC Controllers
├── dao/                          # Data Access Objects
├── model/                        # Entidades
└── service/                      # Lógica de negócio
```

## 🛡️ Segurança e Validações

- Validação de dados obrigatórios
- Validação de CPF/CNPJ únicos
- Verificação de estoque antes de vendas
- Validação de valores negativos
- Tratamento de exceções personalizado
- Mensagens de erro amigáveis

## 🔄 Fluxo de uma Venda

1. Cliente realiza pedido
2. Sistema valida disponibilidade em estoque
3. Calcula preços e totais automaticamente
4. Registra venda no banco
5. Atualiza estoque automaticamente
6. Retorna confirmação da venda

## 🚀 Próximas Melhorias Sugeridas

- [ ] Autenticação e autorização com Spring Security
- [ ] Dashboard com gráficos e estatísticas
- [ ] Exportação de relatórios em PDF/Excel
- [ ] Sistema de notificações por email
- [ ] Histórico de preços dos produtos
- [ ] Controle de caixa e fechamento diário
- [ ] Integração com impressora fiscal
- [ ] App mobile para pedidos

## 📄 Licença

Este projeto é de código aberto e está disponível para fins educacionais.

## 👥 Autor

Desenvolvido para o projeto de Sistema de Gestão de Padaria.

---

**Sistema Gestão Padaria** - Automatizando processos e facilitando a gestão do seu negócio! 🥖
