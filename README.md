# 🏦 BankApp - Documentação Interativa

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)  
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green)](https://spring.io/projects/spring-boot)  
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-orange)](https://www.thymeleaf.org/)  
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple)](https://getbootstrap.com/)  
[![Status](https://img.shields.io/badge/status-active-brightgreen)](#)

---

## 📖 Descrição

O **BankApp** é um sistema de gerenciamento de contas bancárias com funcionalidades para **Clientes** e *
*Administradores**.  
O sistema é seguro, responsivo e apresenta mensagens de feedback em tempo real.

---

## 🔑 Login

- **Admin:** `admin / admin123`
- **Cliente:** `cliente / cliente123`

💡 **Erro de login** é exibido diretamente na tela
---

## 🖥️ Telas Interativas

### Cliente - Operações

- Formulários para:
    - Crédito
    - Débito
    - Transferência

---

### Admin - Listar Contas

- Mostra todas as contas com saldo e proprietário.
- Botão ➕ cria uma nova conta.

---

### Admin - Criar Conta

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
    - Controllers
    - Services
    - DTO Validations
    - Login e logout
---

## ⚙️ Como Rodar

```bash
git clone https://github.com/Demilly/desafio-cast.git
cd case
./mvnw spring-boot:run
