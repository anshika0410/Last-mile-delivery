# Last-Mile Delivery Confirmation System

<br>

A robust **3-Tier Application** designed to manage secure last-mile delivery confirmations. This system ensures that packages are only marked as "Delivered" when a valid shipment ID and OTP are verified against the central database.

---

## 🏗 Architecture Overview

The project follows a strict **3-Tier Architecture**:

1.  **Tier 1: Data Access Layer (Database)**
    *   **Technology**: MySQL
    *   **Role**: Stores all shipment records, OTPs, and delivery statuses.
    *   **Key Security**: Uses Unique Shipment IDs and Status checks to prevent duplicate deliveries.

2.  **Tier 2: Business Logic Layer (Backend API)**
    *   **Technology**: Node.js & Express
    *   **Role**: Exposure of RESTful API endpoints. Handles validation, OTP verification logic, and database transactions.

3.  **Tier 3: Presentation Layer (Frontend App)**
    *   **Technology**: Native Android (Java)
    *   **Role**: User-friendly mobile interface for Delivery Agents to input data and receive real-time feedback.

---

## � Project Structure

```bash
last-mile-delivery/
├── android-app/          # Native Android Studio Project
│   ├── app/              # Main App Module
│   ├── build.gradle      # Project-level Build Config
│   └── settings.gradle   # Project Settings
│
├── backend-service/      # Node.js REST API
│   ├── server.js         # Main Application Entry Point
│   ├── db.js             # MySQL Connection Pool
│   └── package.json      # Dependencies
│
├── database-scripts/     # SQL Scripts
│   └── init.sql          # Database Scheme & Seed Data
│
└── README.md             # Project Documentation
```

---

## ⚙️ Prerequisites

Before you begin, ensure you have the following installed:
*   [Node.js](https://nodejs.org/) (v14 or higher)
*   [MySQL Server](https://dev.mysql.com/downloads/mysql/) (v8.0 recommended)
*   [Android Studio](https://developer.android.com/studio) (latest version)
*   **Git** (for version control)

---

## 🚀 Step-by-Step Setup Guide

### 1. Database Setup
1.  Launch your MySQL client (MySQL Workbench, TablePlus, or CLI).
2.  Open the script: `database-scripts/init.sql`.
3.  **Execute** the script to create the database and table.
4.  It initializes the table `shipments` with:
    *   `id`: Primary Key
    *   `shipment_id`: Unique Identifier (e.g., SHP-1001)
    *   `otp_code`: 6-digit Secret Code
    *   `status`: 'Pending', 'In-Transit', or 'Delivered'

### 2. Backend API Setup
1.  Open a terminal in the `backend-service` folder.
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  **Configuration**: Open `.env` (or create if missing) and set your DB credentials:
    ```properties
    PORT=3000
    DB_HOST=127.0.0.1
    DB_USER=root
    DB_PASSWORD=your_mysql_password
    DB_NAME=delivery_db
    ```
4.  Start the server:
    ```bash
    npm start
    ```
    ✅ *Success Message: "Server running on port 3000"*

### 3. Android App Setup
1.  Open **Android Studio**.
2.  Select **Open** and choose the `android-app` directory.
3.  Wait for Gradle Sync to finish (this may take a few minutes).
4.  Select an Emulator (e.g., Pixel API 34) and click **Run (Green Play Button)**.

---

## 📡 API Reference

#### Base URL: `http://localhost:3000`

### 📦 Confirm Delivery
Verifies the shipment details and updates the status to 'Delivered'.
*   **Method**: `POST`
*   **Endpoint**: `/api/confirm-delivery`
*   **Request Body**:
    ```json
    {
      "shipmentId": "SHP-1001",
      "otp": "123456",
      "agentName": "Agent Name"
    }
    ```
*   **Success Response (200)**: `Delivery Confirmed`
*   **Error Responses**:
    *   `400`: Already Delivered
    *   `401`: Invalid OTP (Verification Failed)
    *   `404`: Shipment Not Found

### 🔄 Resend OTP (Bonus Feature)
Simulates a request to resend the OTP to the customer.
*   **Method**: `POST`
*   **Endpoint**: `/api/request-otp`
*   **Request Body**:
    ```json
    { "shipmentId": "SHP-1001" }
    ```
*   **Response**: `OTP Sent to Customer`
*   **Simulated Action**: The backend server log will display: `[OTP REQUEST] OTP for Shipment SHP-1001 is: 123456`

---

## 📸 Proof of Functionality

### 1. Delivery Confirmed (Success)
*The app turns GREEN and shows a success message when OTP is correct.*
<!-- Paste Screenshot Below -->
<br><br><br><br><br>
<img width="281" height="608" alt="Screenshot 2025-12-27 215004" src="https://github.com/user-attachments/assets/2a25cfab-40b2-46ec-8569-8029b382a779" />
<br><br><br><br><br>

### 2. Resend OTP Logic
*The app requests OTP, and the Backend Console logs the "SMS".*
<!-- Paste Screenshot Below -->
<br><br><br><br><br>
<img width="282" height="590" alt="Screenshot 2025-12-27 214911" src="https://github.com/user-attachments/assets/067f3c87-05d7-4b37-bcf0-2724ad6b5f18" />
<img width="832" height="420" alt="Screenshot 2025-12-27 214932" src="https://github.com/user-attachments/assets/913a737d-8801-4333-b7fa-b8e21ccef09e" />

<br><br><br><br><br>

### 3. Error Handling (Duplicate Delivery)
*The app shows RED if the package was already marked 'Delivered'.*
<!-- Paste Screenshot Below -->
<br><br><br><br><br>
<img width="279" height="615" alt="Screenshot 2025-12-27 215019" src="https://github.com/user-attachments/assets/ff101a65-7eb0-411f-9505-59f4ad60b4fc" />
<br><br><br><br><br>

### 4. Error Handling (Invalid Shipment)
*The app detects non-existent IDs.*
<!-- Paste Screenshot Below -->
<br><br><br><br><br>
<img width="280" height="588" alt="Screenshot 2025-12-27 215133" src="https://github.com/user-attachments/assets/952a0ce0-1f3d-4019-a172-cbe302277169" />
<br><br><br><br><br>

### 5. Error Handling (Invalid OTP)
*The app detects non-existent OTP.*
<!-- Paste Screenshot Below -->
<br><br><br><br><br>
![WhatsApp Image 2025-12-28 at 12 02 28 AM](https://github.com/user-attachments/assets/bdbb434e-6763-4e24-a413-859a05640870)
<br><br><br><br><br>

---

## 🔧 Troubleshooting

| Issue | Solution |
| :--- | :--- |
| **ECONNREFUSED** (Backend) | 1. Ensure MySQL server is running.<br>2. Try changing `DB_HOST` to `127.0.0.1` in `.env`. |
| **Loopback Error** (Android) | Ensure `MainActivity.java` uses `10.0.2.2` (Emulator localhost) instead of `localhost` or `127.0.0.1`. |
| **Resource Not Found** (Android) | Clean Project -> Rebuild Project in Android Studio. |
| **Port 3000 Busy** | Kill the node process or change `PORT` in `.env`. |

---
