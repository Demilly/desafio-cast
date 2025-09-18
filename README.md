# 🏦 BankApp - Documentação

[![Java](https://img.shields.io/badge/Java-8-blue)](https://www.oracle.com/java/)  
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7.18-green)](https://spring.io/projects/spring-boot)  
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-orange)](https://www.thymeleaf.org/)  
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple)](https://getbootstrap.com/)  
[![Status](https://img.shields.io/badge/status-active-brightgreen)](#)

---

## 📖 Descrição

O **BankApp** é um sistema simples de gerenciamento de contas bancárias, desenvolvido com **Spring Boot**, **Java 8**, **Thymeleaf** e **Bootstrap**.  
Ele permite o gerenciamento de contas, movimentações financeiras (crédito, débito, transferências) e fornece uma interface amigável tanto para **Clientes** quanto para **Administradores**.

A aplicação é:

- **Segura:** com autenticação por perfil (Admin / Cliente) e feedback de login incorreto.
- **Responsiva:** interface adaptada para diferentes dispositivos.
- **Configuração por perfil:** suporte a ambientes **local**, **homol** e **prod**, cada um com banco e configurações diferentes.
- **Docker-friendly:** pode ser executada via Docker sem precisar instalar Java ou banco local.

## 📦 Dependências Principais

O projeto utiliza as seguintes dependências:

- **Spring Boot 2.7.18** → Framework principal para desenvolvimento rápido de aplicações Java.
- **Spring Data JPA** → Abstração para persistência de dados com Hibernate.
- **Hibernate** → ORM para mapeamento objeto-relacional.
- **Spring Security** → Autenticação e autorização de usuários.
- **Thymeleaf 3.1** → Motor de templates para renderização das páginas HTML.
- **Bootstrap 5.3** → Estilização responsiva do front-end.
- **H2 Database** → Banco de dados em memória usado em ambiente local/testes.
- **MySQL Driver** → Conector JDBC para banco de dados MySQL.
- **Lombok** → Reduz boilerplate de código (getters, setters, builders, etc).
- **JUnit 5** → Framework de testes unitários.
- **Mockito** → Framework de mock para testes.

---

## 🔑 Login

- **Admin:** `admin / admin123`
- **Cliente:** `cliente / cliente123`

💡 **Erro de login** é exibido diretamente na tela se usuário ou senha forem inválidos.

---

## 🌐 Perfis e Bancos

A aplicação possui **3 perfis principais**, definidos via `spring.config.activate.on-profile`:

| Perfil  | Banco | Usuário | Senha | Observações |
|---------|-------|---------|-------|-------------|
| `local` | H2 (in-memory) | `sa` | (nenhuma) | Banco embarcado para desenvolvimento rápido |
| `homol` | MySQL | `root` | (nenhuma) | Ambiente de homologação |
| `prod`  | MySQL | Config via variáveis de ambiente | Config via variáveis | Produção completa, segura e parametrizada |

### Exemplo de configuração Homol (`application-homol.yml`):

```yaml
spring:
  config:
    activate:
      on-profile: homol
  application:
    name: bankapp
  datasource:
    url: jdbc:mysql://localhost:3306/banco_desafio?useSSL=false&serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
  thymeleaf:
    cache: true
    prefix: classpath:/templates/
    suffix: .html
    mode: HTML

server:
  error:
    whitelabel:
      enabled: false
    path: /error
````

## 🖥️ Telas Interativas

### Cliente - Operações

- Funcionalidades para:
    - Crédito
    - Débito
    - Transferência

---

### Administrador - Listar Contas

- Mostra todas as contas com saldo e proprietário.
- Possibilidade de criar nova conta

---

### Administrador - Criar Conta

- Validações interativas:
    - Número: somente dígitos
    - Nome: mínimo 15 caracteres
    - Saldo inicial: ≥ 0
- Feedback de erro aparece imediatamente se algum campo estiver incorreto.

---

## 🚀 Endpoints e Exemplos

### Admin

| Método | Endpoint              | Descrição          | Exemplo JSON                                                                |
|--------|-----------------------|--------------------|-----------------------------------------------------------------------------|
| GET    | `/admin/accounts`     | Listar contas      | -                                                                           |
| GET    | `/admin/accounts/new` | Formulário criação | -                                                                           |
| POST   | `/admin/accounts`     | Criar conta        | `{ "number": 1, "ownerName": "Fulano da Silva Junior", "balance": 100.00 }` |

### Cliente

| Método | Endpoint           | Descrição               | Exemplo JSON                                            |
|--------|--------------------|-------------------------|---------------------------------------------------------|
| POST   | `/client/credit`   | Realizar crédito        | `{ "accountNumber": 1, "amount": 50.00 }`               |
| POST   | `/client/debit`    | Realizar débito         | `{ "accountNumber": 1, "amount": 20.00 }`               |
| POST   | `/client/transfer` | Transferir entre contas | `{ "fromAccount": 1, "toAccount": 2, "amount": 30.00 }` |

---


## 🧪 Testes

- Unitários: **JUnit + Mockito**
- Integrados com coberturas de:
    - Application
    - Domain
    - Controller
    - Web
---

## ⚙️ Como Rodar

```bash
git clone https://github.com/Demilly/desafio-cast.git
cd case
./mvnw spring-boot:run


