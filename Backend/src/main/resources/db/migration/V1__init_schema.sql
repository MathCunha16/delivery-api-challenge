CREATE TABLE IF NOT EXISTS stores (
                                      id UUID PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
                                      id UUID PRIMARY KEY,
                                      store_id UUID NOT NULL,
                                      total_price DECIMAL(10,2) NOT NULL CHECK(total_price >= 0),
                                      last_status VARCHAR(50) NOT NULL CHECK(last_status IN ('RECEIVED', 'CONFIRMED', 'DISPATCHED', 'DELIVERED', 'CANCELED')),
                                      customer_name VARCHAR(255) NOT NULL,
                                      customer_phone VARCHAR(50),
                                      created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                      updated_at TIMESTAMP WITH TIME ZONE,
                                      FOREIGN KEY (store_id) REFERENCES stores(id)
);

CREATE INDEX idx_orders_store ON orders (store_id);
CREATE INDEX idx_orders_created_at ON orders (created_at);

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
                                                  country CHAR(2) DEFAULT 'BR',
                                                  reference TEXT,
                                                  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_items (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           order_id UUID NOT NULL,
                                           external_code INTEGER,
                                           name VARCHAR(255) NOT NULL,
                                           quantity INTEGER NOT NULL CHECK(quantity > 0),
                                           unit_price DECIMAL(10,2) NOT NULL CHECK(unit_price >= 0),
                                           total_price DECIMAL(10,2) NOT NULL CHECK(total_price >= 0),
                                           discount DECIMAL(10,2) DEFAULT 0 CHECK(discount >= 0),
                                           observations TEXT,
                                           FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_items_order ON order_items (order_id);

CREATE TABLE IF NOT EXISTS order_item_condiments (
                                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                     order_item_id UUID NOT NULL,
                                                     name VARCHAR(255) NOT NULL,
                                                     price DECIMAL(10,2) DEFAULT 0 CHECK(price >= 0),
                                                     FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payments (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        order_id UUID NOT NULL,
                                        payment_method VARCHAR(50) NOT NULL,
                                        value DECIMAL(10,2) NOT NULL CHECK(value >= 0),
                                        is_prepaid BOOLEAN DEFAULT false,
                                        FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_payments_order ON payments (order_id);

CREATE TABLE IF NOT EXISTS order_status_history (
                                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                    order_id UUID NOT NULL,
                                                    status VARCHAR(50) NOT NULL CHECK(status IN ('RECEIVED', 'CONFIRMED', 'DISPATCHED', 'DELIVERED', 'CANCELED')),
                                                    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                                    origin VARCHAR(50),
                                                    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_history_order_date ON order_status_history (order_id, created_at);