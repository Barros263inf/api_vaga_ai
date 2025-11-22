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
* ✅ **Notificações Real-Time:** Integração RabbitMQ → SSE (Server-Sent Events) para avisar o usuário mobile.
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
1.  Faça uma requisição `GET` em `/api/notification/subscribe` (com o Token).
2.  A conexão ficará aberta.
3.  Realize uma ação (ex: Criar um novo Filtro).
4.  Você receberá um evento JSON na conexão aberta.

## 📋 Exemplos de Requisições

### 🔐 Autenticação

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@vaga.ai",
  "password": "admin123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Registro de Novo Usuário
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "password": "senha12345",
  "phone": "11987654321",
  "role": "USER"
}
```

**Resposta:**
```json
{
  "id": 2,
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "phone": "11987654321"
}
```

---

### 👤 Usuários (Requer Role ADMIN)

#### Listar Todos os Usuários
```http
GET /api/users?page=0&size=10&sort=name
Authorization: Bearer {seu_token}
```

**Resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Administrador Padrão",
      "email": "admin@vaga.ai",
      "phone": "11999990000"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1
}
```

#### Buscar Usuário por ID
```http
GET /api/users/1
Authorization: Bearer {seu_token}
```

**Resposta:**
```json
{
  "id": 1,
  "name": "Administrador Padrão",
  "email": "admin@vaga.ai",
  "phone": "11999990000"
}
```

#### Criar Novo Usuário (Admin)
```http
POST /api/users
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "name": "Maria Santos",
  "email": "maria.santos@email.com",
  "password": "senha123456",
  "phone": "11976543210",
  "role": "USER"
}
```

#### Atualizar Usuário
```http
PUT /api/users/2
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "name": "Maria Santos Silva",
  "phone": "11988887777"
}
```

#### Deletar Usuário
```http
DELETE /api/users/2
Authorization: Bearer {seu_token}
```

---

### 🔍 Filtros de Vagas

#### Listar Filtros do Usuário
```http
GET /api/filters?page=0&size=10
Authorization: Bearer {seu_token}
```

**Resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "userId": 2,
      "title": "Desenvolvedor Java",
      "location": "São Paulo",
      "jobType": "PLENO",
      "salaryMin": 5000.00,
      "salaryMax": 8000.00,
      "remotePreference": "HYBRID",
      "experienceLevel": "3-5 anos"
    }
  ]
}
```

#### Buscar Filtro por ID
```http
GET /api/filters/1
Authorization: Bearer {seu_token}
```

#### Criar Novo Filtro
```http
POST /api/filters
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "title": "Desenvolvedor Java",
  "location": "São Paulo",
  "jobType": "PLENO",
  "salaryMin": 5000.00,
  "salaryMax": 8000.00,
  "remotePreference": "HYBRID",
  "experienceLevel": "3-5 anos"
}
```

**Resposta:**
```json
{
  "id": 1,
  "userId": 2,
  "title": "Desenvolvedor Java",
  "location": "São Paulo",
  "jobType": "PLENO",
  "salaryMin": 5000.00,
  "salaryMax": 8000.00,
  "remotePreference": "HYBRID",
  "experienceLevel": "3-5 anos"
}
```

**Valores aceitos:**
- `jobType`: `ESTAGIO`, `TRAINEE`, `JUNIOR`, `PLENO`, `SENIOR`
- `remotePreference`: `OFFICE`, `REMOTE`, `HYBRID`

#### Atualizar Filtro
```http
PUT /api/filters/1
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "salaryMin": 6000.00,
  "salaryMax": 10000.00,
  "remotePreference": "REMOTE"
}
```

#### Deletar Filtro
```http
DELETE /api/filters/1
Authorization: Bearer {seu_token}
```

---

### 💼 Vagas Favoritas

#### Listar Vagas Salvas
```http
GET /api/jobs?page=0&size=10
Authorization: Bearer {seu_token}
```

**Resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "jobApiId": "ext_123456",
      "jobTitle": "Desenvolvedor Java Sênior",
      "companyName": "Tech Solutions LTDA",
      "location": "São Paulo - SP",
      "description": "Vaga para desenvolver sistemas corporativos...",
      "salaryInfo": 12000.00,
      "redirectUrl": "https://exemplo.com/vaga/123456",
      "savedAt": "2024-11-22T10:30:00"
    }
  ]
}
```

#### Buscar Vaga por ID
```http
GET /api/jobs/1
Authorization: Bearer {seu_token}
```

#### Salvar Nova Vaga
```http
POST /api/jobs
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "jobApiId": "ext_123456",
  "jobTitle": "Desenvolvedor Java Sênior",
  "companyName": "Tech Solutions LTDA",
  "location": "São Paulo - SP",
  "description": "Vaga para desenvolver sistemas corporativos usando Java e Spring Boot",
  "salaryInfo": 12000.00,
  "redirectUrl": "https://exemplo.com/vaga/123456"
}
```

**Resposta:**
```json
{
  "id": 1,
  "jobApiId": "ext_123456",
  "jobTitle": "Desenvolvedor Java Sênior",
  "companyName": "Tech Solutions LTDA",
  "location": "São Paulo - SP",
  "description": "Vaga para desenvolver sistemas corporativos usando Java e Spring Boot",
  "salaryInfo": 12000.00,
  "redirectUrl": "https://exemplo.com/vaga/123456",
  "savedAt": "2024-11-22T10:30:00"
}
```

#### Remover Vaga Favorita
```http
DELETE /api/jobs/1
Authorization: Bearer {seu_token}
```

---

### 📄 Currículos

#### Listar Currículos do Usuário
```http
GET /api/resumes?page=0&size=5
Authorization: Bearer {seu_token}
```

**Resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "fileName": "curriculo_joao_silva.pdf",
      "filePath": "/uploads/curriculos/2024/curriculo_joao_silva.pdf",
      "extractedText": "João Silva\nDesenvolvedor Java\nExperiência: 5 anos...",
      "extractedSkills": "[\"Java\", \"Spring Boot\", \"Docker\", \"AWS\"]",
      "createdAt": "2024-11-22T09:15:00"
    }
  ]
}
```

#### Buscar Currículo por ID
```http
GET /api/resumes/1
Authorization: Bearer {seu_token}
```

#### Upload de Novo Currículo
```http
POST /api/resumes
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "fileName": "curriculo_joao_silva.pdf",
  "filePath": "/uploads/curriculos/2024/curriculo_joao_silva.pdf",
  "extractedText": "João Silva\nDesenvolvedor Java\nExperiência: 5 anos em desenvolvimento backend com Spring Boot, Microserviços e Cloud Computing.",
  "extractedSkills": "[\"Java\", \"Spring Boot\", \"Docker\", \"AWS\", \"PostgreSQL\"]"
}
```

**Resposta:**
```json
{
  "id": 1,
  "fileName": "curriculo_joao_silva.pdf",
  "filePath": "/uploads/curriculos/2024/curriculo_joao_silva.pdf",
  "extractedText": "João Silva\nDesenvolvedor Java\nExperiência: 5 anos...",
  "extractedSkills": "[\"Java\", \"Spring Boot\", \"Docker\", \"AWS\", \"PostgreSQL\"]",
  "createdAt": "2024-11-22T09:15:00"
}
```

#### Deletar Currículo
```http
DELETE /api/resumes/1
Authorization: Bearer {seu_token}
```

---

### 🔔 Notificações (SSE)

#### Conectar ao Stream de Notificações
```http
GET /api/notification/subscribe
Authorization: Bearer {seu_token}
Accept: text/event-stream
```

**Resposta (Stream contínuo):**
```
data:notification
data:{"to":"joao.silva@email.com","subject":"Novo Filtro Criado","body":"Você criou o filtro: Desenvolvedor Java"}

data:notification
data:{"to":"joao.silva@email.com","subject":"Nova Vaga","body":"Você favoritou: Desenvolvedor Java Sênior"}
```

---

## 📂 Estrutura de Pastas
```
src/main/java/com/vaga/ai/gs 
    ├── config/           # Configurações (Swagger, Cache, RabbitMQ, i18n) 
    ├── controller/       # Endpoints REST 
    ├── dto/              # Objetos de transferência de dados (Records) 
    ├── event/            # Eventos de domínio (Spring Events) 
    ├── exception/        # Tratamento global de erros 
    ├── messaging/        # Produtores e Consumidores RabbitMQ 
    ├── model/            # Entidades JPA 
    ├── repository/       # Interfaces de Banco de Dados 
    ├── security/         # Configuração de JWT e Filtros 
    └── service/          # Regras de Negócio
```

## 📊 Modelo de Dados

### Entidades Principais

- **User (TB_USUARIOS)**: Usuários do sistema com autenticação JWT
- **Filter (TB_FILTROS)**: Filtros de busca de vagas personalizados
- **Job (TB_VAGAS)**: Vagas favoritas salvas pelos usuários
- **Resume (TB_CURRICULOS)**: Currículos com texto extraído e skills

## 🔒 Segurança

A API utiliza autenticação stateless com JWT. Cada requisição autenticada deve incluir o header:

```
Authorization: Bearer {token_jwt}
```

### Roles Disponíveis:
- **USER**: Acesso aos próprios recursos (filtros, vagas, currículos)
- **ADMIN**: Acesso total, incluindo gerenciamento de usuários

## 🌐 Internacionalização

A API suporta mensagens em múltiplos idiomas através do header:

```
Accept-Language: pt-BR
Accept-Language: en-US
```

## ⚡ Performance e Cache

- **Redis** é utilizado para cache de:
  - Usuários (`@Cacheable`)
  - Filtros de vagas
  - Vagas salvas
- **TTL padrão**: 10 minutos

## 🐰 Mensageria

O RabbitMQ é utilizado para processamento assíncrono de:
- E-mails de boas-vindas
- Notificações de eventos (criação de filtros, vagas favoritas)
- Integração com SSE para notificações em tempo real

## 🤝 Contribuição

Este projeto é uma entrega acadêmica/MVP. Sinta-se à vontade para abrir Issues ou Pull Requests para melhorias.

## 📝 Licença

Apache 2.0

---

**Desenvolvido com ❤️ para a comunidade de desenvolvedores**