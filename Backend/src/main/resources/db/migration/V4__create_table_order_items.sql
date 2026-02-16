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