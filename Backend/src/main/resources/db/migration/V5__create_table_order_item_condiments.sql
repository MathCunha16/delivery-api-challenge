CREATE TABLE IF NOT EXISTS order_item_condiments (
                                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                     order_item_id UUID NOT NULL,
                                                     name VARCHAR(255) NOT NULL,
                                                     price DECIMAL(10,2) DEFAULT 0 CHECK(price >= 0),
                                                     FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE
);