# DeerBank-Backend

DeerBank App provides a simplified and secure digital banking platform that allows users to manage their bank accounts online.

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
- System shall support repetitive (recurring) bill payments.

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

## 3. Artifacts (Discovery and Analysis)

This section presents the key artifacts created during the Discovery and Analysis phase, detailing the structure and behavior of the DeerBank system.

### Use Case Diagram

This diagram illustrates the main functionalities of the system and how different user types (Actors) interact with them, providing an overview of the scope.


![Use Case Diagram](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/DeerBank_UseCase.png?v=1).

### User Stories

User Stories define the functionalities from the end-user's perspective, focusing on the value each feature delivers.
![User Stories](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/DeerBank_UserStories.png)

### Class Diagram

The Class Diagram presents the static structure of the system, showing the essential classes (such as `Customer`, `Account`, `Transaction`, `Payee`) and the relationships between them.
![Class Diagram](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/DeerBank_ClassDiagram.png?v=1)

### Sequence Diagram: Transfer Between Own Accounts

This diagram details the interaction flow and the order of messages between system objects when performing an internal transfer.
![Sequence Diagram - Transfer Between Own Accounts](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/SequenceDiagram_TransferBetweenOwnAccounts.png)

### Sequence Diagram - Schedule Recurring Payment
This diagram illustrates the sequence of steps and interactions between different components of the system to schedule a recurring payment.
![Sequence Diagram - Schedule Recurring Payment](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/SequenceDiagram-ScheduleRecurringPayment.png)

### Sequence Diagram: Pay Bill

This diagram illustrates the step-by-step communication between system components during the bill payment process.
![Sequence Diagram - Pay Bill](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/SequenceDiagram-PayBill.png)

### Software Requirements Specification (SRS)

The SRS (Software Requirements Specification) document contains the formal and detailed description of all functional and non-functional requirements for the project.
[Access Full SRS Document (PDF)](https://deerbankapp-619572.s3.us-east-2.amazonaws.com/discovery/SRS.pdf)
