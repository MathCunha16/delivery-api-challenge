# 🚀 Delivery API - Sistema de Gestão de Pedidos (Desafio Coco Bambu)

Bem-vindo ao repositório principal do Desafio Técnico para a vaga de Desenvolvedor. Este projeto é uma solução *Full-Stack* completa para gestão de pedidos de delivery, contendo simulador de compras, painel de parceiros, máquina de estados e testes de performance.

O ecossistema é dividido em duas aplicações principais, cada uma com sua própria documentação detalhada:

* ☕ **[Documentação do Backend (Java/Spring Boot) ➔](./Backend/README.md)**
* ⚛️ **[Documentação do Frontend (React/Vite) ➔](./Frontend/README.md)**
* 📋 **[Diário de Bordo e Backlog ➔](./BACKLOG.md)**
* 🗺️ **[Roadmap e Quadro Kanban (GitHub Projects) ➔](https://github.com/users/MathCunha16/projects/1)**

---

## 🐳 Como Executar o Projeto (Docker)

A maneira mais fácil e recomendada de rodar a aplicação é utilizando o Docker. Toda a infraestrutura (Banco de Dados PostgreSQL, API Java e Frontend React) foi conteinerizada para rodar com um único comando.

### Pré-requisitos
* **Linux / macOS:** Ter o `docker` e o `docker-compose` instalados.
* **Windows:** Ter o [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e rodando em segundo plano.

### Passo a Passo

1. Clone este repositório para a sua máquina.
2. Abra o terminal na **raiz do projeto** (onde está o arquivo `docker-compose.yml`).
3. Execute o comando de build e inicialização:

   *No Linux/macOS ou Windows (PowerShell/CMD):*
   ```bash
   docker compose up --build
   ```

> ⏳ **Aviso Importante:** A primeira execução pode levar alguns minutos. O Docker irá baixar as imagens base (Node, Eclipse Temurin Java, PostgreSQL), baixar todas as dependências do Gradle/NPM e compilar as aplicações. Nas próximas vezes, a inicialização será quase instantânea.

### 🌐 Acessando a Aplicação

Com os containers rodando, você pode acessar os serviços nos seguintes endereços:

* **Frontend (Interface Web):** [http://localhost:5173](http://localhost:5173)
* **Backend (Swagger / Documentação da API):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **Banco de Dados (PostgreSQL):** `localhost:5432`

---

## 💣 Testes de Carga (Gatling)

Este projeto inclui uma suíte robusta de testes de estresse para provar a resiliência da API sob cenários de alta concorrência. 

Para rodar a injeção massiva de milhares de pedidos, mantenha os containers rodando, abra um **novo terminal** e navegue até a pasta `/Backend`:

* **Linux / macOS:**
  ```bash
  ./gradlew gatlingRun
  ```
* **Windows:**
  ```cmd
  gradlew.bat gatlingRun
  ```
> 💡 *Para ver os resultados de latência e P99, consulte o [README do Backend](./Backend/README.md).*

---
Feito com ☕ por Matheus Cunha.