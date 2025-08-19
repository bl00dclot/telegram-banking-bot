CREATE TABLE IF NOT EXISTS vault (
    id INTEGER,
    usd DOUBLE,
    gold DOUBLE
);
CREATE TABLE IF NOT EXISTS config (
    id INTEGER,
    auth_password VARCHAR,
    admin_id BIGINT
);
CREATE TABLE IF NOT EXISTS users (
    id INTEGER,
    telegram_id BIGINT,
    username VARCHAR,
    gold_balance DOUBLE,
    real_usd_balance DOUBLE,
    expected_usd_total DOUBLE,
    authorized BOOLEAN
);
CREATE TABLE IF NOT EXISTS invoices (
    id INTEGER,
    user_id INTEGER,
    contract_name VARCHAR,
    gold DOUBLE,
    expected_usd DOUBLE,
    received_usd DOUBLE,
    timestamp TIMESTAMP
);