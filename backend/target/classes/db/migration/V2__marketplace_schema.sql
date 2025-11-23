-- V2: full marketplace core tables (additions to V1)
CREATE TABLE IF NOT EXISTS ev_profiles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  vehicle_id TEXT,
  battery_kwh NUMERIC,
  emission_factor_kg_per_kwh NUMERIC,
  region TEXT,
  created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS journeys (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  ev_profile_id UUID REFERENCES ev_profiles(id) ON DELETE CASCADE,
  raw_file_path TEXT,
  start_time TIMESTAMPTZ,
  end_time TIMESTAMPTZ,
  km NUMERIC,
  energy_kwh NUMERIC,
  calculated_co2_kg NUMERIC,
  created_at TIMESTAMPTZ DEFAULT now(),
  status VARCHAR(32) DEFAULT 'PENDING_REVIEW'
);

CREATE TABLE IF NOT EXISTS carbon_credits (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  serial_no TEXT UNIQUE,
  owner_id UUID REFERENCES users(id),
  quantity_tons NUMERIC(12,4) NOT NULL,
  issued_at TIMESTAMPTZ,
  issued_by_cva_id UUID REFERENCES users(id),
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

CREATE TABLE IF NOT EXISTS bids (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  listing_id UUID REFERENCES listings(id) ON DELETE CASCADE,
  bidder_id UUID REFERENCES users(id),
  bid_amount_cents BIGINT,
  bid_time TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS certificates (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  carbon_credit_id UUID REFERENCES carbon_credits(id),
  tx_id UUID REFERENCES transactions(id),
  certificate_file_path TEXT,
  issued_to_user_id UUID REFERENCES users(id),
  issued_at TIMESTAMPTZ DEFAULT now(),
  content_hash TEXT
);

ALTER TABLE wallets ADD COLUMN IF NOT EXISTS locked_cents BIGINT DEFAULT 0;
