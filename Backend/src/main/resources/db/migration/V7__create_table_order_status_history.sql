CREATE TABLE IF NOT EXISTS order_status_history (
                                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                    order_id UUID NOT NULL,
                                                    status VARCHAR(50) NOT NULL CHECK(status IN ('RECEIVED', 'CONFIRMED', 'DISPATCHED', 'DELIVERED', 'CANCELED')),
                                                    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                                    origin VARCHAR(50),
                                                    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_history_order_date ON order_status_history (order_id, created_at);