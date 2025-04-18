CREATE DATABASE payroll_db;

USE payroll_db;

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    designation VARCHAR(100),
    basic_salary DOUBLE,
    overtime_hours INT,
    tax_percentage DOUBLE
);
