# 📚 ForumHub API

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de tópicos de um fórum, com **autenticação via JWT**, validações e operações completas de CRUD.

O projeto foi criado como solução para o Challenge do programa Oracle next Education + Alura, onde o objetivo é construir um backend capaz de gerenciar tópicos, usuários e autenticação.

## 🚀 Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- Bean Validation
- MySQL
- Lombok

## ⚙️ Funcionalidades

- Autenticação e geração de token (JWT)
- Cadastro de tópico (com validações)
- Listagem de tópicos (público)
- Detalhamento de tópico por id (público)
- Atualização de tópico (apenas autor, autenticado)
- Exclusão de tópico (apenas autor, autenticado)
- Regras para evitar duplicidade (título + mensagem)

## 🔐 Regras de acesso (Segurança)

- **Públicos (sem token):**
  - `POST /login`
  - `GET /topicos`
  - `GET /topicos/{id}`

- **Protegidos (com token Bearer):**
  - `POST /topicos`
  - `PUT /topicos/{id}`
  - `DELETE /topicos/{id}`

Além disso, **apenas o autor** do tópico pode **editar** ou **excluir**.

---

# 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

-  **Java 17 ou superior**
-  **MySQL** ou outro banco relacional compatível
-  **Postman** ou **Insomnia** para testar os endpoints

---

# ▶️ Como rodar o projeto

## 1️⃣ Clonar o repositório para sua pasta desejada:

```bash
git clone https://github.com/mands-mands/ForumHub.git
```

---

## 2️⃣ Configurar o banco de dados

Antes de iniciar a aplicação, crie o banco de dados no MySQL (ou outro banco relacional).

Exemplo utilizando MySQL:

```sql
CREATE DATABASE forumhub;
```

Depois configure a conexão no arquivo:

`src/main/resources/application.properties`

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forumhub
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

⚠️ **Observação**

Por questões de segurança, é recomendado utilizar **variáveis de ambiente** para armazenar as credenciais do banco de dados, em vez de deixá-las diretamente no arquivo de configuração.

Exemplo utilizando variáveis de ambiente:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forumhub
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

---

## 🔐 Configuração do JWT

A aplicação utiliza **JWT (JSON Web Token)** para autenticação.

Para gerar a chave secreta utilizada na assinatura dos tokens, execute no terminal:

```bash
openssl rand -base64 48
```
Esse comando irá gerar uma **chave segura** para assinatura dos tokens.

Após gerar a chave, salve-a em uma variável de ambiente.

### ⚠️ Usuários Windows

O comando acima utiliza **OpenSSL**, que normalmente já vem instalado em sistemas **Linux e Mac**.

No **Windows**, caso o comando não funcione, é necessário instalar o OpenSSL.

Você pode baixar o instalador em:

https://slproweb.com/products/Win32OpenSSL.html


Após instalar, reinicie o terminal e execute novamente o comando:

`openssl rand -base64 48`

---

### ⚙️ Configuração no Spring Boot

Configure o `application.properties` para utilizar a variável de ambiente:

```properties
api.security.token.secret=${JWT_SECRET}
```

⚠️ **Importante**

Nunca salve a chave secreta diretamente no repositório.  
Utilizar **variáveis de ambiente** evita exposição de informações sensíveis.

---

## 👤 Criar usuário para autenticação

Para utilizar o endpoint `/login`, é necessário que exista um usuário cadastrado no banco de dados.

Exemplo de inserção manual:

```sql
INSERT INTO usuarios (login, senha)
VALUES ('admin', 'SENHA_CRIPTOGRAFADA');
```

⚠️ **Importante**

A senha deve estar **criptografada com BCrypt**, pois o Spring Security utiliza esse padrão para autenticação.

Exemplo de senha criptografada:

```
$2a$10$7EqJtq98hPqEX7fNZaFWoO
```

---

## 🚀 Executar a aplicação

Execute a aplicação

A API ficará disponível em:

```
http://localhost:8080
```
---

# 🧪 Testando a API com Insomnia

Você pode testar os endpoints da API utilizando o **Insomnia** (ou Postman).

Após iniciar a aplicação, crie um novo projeto no Insomnia e configure as seguintes requisições:

## Endpoints disponíveis

| Método | Endpoint |
|------|------|
| POST | `http://localhost:8080/login` |
| POST | `http://localhost:8080/topicos` |
| GET | `http://localhost:8080/topicos` |
| GET | `http://localhost:8080/topicos/{id}` |
| PUT | `http://localhost:8080/topicos/{id}` |
| DELETE | `http://localhost:8080/topicos/{id}` |

---

## 🔑 Fazendo login

Para cadastrar, editar ou deletar tópicos é necessário **realizar login primeiro**.

Endpoint:

POST: `http://localhost:8080/login`


### Body da requisição

No Insomnia, selecione:

Body -> JSON


E envie o seguinte JSON:

```json
{
  "login": "usuario@email.com",
  "senha": "sua_senha"
}
```

Resposta da API

Se as credenciais estiverem corretas, a API retornará um token JWT.

Exemplo:

```json
{
  "token": "seu_token_aqui"
}
```

---

### 🔐 Usando o token nas requisições protegidas

Para acessar os endpoints protegidos (POST, PUT, DELETE de tópicos), é necessário utilizar o token gerado no login.

No Insomnia:


1. Copie o token retornado no login
2. Vá na aba Auth
3. Selecione Bearer Token
4. Cole o token no campo Token

Após isso, será possível realizar as seguintes operações:

- Criar tópico
- Editar tópico
- Deletar tópico

Os endpoints abaixo não precisam de autenticação:

GET /topicos

GET /topicos/{id}

---

# ✅ API pronta para testes

Após realizar todas as configurações e iniciar a aplicação, a API estará pronta para ser utilizada e testada através de ferramentas como Insomnia ou Postman.

---

## 🎓 Agradecimentos

Gostaria de agradecer:
- **Oracle** e **Alura** pela oportunidade de participar do programa **Oracle Next Education (ONE)**
- A toda equipe de instrutores e suporte que tornaram este aprendizado possível
- À comunidade da Alura, sempre disposta a ajudar

**⭐ Se este projeto te ajudou, considere dar uma estrela! ⭐**
