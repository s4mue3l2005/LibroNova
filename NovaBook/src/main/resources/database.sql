CREATE DATABASE IF NOT EXISTS libronova;
USE libronova;

-- Create dedicated user for the application (Security Best Practice)
DROP USER IF EXISTS 'novabook_user'@'localhost';
CREATE USER 'novabook_user'@'localhost' IDENTIFIED BY 'novabook!123';
GRANT ALL PRIVILEGES ON libronova.* TO 'novabook_user'@'localhost';
FLUSH PRIVILEGES;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('ADMIN', 'ASISTENTE') NOT NULL DEFAULT 'ASISTENTE',
    status ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Partners table
CREATE TABLE IF NOT EXISTS partners (
    id INT AUTO_INCREMENT PRIMARY KEY,
    document_number VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    status ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    total_copies INT NOT NULL DEFAULT 0,
    available_copies INT NOT NULL DEFAULT 0,
    reference_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Loans table
CREATE TABLE IF NOT EXISTS loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    partner_id INT NOT NULL,
    book_id INT NOT NULL,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('ACTIVO', 'DEVUELTO', 'VENCIDO') NOT NULL DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (partner_id) REFERENCES partners(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

INSERT INTO users (username, password, email, role, status) VALUES 
('admin', 'admin123', 'admin@libronova.com', 'ADMIN', 'ACTIVO');

-- Insert sample books
INSERT INTO books (isbn, title, author, category, total_copies, available_copies, reference_price) VALUES 
('9780134685991', 'Effective Java', 'Joshua Bloch', 'Programming', 3, 3, 45.99),
('9780132350884', 'Clean Code', 'Robert C. Martin', 'Programming', 2, 2, 42.99),
('9780596009205', 'Head First Design Patterns', 'Eric Freeman', 'Programming', 4, 4, 39.99),
('9780132143011', 'Java: The Complete Reference', 'Herbert Schildt', 'Programming', 2, 2, 55.99),
('9780201633610', 'Design Patterns', 'Gang of Four', 'Programming', 1, 1, 49.99);

-- Insert sample partners
INSERT INTO partners (document_number, first_name, last_name, email, phone, address) VALUES 
('12345678', 'Juan', 'Pérez', 'juan.perez@email.com', '3001234567', 'Calle 123 #45-67'),
('87654321', 'María', 'García', 'maria.garcia@email.com', '3007654321', 'Avenida 456 #78-90'),
('11223344', 'Carlos', 'López', 'carlos.lopez@email.com', '3009876543', 'Carrera 789 #12-34');