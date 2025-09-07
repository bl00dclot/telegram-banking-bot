CREATE TABLE IF NOT EXISTS vault_state (
    price_per_g INTEGER,
    gold_g BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    version BIGINT DEFAULT 1
);
CREATE TABLE IF NOT EXISTS vault_history (
    id BIGINT,
    action VARCHAR,
    change_gold_g BIGINT,
    prev_gold_g BIGINT,
    new_gold_g BIGINT,
    prev_price_per_g BIGINT,
    new_price_per_g BIGINT,
    prev_value_usd BIGINT, --prev_price_per_g * prev_gold_g
    new_value_usd BIGINT, --new_gold_g * new_price_per_g
    actor_telegram_id BIGINT,
    reason VARCHAR,
    external_id VARCHAR,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS config (
    id INTEGER,
    auth_password VARCHAR,
    registration_open BOOLEAN,
    admin_id BIGINT
);
CREATE TABLE IF NOT EXISTS admins (
    telegram_id BIGINT PRIMARY KEY,
    label VARCHAR,                       -- optional human label
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT                    -- who added this admin (nullable)
);
CREATE TABLE IF NOT EXISTS users (
    id INTEGER,
    telegram_id BIGINT,
    username VARCHAR,
    gold_balance INTEGER,
    real_usd_balance INTEGER,
    gold_price_usd INT,
    expected_usd_total INTEGER,
    authorized BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS invoices (
    id INTEGER,
    user_id INTEGER,
    contract_name VARCHAR,
    gold INTEGER,
    expected_usd INTEGER,
    received_usd INTEGER,
    timestamp TIMESTAMP
);