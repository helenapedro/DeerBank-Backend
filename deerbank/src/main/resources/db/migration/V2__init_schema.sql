SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ===========================
-- USERS
-- ===========================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,  -- 'CUSTOMER', 'ADMIN'
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ===========================
-- ACCOUNTS
-- ===========================
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_number VARCHAR(30) NOT NULL,
    account_type   VARCHAR(50) NOT NULL,  -- 'CHECKING', 'SAVINGS'
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_accounts_number UNIQUE (account_number),
    CONSTRAINT fk_accounts_user
    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_accounts_user_id ON accounts(user_id);

-- ===========================
-- PAYEES
-- ===========================
CREATE TABLE IF NOT EXISTS payees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, -- owner customer
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NULL, -- e.g. 'ELECTRICITY', 'PHONE'
    external_account_ref VARCHAR(100) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_payees_user
    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_payees_user_id ON payees(user_id);

-- ===========================
-- RECURRING PAYMENTS
-- ===========================
CREATE TABLE IF NOT EXISTS recurring_payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    payee_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    frequency VARCHAR(50) NOT NULL, -- 'WEEKLY', 'MONTHLY', etc.
    start_date DATE NOT NULL,
    end_date DATE NULL,
    next_execution_date DATETIME NULL,
    status VARCHAR(50) NOT NULL, -- 'ACTIVE', 'PAUSED', 'CANCELLED'
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_recurring_user
    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_recurring_account
    FOREIGN KEY (account_id) REFERENCES accounts(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_recurring_payee
    FOREIGN KEY (payee_id) REFERENCES payees(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_recurring_user_id ON recurring_payments(user_id);
CREATE INDEX idx_recurring_account_id ON recurring_payments(account_id);
CREATE INDEX idx_recurring_payee_id ON recurring_payments(payee_id);

-- ===========================
-- BILL PAYMENTS
-- ===========================
CREATE TABLE IF NOT EXISTS bill_payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    payee_id BIGINT NOT NULL,
    recurring_payment_id BIGINT NULL,  -- nullable for one-time payments
    payment_type VARCHAR(50) NOT NULL, -- 'ONE_TIME', 'RECURRING'
    amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- 'SCHEDULED', 'EXECUTED', 'CANCELLED'
    scheduled_date DATETIME NULL,
    executed_date DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bill_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
    CONSTRAINT fk_bill_account
    FOREIGN KEY (account_id) REFERENCES accounts(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
    CONSTRAINT fk_bill_payee
    FOREIGN KEY (payee_id) REFERENCES payees(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
    CONSTRAINT fk_bill_recurring
    FOREIGN KEY (recurring_payment_id) REFERENCES recurring_payments(id)
    ON DELETE SET NULL
    ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_bill_user_id ON bill_payments(user_id);
CREATE INDEX idx_bill_account_id ON bill_payments(account_id);
CREATE INDEX idx_bill_payee_id ON bill_payments(payee_id);
CREATE INDEX idx_bill_recurring_id ON bill_payments(recurring_payment_id);

-- ===========================
-- TRANSACTIONS
-- ===========================
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL, -- 'DEBIT', 'CREDIT', 'TRANSFER', 'BILL_PAYMENT'
    amount DECIMAL(15,2) NOT NULL,
    description VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tx_account
    FOREIGN KEY (account_id) REFERENCES accounts(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_tx_account_id ON transactions(account_id);

SET FOREIGN_KEY_CHECKS = 1;
