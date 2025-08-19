CREATE TABLE IF NOT EXISTS vault (
    id INTEGER PRIMARY KEY,
    usd DOUBLE,
    gold DOUBLE
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    telegram_id BIGINT UNIQUE,
    username TEXT,
    gold_balance DOUBLE DEFAULT 0,
    real_usd_balance DOUBLE DEFAULT 0,
    expected_usd_total DOUBLE DEFAULT 0,
    authorized BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS invoices (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    contract_name TEXT,
    gold DOUBLE,
    expected_usd DOUBLE,
    received_usd DOUBLE,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS config (
    id INTEGER PRIMARY KEY,
    auth_password TEXT,
    admin_id BIGINT
);
