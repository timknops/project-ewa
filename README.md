# Solar Sedum

A web application built with Spring Boot and Vue.js, using MySQL as the database.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
    - [Backend](#backend)
    - [Frontend](#frontend)
- [Configuration](#configuration)
- [Usage](#usage)
- [Contacts](#contacts)

## Overview

Solar Sedum is a comprehensive web application developed with Spring Boot and Vue.js, integrating MySQL as the database. The application is tailored for Solar Sedum, offering functionality to monitor various aspects of their organization.

## Prerequisites

Before you begin, ensure you have the following prerequisites installed:

- [Java](https://www.java.com/en/download/)
- [Node.js](https://nodejs.org/)
- [MySQL](https://www.mysql.com/)

## Installation

### Backend

1. Clone the backend repository:

   ```
   git clone https://github.com/your-username/my-awesome-project-backend.git
   cd my-awesome-project-backend

2. Build and run the Spring Boot application:
   ```
   ./mvnw clean install
   ./mvnw spring-boot:run

3. The backend should now be running at http://localhost:8083.


### Frontend
1. Clone the frontend repository:
    ```
    git clone https://gitlab.fdmci.hva.nl/se-ewa/2023-2024-1/solar-4.git
    cd solar-4

2. Install dependencies and build the Vue.js application:
    ```
   npm install
   npm run build

3. Run the front-end application:
   ```
   npm run serve


### Configuration

- Backend: Check src/main/resources/ for database and other configurations.
- Frontend: src/appConfig.js to set the correct API base URL.


### Usage

- Backend: https://back-end-hfne.onrender.com
- Frontend: https://front-end-8cvv.onrender.com


### Contacts
- Tim Knops
- Hanan Ouardi
- Nashon Woldai
- Julian Kruithof
- Wilco van de Pol
- Noa de Greef