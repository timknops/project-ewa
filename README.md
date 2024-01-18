# Solar Sedum

A web application built with Spring Boot and Vue.js, using MySQL as the database.

## Table of Contents

- [Solar Sedum](#solar-sedum)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
    - [Backend](#backend)
    - [Frontend](#frontend)
    - [Configuration](#configuration)
    - [Usage](#usage)
    - [Techno (M\&T)](#techno-mt)
      - [Back-end](#back-end)
      - [Front-end](#front-end)
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

   ```

2. Build and run the Spring Boot application:

   ```
   ./mvnw clean install
   ./mvnw spring-boot:run

   ```

3. The backend should now be running at http://localhost:8083.

### Frontend

1. Clone the frontend repository:

   ```
   git clone https://gitlab.fdmci.hva.nl/se-ewa/2023-2024-1/solar-4.git
   cd solar-4

   ```

2. Install dependencies and build the Vue.js application:

   ```
   npm install
   npm run build

   ```

3. Run the front-end application:
   ```
   npm run serve
   ```

### Configuration

- Backend: Check src/main/resources/ for database and other configurations.
- Frontend: src/appConfig.js to set the correct API base URL.

### Usage

- Backend: https://back-end-hfne.onrender.com
- Frontend: https://front-end-8cvv.onrender.com

### Techno (M&T)

All classes and who made what.

#### Back-end

DTO:

- ProjectResourceDTO: Tim

models:

- Project: Tim
- Resource: Tim

models/compositeKeys:

- ResourceKey: Tim

models/wrappers:

- ProjectRequestWrapper: Tim

repositories:

- ProjectRepository: Tim
- ResourceRepository: Tim

repositories/jpaRepositories:

- ResourceRepositoryJpa: Tim
- ProjectRepositoryJpa: Tim

rest:

- ProjectController: Tim

java/nl/solar/app:

- BackEndApplication: Tim, Julian, Noa

#### Front-end

front-end/src/...

components:

- ProjectsOverview: Tim
- ProjectSpecific: Tim

modal:

- ModalComponent: Julian

modal/project: Tim
modal/inventory: ...

table:

- TableButtons: Tim
- TableComponent: Tim
- TableFooter: Tim
- TableHeaderRow: Tim
- TableSortingIcons: Tim

utils:

- ErrorMessage: Tim

front-end/service:

- projectAdaptor: Tim

front-end/models:

- project: Tim

### Contacts

- Tim Knops
- Anonymized
- Nashon Woldai
- Julian Kruithof
- Wilco van de Pol
- Noa de Greef
