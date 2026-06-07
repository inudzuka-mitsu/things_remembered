# Playwright Java Automation Framework

This project is a UI Test Automation framework built using **Playwright**, **Java 17**, **JUnit 5**, and **Maven**. It supports parallel execution, multi-environment configuration, and Allure reporting.

## 📋 Prerequisites

Before running the tests, ensure you have the following installed:

* **Java JDK 17+**: [Download Here](https://adoptium.net/)
* **Maven**: [Download Here](https://maven.apache.org/download.cgi) (or install via Homebrew: `brew install maven`)

## 🚀 Quick Setup

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url>
    cd <project-folder>
    ```

2.  **Install dependencies:**
    ```bash
    mvn clean install -DskipTests
    ```

## 🏃 Running Tests!@34

### 1. Default Run (Dev Environment)
Runs all tests using the default configuration (`src/test/resources/config-stg.properties`).
```bash
mvn clean test
```

### 2. Run on Specific Environment
To switch environments (e.g., Prod, Staging), pass the env property. This loads the corresponding config file (e.g., config-stg.properties).

```bash
mvn clean test -Denv=prod
mvn clean test -Denv=stg
```

### 3. Run in Headless Mode

```bash
mvn clean test -Dheadless=true
```

### 4. Run smoke tests only

```bash
mvn clean test -Denv=stg -Dgroups="smoke"
```

### 5. Generate reports

```bash
allure generate target/allure-results --clean --single-file
```

Open in a browser

```bash
open allure-report/index.html
```
### 6. Run mobile tests

```bash
mvn clean test -Denv=stg -Ddevice.name="iPhone 13 Pro Max"
```