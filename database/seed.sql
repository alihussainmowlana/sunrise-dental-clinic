-- =====================================================================
-- Sunrise Dental Clinic Appointment System
-- Minimal Seed Data
-- Database Name: sunrise_dental_db
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. SEED DENTISTS
-- Initial practitioner staff for appointment assignment.
-- ---------------------------------------------------------------------
INSERT INTO dentists (dentist_name) VALUES 
('Dr. Perera'),
('Dr. Silva'),
('Dr. Fernando');

-- ---------------------------------------------------------------------
-- 2. SEED TREATMENTS
-- Standard dental clinic treatment catalog with realistic demo base costs in LKR.
-- ---------------------------------------------------------------------
INSERT INTO treatments (treatment_name, treatment_cost) VALUES 
('Consultation', 1500.00),
('Dental Cleaning', 5000.00),
('Tooth Filling', 6000.00),
('Tooth Extraction', 8000.00),
('Root Canal Treatment', 25000.00);

-- ---------------------------------------------------------------------
-- 3. SEED INITIAL USERS (ADMIN / RECEPTIONIST)
-- Note: In accordance with Phase 2 security guidelines, plaintext
-- passwords are NOT inserted into the database.
-- 
-- The password_hash field will be populated with secure cryptographic
-- hashes (e.g. BCrypt / Argon2 / PBKDF2) during Phase 3 (Authentication).
--
-- For initial bootstrapping, the following placeholder records can be
-- activated once the password hashing utility is implemented:
--
-- INSERT INTO users (username, password_hash, role) VALUES 
-- ('admin', '$2a$12$placeholderHashForAdminUserPhase3', 'ADMIN'),
-- ('receptionist', '$2a$12$placeholderHashForReceptionistPhase3', 'RECEPTIONIST');
-- ---------------------------------------------------------------------