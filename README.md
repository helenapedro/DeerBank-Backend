# DeerBank Backend

A simplified digital banking system built with Spring Boot.

The DeerBank App provides a secure and user-friendly digital banking platform that allows users to manage accounts, transfer funds, schedule recurring payments, and pay bills online.
This repository contains the backend implementation and SDLC artifacts.

## 1. Functional Requirements

- The System shall allow users to register an account.
- The System shall allow users to securely log in and log out.
- The System shall allow users to link bank accounts.
- The System shall allow users to view the list of accounts and balances.
- The System shall allow users to view detailed transaction history.
- The System shall allow users to filter the transaction history.
- The System shall allow users to transfer between their own accounts.
- The System shall allow users to transfer to other customers.
- The System shall validate the balance before the transfer.
- The System shall allow users to register and manage payees (beneficiaries).
- The System shall allow users to pay bills.
- The System shall register all operations as transactions.
- The System shall store customer profile data.
- The System shall log important events.
- System shall support one-time bill payments.
- System shall support recurring (repetitive) bill payments.

## 2. Non-Functional Requirements

- The UI (User Interface) must be user-friendly.
- The System must be Secure.
- The System must ensure consistency and atomic operations.
- The System must provide acceptable performance.
- The System must be available during normal banking hours with minimal downtime.
- The System must be scalable.
- The System must be maintainable.
- The System must be cost-efficient.
- The System must perform well.


### Software Requirements Specification (SRS)

[Access Full SRS (PDF)](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/SRS.pdf)

## 3. Artifacts — Discovery & Analysis Phase

### Use Case Diagram

Shows the major user interactions with the system.

![Use Case Diagram](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/DeerBank_UseCase.png?v=1).

### User Stories

User-centered descriptions of core functionalities.

![User Stories](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/DeerBank_UserStories.png)

### Class Diagram
Domain classes and their relationships.

![Class Diagram](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/DeerBank_ClassDiagram.png?v=5)

### Sequence Diagram — Transfer Between Own Accounts

![Sequence Diagram - Transfer Between Own Accounts](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/SequenceDiagram_TransferBetweenOwnAccounts.png)

### Sequence Diagram — Schedule Recurring Payment

![Sequence Diagram - Schedule Recurring Payment](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/SequenceDiagram-ScheduleRecurringPayment.png)

### Sequence Diagram — Pay Bill

![Sequence Diagram - Pay Bill](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/SequenceDiagram-PayBill.png)

## 4. Artifacts — Architecture Phase
The DeerBank backend follows a layered architecture:

* **Client Layer** — React web application communicating over HTTPS REST.
* **API Layer** — REST controllers for authentication, accounts, transfers, and payments.
* **Service Layer** — Business logic (AuthService, AccountService, TransferService, BillPaymentService).
* **Domain Layer** — Core business entities and aggregates.
* **Persistence Layer** — JPA repositories for database access.
* **Background Jobs** — Scheduler for recurring bill payments.
* **Infrastructure** — MySQL database (Hostinger) and Email/Notification Service.

### Container Diagram

![DeerBank Container Diagram](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/archtecture/DeerBank-ContainerDiagram.png)

### Component Diagram

![DeerBank Component Diagram](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/archtecture/DeerBank-ComponentDiagram.png)

### High-Level Sequence Diagram (Architecture View)

![DeerBank Sequence Diagram](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/archtecture/SD-PayBill_ArchitectureView.png)

### Deployment Diagram

![DeerBank Deployment Diagram](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/archtecture/DeerBank-DeploymentDiagram.png)


## 5. Artifacts — Design Phase

### Design-level Class Diagram
(Services + DAOs + Domain + Utility classes)
![Class Diagram(Design)](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/design/DeerBank-ClassDiagram-Design.png)

### Design-Level Sequence Diagram — Transfer Between Own Accounts

![Sequence Diagram(Design) - Transfer Between Own Accounts](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/design/SD_TransferBetweenOwnAccounts_Design.png)

### Design-Level Sequence Diagram — Schedule Recurring Payment

![Sequence Diagram - Schedule Recurring Payment](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/design/SD-ScheduleRecurringPayment-Design.png)

### Design-Level Sequence Diagram — Pay Bill

![Sequence Diagram - Pay Bill](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/design/SD-PayBill-Design.png)

---

## Author

* [Helena Pedro](https://www.linkedin.com/in/helena-software-engineer/)

