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