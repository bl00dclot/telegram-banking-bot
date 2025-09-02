CREATE TABLE IF NOT EXISTS vault (
    id INTEGER,
    usd DOUBLE,
    gold INTEGER
);
CREATE TABLE IF NOT EXISTS config (
    id INTEGER,
    auth_password VARCHAR,
    registration_open BOOLEAN,
    admin_id BIGINT
);
CREATE TABLE IF NOT EXISTS users (
    id INTEGER,
    telegram_id BIGINT,
    username VARCHAR,
    gold_balance INTEGER,
    real_usd_balance DOUBLE,
    gold_price_usd INT,
    expected_usd_total DOUBLE,
    authorized BOOLEAN,
    date_by TIMESTAMP_S
);
CREATE TABLE IF NOT EXISTS invoices (
    id INTEGER,
    user_id INTEGER,
    contract_name VARCHAR,
    gold INTEGER,
    expected_usd DOUBLE,
    received_usd DOUBLE,
    timestamp TIMESTAMP
);