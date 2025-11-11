# Guia de Exemplos de API

Este documento demonstra como usar os endpoints REST API do Sistema de Gestão de Padaria.

## Produtos

### Criar um novo produto

```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pão Integral",
    "descricao": "Pão integral com grãos",
    "categoria": "PAES",
    "precoVenda": 12.50,
    "precoCusto": 7.00,
    "unidadeMedida": "UN",
    "ativo": true
  }'
```

### Listar todos os produtos

```bash
curl http://localhost:8080/api/produtos
```

### Buscar produto por ID

```bash
curl http://localhost:8080/api/produtos/1
```

### Atualizar produto

```bash
curl -X PUT http://localhost:8080/api/produtos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "nome": "Pão Francês",
    "descricao": "Pão francês tradicional crocante",
    "categoria": "PAES",
    "precoVenda": 0.70,
    "precoCusto": 0.35,
    "unidadeMedida": "UN",
    "ativo": true
  }'
```

## Estoque

### Criar registro de estoque

```bash
curl -X POST http://localhost:8080/api/estoque \
  -H "Content-Type: application/json" \
  -d '{
    "produtoId": 1,
    "quantidade": 200,
    "quantidadeMinima": 50,
    "quantidadeMaxima": 400,
    "localizacao": "Prateleira A1"
  }'
```

### Adicionar quantidade ao estoque

```bash
curl -X PATCH http://localhost:8080/api/estoque/produto/1/adicionar \
  -H "Content-Type: application/json" \
  -d '{
    "quantidade": 50
  }'
```

### Verificar alertas de estoque baixo

```bash
curl http://localhost:8080/api/estoque/alertas
```

## Clientes

### Criar cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pedro Henrique",
    "cpf": "567.890.123-44",
    "telefone": "(11) 95678-9012",
    "email": "pedro.henrique@email.com",
    "endereco": "Rua E, 555 - São Paulo, SP",
    "ativo": true
  }'
```

### Listar clientes ativos

```bash
curl http://localhost:8080/api/clientes/ativos
```

## Vendas

### Realizar uma venda

```bash
curl -X POST http://localhost:8080/api/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "formaPagamento": "DINHEIRO",
    "desconto": 0,
    "observacoes": "Cliente regular",
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 10,
        "precoUnitario": 0.60
      },
      {
        "produtoId": 4,
        "quantidade": 3,
        "precoUnitario": 4.50
      }
    ]
  }'
```

### Listar vendas por período

```bash
curl "http://localhost:8080/api/vendas/periodo?inicio=2025-01-01T00:00:00&fim=2025-12-31T23:59:59"
```

## Encomendas

### Criar uma encomenda

```bash
curl -X POST http://localhost:8080/api/encomendas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 2,
    "dataEntrega": "2025-12-25",
    "valorEntrada": 100.00,
    "status": "PENDENTE",
    "observacoes": "Encomenda para festa de Natal",
    "itens": [
      {
        "produtoId": 3,
        "quantidade": 5,
        "precoUnitario": 35.00
      },
      {
        "produtoId": 7,
        "quantidade": 3,
        "precoUnitario": 30.00
      }
    ]
  }'
```

### Atualizar status da encomenda

```bash
curl -X PATCH http://localhost:8080/api/encomendas/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "CONFIRMADA"
  }'
```

### Listar encomendas por status

```bash
curl http://localhost:8080/api/encomendas/status/PENDENTE
```

## Relatórios

### Relatório de vendas por período

```bash
curl "http://localhost:8080/api/relatorios/vendas/periodo?inicio=2025-01-01T00:00:00&fim=2025-12-31T23:59:59"
```

### Produtos mais vendidos

```bash
curl "http://localhost:8080/api/relatorios/produtos/mais-vendidos?inicio=2025-01-01T00:00:00&fim=2025-12-31T23:59:59"
```

### Relatório de estoque baixo

```bash
curl http://localhost:8080/api/relatorios/estoque/baixo
```

### Clientes mais ativos

```bash
curl http://localhost:8080/api/relatorios/clientes/ativos
```

## Testando com JavaScript (Frontend)

```javascript
// Exemplo de criação de venda usando Fetch API

async function realizarVenda() {
  const venda = {
    clienteId: 1,
    formaPagamento: "PIX",
    desconto: 5.00,
    itens: [
      {
        produtoId: 1,
        quantidade: 20,
        precoUnitario: 0.60
      },
      {
        produtoId: 6,
        quantidade: 10,
        precoUnitario: 2.50
      }
    ]
  };

  try {
    const response = await fetch('http://localhost:8080/api/vendas', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(venda)
    });

    const result = await response.json();
    console.log('Venda realizada:', result);
  } catch (error) {
    console.error('Erro ao realizar venda:', error);
  }
}
```

## Respostas de Erro

A API retorna mensagens de erro apropriadas:

```json
{
  "message": "Nome do produto é obrigatório"
}
```

Códigos HTTP:
- 200: Sucesso
- 201: Criado com sucesso
- 204: Sem conteúdo (sucesso em delete)
- 400: Requisição inválida
- 404: Recurso não encontrado
- 500: Erro interno do servidor
