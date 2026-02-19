CREATE TABLE IF NOT EXISTS delivery_addresses (
                                                  order_id UUID PRIMARY KEY,
                                                  coordinate_id BIGINT,
                                                  latitude DECIMAL(10,8),
                                                  longitude DECIMAL(11,8),
                                                  street_name VARCHAR(255) NOT NULL,
                                                  street_number VARCHAR(50),
                                                  neighborhood VARCHAR(100),
                                                  city VARCHAR(100) NOT NULL,
                                                  state VARCHAR(50) NOT NULL,
                                                  postal_code VARCHAR(20) NOT NULL,
                                                  country VARCHAR(2) DEFAULT 'BR',
                                                  reference TEXT,
                                                  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);