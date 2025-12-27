CREATE DATABASE IF NOT EXISTS delivery_db;
USE delivery_db;

CREATE TABLE IF NOT EXISTS shipments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    shipment_id VARCHAR(50) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    status ENUM('Pending', 'In-Transit', 'Delivered') DEFAULT 'Pending',
    delivered_at TIMESTAMP NULL,
    delivered_by VARCHAR(100) NULL
);

-- Sample Data
INSERT INTO shipments (shipment_id, customer_name, otp_code, status) VALUES 
('SHP-1001', 'Alice Johnson', '123456', 'Pending'),
('SHP-1002', 'Bob Smith', '654321', 'In-Transit'),
('SHP-1003', 'Charlie Brown', '112233', 'Pending');
