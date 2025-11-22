# 🚀 Vaga AI API

API RESTful desenvolvida com Spring Boot 3 para gerenciamento de uma plataforma de vagas inteligente. A aplicação serve como backend para um aplicativo mobile, gerenciando autenticação, dados de usuários, currículos e preferências, integrando-se a um modelo de IA externo.

## 🛠️ Tecnologias Utilizadas

* **Java 17** & **Spring Boot 3.3.5**
* **Banco de Dados:** Oracle Database 21c XE
* **Segurança:** Spring Security + JWT (JSON Web Token)
* **Mensageria:** RabbitMQ
* **Cache:** Redis
* **Documentação:** Swagger UI (SpringDoc OpenAPI)
* **Containerização:** Docker & Docker Compose

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas robusta:
* **Controller:** Endpoints REST, documentados via Swagger.
* **Service:** Regras de negócio, validações e disparo de eventos.
* **Repository:** Abstração de acesso a dados (Spring Data JPA).
* **Security:** Filtros e configurações de autenticação Stateless.
* **Messaging:** Produtores e Consumidores assíncronos.

### Funcionalidades Principais
* ✅ **CRUD Completo:** Usuários, Filtros de Vagas, Vagas Favoritas e Currículos.
* ✅ **Autenticação Robusta:** Login, Registro e RBAC (Role-Based Access Control).
* ✅ **Notificações Real-Time:** Integração RabbitMQ -> SSE (Server-Sent Events) para avisar o usuário mobile.
* ✅ **Performance:** Cache automático com Redis para dados de leitura frequente.
* ✅ **Internacionalização (i18n):** Suporte nativo a PT-BR e EN-US.

## 🚀 Como Executar

### Pré-requisitos
* Docker & Docker Compose instalados.
* (Opcional) Java 17 e Maven instalados localmente.

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/Barros263inf/api_vaga_ai.git
    cd api_vaga_ai
    ```

2.  **Gere o pacote da aplicação:**
    ```bash
    # Linux/Mac
    ./mvnw clean package -DskipTests
    
    # Windows
    mvnw.cmd clean package -DskipTests
    ```

3.  **Suba a infraestrutura (Orquestração):**
    Isso iniciará o Oracle, RabbitMQ, Redis e a API simultaneamente.
    ```bash
    docker-compose up --build
    ```

4.  **Acesse a aplicação:**
    * **API Base:** `http://localhost:8080`
    * **Documentação (Swagger):** `http://localhost:8080/swagger-ui/index.html`
    * **RabbitMQ Management:** `http://localhost:15672` (Login: `guest`/`guest`)

## 🧪 Testando os Endpoints

### 1. Usuário Admin Padrão
Ao iniciar pela primeira vez, o sistema cria automaticamente:
* **Email:** `admin@vaga.ai`
* **Senha:** `admin123`

### 2. Fluxo de Autenticação
1.  Faça um `POST` em `/api/auth/login` com as credenciais acima.
2.  Copie o `token` retornado.
3.  No Swagger (botão Authorize) ou no Postman (Header Authorization), use: `Bearer SEU_TOKEN`.

### 3. Notificações em Tempo Real (SSE)
Para testar o recebimento de notificações do RabbitMQ:
1.  Faça uma requisição `GET` em `/api/notifications/subscribe` (com o Token).
2.  A conexão ficará aberta.
3.  Realize uma ação (ex: Criar um novo Filtro).
4.  Você receberá um evento JSON na conexão aberta.

## 📂 Estrutura de Pastas
```
src/main/java/com/vaga/ai/gs 
    ├── config/ # Configurações (Swagger, Cache, RabbitMQ, i18n) 
    ├── controller/ # Endpoints REST 
    ├── dto/ # Objetos de transferência de dados (Records) 
    ├── event/ # Eventos de domínio (Spring Events) 
    ├── exception/ # Tratamento global de erros 
    ├── messaging/ # Produtores e Consumidores RabbitMQ 
    ├── model/ # Entidades JPA 
    ├── repository/ # Interfaces de Banco de Dados 
    ├── security/ # Configuração de JWT e Filtros 
    └── service/ # Regras de Negócio
```

## 🤝 Contribuição

Este projeto é uma entrega acadêmica/MVP. Sinta-se à vontade para abrir Issues ou Pull Requests para melhorias.