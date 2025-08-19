# 💰 Banking & Contracts Bot (Java)

A private **Telegram banking bot** for managing a central vault (USD + Gold), user balances, and contract-style invoices.

---

## ✨ Features
- 🔑 Password-protected access
- 👤 User Profiles (Gold, Real USD, Expected USD)
- 💵 Withdraw funds from central vault
- 🧾 Invoices (contracts with expected vs. received USD + gold value)
- 🛠 Admin Panel (vault control, user control, invoice oversight)

---

## 🚀 Tech Stack
- Java 17+
- [TelegramBots API](https://github.com/rubenlagus/TelegramBots)
- SQLite database (lightweight & simple)

---

## 📂 Database
- **vault**: central reserves
- **users**: gold, real usd, expected usd
- **invoices**: contracts with expected vs. received values
- **config**: bot settings

---

## 🔧 Commands

### User
- `/start` – authenticate
- `/profile` – view balances
- `/withdraw <amount> <currency>`
- `/invoice create <name> <gold> <expected_usd> <received_usd>`
- `/invoice list`

### Admin
- `/admin vault`
- `/admin setvault <usd> <gold>`
- `/admin setbalance <user_id> <gold> <real_usd> <expected_usd>`
- `/admin listusers`
- `/admin invoices <user_id>`

---

## 🛡 License
MIT
