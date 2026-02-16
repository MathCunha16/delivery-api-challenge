CREATE TABLE IF NOT EXISTS payments (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        order_id UUID NOT NULL,
                                        payment_method VARCHAR(50) NOT NULL,
                                        value DECIMAL(10,2) NOT NULL CHECK(value >= 0),
                                        is_prepaid BOOLEAN DEFAULT false,
                                        FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_payments_order ON payments (order_id);