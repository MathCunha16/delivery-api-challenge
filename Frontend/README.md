# 🍔 Delivery API - Frontend

Bem-vindo ao repositório Frontend do meu projeto para o Desafio Técnico Coco Bambu! 🚀 
Construí esta aplicação para ser a interface principal de um sistema robusto de gestão de pedidos de delivery. Meu objetivo principal foi criar uma experiência de usuário (UX) fluida, reativa e visualmente agradável, dividida entre uma visão simulada de cliente (para criar os pedidos) e um painel de parceiro (para gerenciar e evoluir o fluxo desses pedidos).

---

## 🛠️ Tecnologias Utilizadas

Para garantir performance e uma base de código manutenível e tipada, escolhi a dedo a stack deste projeto:

- **[React](https://react.dev/) (v18)**: Biblioteca base para a construção das interfaces declarativas.
- **[TypeScript](https://www.typescriptlang.org/)**: Tipagem estática para evitar bugs em tempo de execução e melhorar o intellisense, fundamental ao consumir os DTOs do backend.
- **[Vite](https://vitejs.dev/)**: O bundler/build tool ultrarrápido que escolhi no lugar do Create React App ou Webpack, focando em *Hot Module Replacement (HMR)* instantâneo durante o desenvolvimento.
- **[Tailwind CSS](https://tailwindcss.com/)**: Framework utility-first para entregar um design moderno, escuro (*Dark Mode* por padrão) e responsivo sem precisar sair do arquivo TSX.
- **[React Router DOM](https://reactrouter.com/)**: Para roteamento client-side limpo e baseado em SPA (Single Page Application).
- **[Lucide React](https://lucide.dev/)**: Biblioteca leve e bonita de ícones SVG.
- **Docker & Nginx**: Otimização completa do ambiente de produção utilizando *multi-stage builds*.

---

## ✨ Principais Funcionalidades (Features)

A aplicação engloba todos os desafios propostos, trazendo visual e interatividade em cima da API:

- 📊 **Dashboard de Parcerias (Listagem)**: Uma listagem interativa onde cada loja parceira pode visualizar os últimos pedidos com paginação diretamente do backend.
- 🛒 **Simulador de Criação de Pedido**: Um fluxo de checkout gamificado, permitindo a seleção de lojas, itens (com um mock interno), preenchimento dinâmico de endereços (com seleção de Estado em dropdown e cidade livre), finalizando com totalizadores dinâmicos.
- 🚦 **Máquina de Estados de Pedido**: Componentização visual (`StatusBadge`) e processamento real do fluxo de vida de um delivery. O usuário pode abrir um painel de detalhes arrastável e transitar o pedido por: `RECEBIDO` ➔ `CONFIRMADO` ➔ `DESPACHADO` ➔ `ENTREGUE` ou `CANCELADO`.
- ✏️ **Edição Fluida de Dados**: Modal dinâmico permitindo a edição rápida dos detalhes do cliente (telefone) e endereço de entrega assim que o pedido for recepcionado.
- 🚀 **Área de "Teste de Carga" (Performance Store Isolada)**: Criei uma estratégia arquitetural para ocultar a loja voltada para testes massivos do Gatling (`99999999...`) do fluxo normal de usuários, realocando-a em uma seção de destaque exclusiva ("Teste de Carga") no rodapé da página inicial.

---

## 📂 Arquitetura e Organização de Pastas

Organizei o código estruturando componentes por "Feature Slices" (*Feature-based architecture*), facilitando a legibilidade à medida que a aplicação escala:

```
src/
 ├── api/                # Instância base do Axios e Fetchers gerais
 ├── components/         # Componentes burros (Dumb) e globais (ex: Header)
 ├── context/            # Contextos do React (ex: StoreContext para loja atual)
 ├── features/           # Módulos principais separados por domínio lógico
 │    ├── stores/        # Regras, tipos e tela de seleção de lojas (StoreSelection)
 │    └── orders/        # Pedidos, fluxo, lista de entregas, edição e modais
 ├── pages/              # Rotas maiores (Home, Dashboard, Pagina de Novo Pedido)
 └── types/              # Tipagens globais TypeScript
```

---

## 📋 Pré-requisitos

Para rodar este projeto em sua máquina local, você precisará ter instalado:

- [Node.js](https://nodejs.org/) (versão 18+ recomendada)
- NPM ou Yarn

*Opcional para a etapa de containerização:*
- [Docker](https://www.docker.com/) e Docker Compose

---

## 🏃 Como Executar Localmente (Modo Desenvolvimento)

Siga os passos abaixo para rodar a versão de desenvolvimento na sua máquina:

1. Abra seu terminal e instale todas as dependências do projeto:
```bash
npm install
```

2. Execute o servidor de desenvolvimento do Vite:
```bash
npm run dev
```

3. O Vite vai levantar o servidor ultrarrápido. O projeto geralmente estará rodando na porta `5173`. Procure pelo link no terminal ou abra diretamente no seu navegador:

👉 **http://localhost:5173/** (ou 5174 caso a anterior esteja ocupada)

---

## 🐳 Execução via Docker (Modo Produção)

Para ter uma experiência mais próxima ao ambiente produtivo real, empacotei o app com um `Dockerfile` multi-stage: a primeira etapa (Node) cuida do build (minificação, treeshaking) e a segunda etapa (Nginx) provê o roteamento correto da SPA.

Na raiz da pasta `/Frontend`, execute:

```bash
docker compose up --build
```
*Acesse em [http://localhost:8080](http://localhost:8080)*

*(O container foi batizado de forma simplificada e mapeado no `docker-compose.yml` da raiz do projeto para rodar em sincronia total com o Backend em Java).*

---

## 📸 Screenshots e Telas da Aplicação

### 1. Tela Inicial e Seleção de Módulos
A página inicial foi segmentada para separar claramente o fluxo operacional do parceiro, o simulador de pedidos e a área isolada para testes de performance.

| Área do Parceiro | Simulador de Pedidos | Área de Teste de Carga |
|:---:|:---:|:---:|
| ![Área do Parceiro](../docs/home_area_parceiro.png) | ![Simulador](../docs/home_area_pedidos.png) | ![Teste de Carga](../docs/home_teste_de_carga.png) |

### 2. Fluxo do Simulador de Pedidos (Cliente)
O simulador guia o usuário desde a identificação até o pagamento, passando por endereço e seleção de itens com adicionais.

| Identificação | Endereço de Entrega | Cardápio | Pagamento |
|:---:|:---:|:---:|:---:|
| ![Identificação](../docs/criacao_pedido_identificacao_e1.png) | ![Endereço](../docs/criacao_pedido_endereco_e2.png) | ![Cardápio](../docs/criacao_pedidos_cardapio_e3.png) | ![Pagamento](../docs/criacao_pedido_pagamento_e4.png) |

*Detalhe do modal de condimentos e adicionais:*
![Modal de Item](../docs/condimentos_e_detalhes_item_e3_1.png)

### 3. Dashboard Operacional (Restaurante)
Visão geral dos pedidos da loja selecionada, com listagem, paginação e visualização clara dos status.

![Dashboard](../docs/dashboard_listagem_pedidos.png)

### 4. Detalhes do Pedido e Máquina de Estados
Tela principal para a operação da loja, permitindo visualizar itens, dados do cliente e, crucialmente, avançar o status do pedido (ex: de "Recebido" para "Confirmado") e editar informações de entrega.

![Detalhes do Pedido - Topo](../docs/info_pedido_1.png)
![Detalhes do Pedido - Rodapé](../docs/info_pedido_2.png)

---
Feito com ☕ e TypeScript por Matheus Cunha.
