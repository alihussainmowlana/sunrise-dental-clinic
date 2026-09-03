-- =====================================================================
-- Sunrise Dental Clinic Appointment System
-- Database Schema for PostgreSQL
-- Database Name: sunrise_dental_db
-- =====================================================================

-- Drop existing tables in reverse dependency order for clean recreation
DROP TABLE IF EXISTS bills CASCADE;
DROP TABLE IF EXISTS appointments CASCADE;
DROP TABLE IF EXISTS treatments CASCADE;
DROP TABLE IF EXISTS dentists CASCADE;
DROP TABLE IF EXISTS patients CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ---------------------------------------------------------------------
-- 1. USERS TABLE
-- Stores authorized system operators for authentication.
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'RECEPTIONIST')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- 2. PATIENTS TABLE
-- Stores patient contact and personal details for appointment bookings.
-- ---------------------------------------------------------------------
CREATE TABLE patients (
    patient_id SERIAL PRIMARY KEY,
    patient_name VARCHAR(150) NOT NULL,
    address TEXT NOT NULL,
    contact_number VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- 3. DENTISTS TABLE
-- Stores dental practitioners available for appointment assignment.
-- ---------------------------------------------------------------------
CREATE TABLE dentists (
    dentist_id SERIAL PRIMARY KEY,
    dentist_name VARCHAR(150) UNIQUE NOT NULL
);

-- ---------------------------------------------------------------------
-- 4. TREATMENTS TABLE
-- Stores catalog of dental treatments and standardized pricing.
-- ---------------------------------------------------------------------
CREATE TABLE treatments (
    treatment_id SERIAL PRIMARY KEY,
    treatment_name VARCHAR(150) UNIQUE NOT NULL,
    treatment_cost NUMERIC(10, 2) NOT NULL CHECK (treatment_cost >= 0)
);

-- ---------------------------------------------------------------------
-- 5. APPOINTMENTS TABLE
-- Main transactional table linking patients, dentists, and treatments.
-- ---------------------------------------------------------------------
CREATE TABLE appointments (
    appointment_id SERIAL PRIMARY KEY,
    appointment_number VARCHAR(50) UNIQUE NOT NULL,
    patient_id INT NOT NULL REFERENCES patients(patient_id) ON DELETE CASCADE,
    dentist_id INT NOT NULL REFERENCES dentists(dentist_id) ON DELETE RESTRICT,
    treatment_id INT NOT NULL REFERENCES treatments(treatment_id) ON DELETE RESTRICT,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Enforce single appointment per dentist at a specific date and time slot
    CONSTRAINT uq_dentist_appointment_slot UNIQUE (dentist_id, appointment_date, appointment_time)
);

-- Indexes on appointments for query performance on search and lookups
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_dentist ON appointments(dentist_id);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);

-- ---------------------------------------------------------------------
-- 6. BILLS TABLE
-- Stores invoice records generated for completed/finalized appointments.
-- ---------------------------------------------------------------------
CREATE TABLE bills (
    bill_id SERIAL PRIMARY KEY,
    bill_number VARCHAR(50) UNIQUE NOT NULL,
    appointment_id INT UNIQUE NOT NULL REFERENCES appointments(appointment_id) ON DELETE CASCADE,
    consultation_fee NUMERIC(10, 2) NOT NULL DEFAULT 0.00 CHECK (consultation_fee >= 0),
    treatment_cost NUMERIC(10, 2) NOT NULL CHECK (treatment_cost >= 0),
    total_amount NUMERIC(10, 2) NOT NULL CHECK (total_amount >= 0),
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index on bills for appointment invoice lookup
CREATE INDEX idx_bills_appointment ON bills(appointment_id);
