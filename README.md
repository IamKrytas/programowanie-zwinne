# 🗂️ Agile Project Manager

> Web application for organizing projects and tasks, built using React & Spring Boot.


![CI/CD Status](https://github.com/IamKrytas/programowanie-zwinne/actions/workflows/build.yml/badge.svg)

---

## 📝 Description

Agile Project Manager is a self-hosted project management application created as part of an Agile Programming course. It allows users to create, edit, and delete projects, assign tasks, and manage team members. Each user can authenticate, join or manage projects, and collaborate on tasks with other users.  

The system is composed of:
- a **React frontend** for the UI
- a **Spring Boot backend** for business logic and persistence

---


## 💠 Technologies

| Frontend          | Backend        |
|-------------------|----------------|
| React             | Spring Boot    |
| TypeScript        | Java           |
| Vite              | Gradle         |
|                   | MongoDB        |

---
## ⚙️ Requirements

| Technology         | Version             |
|--------------------|---------------------|
| Java               | 21+                 |
| Node.js            | Latest LTS          |

---

## 🚀 Installation and Running


### 1️⃣ Clone the repository

```bash
git clone https://github.com/IamKrytas/programowanie-zwinne.git
cd programowanie-zwinne
```

---

### 2️⃣ Running the frontend

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Run the development server:

```bash
npm run dev
```

➡️ The frontend will be available at:  
`http://localhost:5173/`

---

### 3️⃣ Running the backend

Navigate to the backend directory:

```bash
cd backend
```

Build the project and start the Spring Boot server:

```bash
./gradlew build
./gradlew bootRun
```

➡️ The backend will be running at: `http://localhost:8080/`

---

## 📂 Project Structure

```
.
├── .github
│   └── workflows
├── BACKEND
│   ├── .gradle
│   ├── bin
│   ├── build
│   ├── build.gradle
│   ├── gradle
│   ├── gradlew
│   ├── gradlew.bat
│   ├── settings.gradle
│   └── src
├── CONTRIB
├── FRONTEND
│   ├── index.html
│   ├── package-lock.json
│   ├── package.json
│   ├── projekt
│   ├── public
│   ├── README.md
│   ├── src
│   ├── tsconfig.app.json
│   ├── tsconfig.json
│   ├── tsconfig.node.json
│   └── vite.config.ts
├── README.md
├── update_languages.py
└── update_structure.py
```
## 🎬 Demo

![Agile Project Manager Screenshot](https://raw.githubusercontent.com/IamKrytas/programowanie-zwinne/main/demo/screenshot.png)

---

## 🧪 Testing

- Frontend: Add component and integration tests using tools like Jest or Vitest.
- Backend: Add unit and integration tests using JUnit and MockK.
- To run backend tests:

```bash
cd backend
./gradlew test
```

---

## 📊 Languages Used

```
Java        66.3%
TypeScript  25.5%
CSS          2.6%
JavaScript   2.2%
Dockerfile   2.0%
HTML         1.4%
```

---

## 🎓 Academic Info

This project was created as part of an assignment for the **Agile Programming** course at the **Bydgoszcz University of Science and Technology**.





---


## ✍️ Contributors

| <img src="https://avatars.githubusercontent.com/u/32397526?v=4&s=100" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/92470000?v=4&s=100" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/96568740?v=4&s=100" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/98387159?v=4&s=100" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/202075381?v=4&s=100" width="100" height="100"> |
|:--:|:--:|:--:|:--:|:--:|
| [@danrog303](https://github.com/danrog303) | [@Kyandi0](https://github.com/Kyandi0) | [@IamKrytas](https://github.com/IamKrytas) | [@LikeCiastka](https://github.com/LikeCiastka) | [@Pawel-234](https://github.com/Pawel-234) |


---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---
