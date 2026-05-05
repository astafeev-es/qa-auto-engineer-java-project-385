### Hexlet tests and linter status:
[![Actions Status](https://github.com/astafeev-es/qa-auto-engineer-java-project-385/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/astafeev-es/qa-auto-engineer-java-project-385/actions)
[![Build Status](https://github.com/astafeev-es/qa-auto-engineer-java-project-385/actions/workflows/build.yml/badge.svg)](https://github.com/astafeev-es/qa-auto-engineer-java-project-385/actions)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=astafeev-es_qa-auto-engineer-java-project-385&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=astafeev-es_qa-auto-engineer-java-project-385)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=astafeev-es_qa-auto-engineer-java-project-385&metric=coverage)](https://sonarcloud.io/summary/new_code?id=astafeev-es_qa-auto-engineer-java-project-385)

---

# Kanban Board Automation Project

This project contains automated end-to-end tests for a Kanban Board application. It uses a modern tech stack to ensure the reliability and stability of task management features.

## Description
The goal of this project is to implement robust UI automation for a task manager that includes features like:
- **Authentication**: Secure login and session management.
- **Task Management**: Creating, editing, moving, and deleting tasks within a Kanban interface.
- **User & Label Management**: Managing participants and categorization tags.
- **Status Tracking**: Organizing tasks through customizable statuses (Draft, To Review, Published, etc.).

## Tech Stack
- **Language**: Java 21
- **Testing Framework**: JUnit 5
- **Logging**: Slf4j
- **UI Automation**: Selenium WebDriver (with Headless Chrome)
- **Design Pattern**: Page Object Model (POM)
- **Build System**: Gradle
- **Static Analysis**: Checkstyle & SonarCloud
- **CI/CD**: GitHub Actions
- **Containerization**: Docker (for running the application environment)
