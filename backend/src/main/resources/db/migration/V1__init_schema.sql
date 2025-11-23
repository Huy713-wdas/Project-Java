CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  role VARCHAR(32) NOT NULL,
  name TEXT,
  email TEXT UNIQUE NOT NULL,
  password_hash TEXT,
  created_at TIMESTAMPTZ DEFAULT now(),
  enabled BOOLEAN DEFAULT TRUE,
  metadata JSONB
);

CREATE TABLE IF NOT EXISTS wallets (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  balance_cents BIGINT DEFAULT 0,
  currency VARCHAR(8) DEFAULT 'VND',
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS carbon_credits (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  serial_no TEXT UNIQUE,
  owner_id UUID REFERENCES users(id),
  quantity_tons NUMERIC(12,4) NOT NULL,
  issued_at TIMESTAMPTZ,
  status VARCHAR(32) DEFAULT 'ISSUED',
  metadata JSONB
);

CREATE TABLE IF NOT EXISTS listings (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  carbon_credit_id UUID REFERENCES carbon_credits(id),
  seller_id UUID REFERENCES users(id),
  amount_tons NUMERIC(12,4),
  price_per_ton_cents BIGINT,
  listing_type VARCHAR(16) DEFAULT 'FIXED',
  start_at TIMESTAMPTZ,
  end_at TIMESTAMPTZ,
  status VARCHAR(32) DEFAULT 'ACTIVE',
  created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS transactions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tx_type VARCHAR(32) NOT NULL,
  from_user_id UUID,
  to_user_id UUID,
  amount_cents BIGINT DEFAULT 0,
  amount_cc_tons NUMERIC(12,4),
  status VARCHAR(32) DEFAULT 'PENDING',
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now(),
  reference TEXT,
  metadata JSONB
);

CREATE TABLE IF NOT EXISTS audit_logs (
  id BIGSERIAL PRIMARY KEY,
  actor_id UUID,
  action TEXT,
  target_type VARCHAR(64),
  target_id UUID,
  remarks TEXT,
  created_at TIMESTAMPTZ DEFAULT now()
);
