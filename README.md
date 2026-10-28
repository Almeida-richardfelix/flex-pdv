# 🛒 Flex PDV

> **Sistema de Ponto de Venda (PDV/POS) ágil, moderno e focado na consistência de regras de negócio de varejo.**

Este repositório contém o protótipo funcional do **Flex PDV**, englobando uma API REST robusta (Backend) e uma interface de frente de caixa minimalista e focada em produtividade (Frontend).

## 🚀 Arquitetura do Projeto

O projeto adota uma arquitetura dividida em duas camadas principais:

- **`/backend`**: Desenvolvido em Java com Spring Boot, responsável pelas regras de negócio rigorosas (validação de caixa, baixa atômica de estoque, cálculo de troco) e persistência de dados.
- **`/frontend`**: Interface web minimalista simulando a tela clássica de um operador de caixa (foco em teclado e agilidade).

## ✨ Principais Funcionalidades

### 1. Ciclo de Vida do Caixa
- [ ] Abertura de sessão de caixa (com operador e saldo inicial/troco).
- [ ] Registro de movimentações avulsas (sangrias e suprimentos).
- [ ] Fechamento de caixa com conciliação de saldo (Dinheiro, PIX, Cartão de Crédito/Débito).

### 2. Frente de Caixa (Checkout)
- [ ] Leitura rápida de produtos por Código de Barras (SKU/EAN) ou ID interno.
- [ ] Cálculo automático de quantidades, subtotais e troco.
- [ ] Suporte a múltiplas formas de pagamento simultâneas na mesma venda.
- [ ] Cancelamento de itens individuais ou da venda (cupom) completa.

### 3. Retaguarda e Estoque
- [x] Cadastro e consulta de produtos por ID interno ou SKU/EAN.
- [ ] Baixa atômica de estoque (Garantia transacional `@Transactional`: o produto só é descontado se o pagamento for concluído com sucesso).
- [ ] Bloqueio e tratamento de exceção para vendas com saldo de estoque insuficiente.

## 🛠️ Tecnologias Utilizadas

**Backend (API REST):**
- Java 17
- Spring Boot 3
- Spring Data JPA / Hibernate
- MySQL
- Maven

**Frontend (Client):**
- HTML5, CSS3, JavaScript (Vanilla)
- Integração via Fetch API

## ⚙️ Como Executar o Backend localmente

Pré-requisitos: Java 17+, Maven 3.9+ e Docker Compose.

1. Inicie o MySQL:
   ```bash
   docker compose up -d mysql
   ```
2. Execute a API:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   A API ficará disponível em `http://localhost:8080`.

### API de produtos

- `POST /api/produtos`: cadastra um produto. Exemplo de corpo:
  ```json
  {
    "code": "7891234567890",
    "name": "Café",
    "price": 12.50,
    "stock": 8
  }
  ```
- `GET /api/produtos/{id}`: consulta pelo ID interno.
- `GET /api/produtos/codigo/{code}`: consulta pelo SKU/EAN.

As credenciais do MySQL no Compose são somente para desenvolvimento local. Para outra configuração, defina `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` e `DB_PASSWORD`.
