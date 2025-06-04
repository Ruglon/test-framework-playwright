# Playwright TestNG Automation Framework

A robust test automation framework for **UI and API testing** using **Playwright**, **TestNG**, and **Allure** reporting. Built with Java 17, it supports cross-browser testing (Chromium, Firefox, Safari/WebKit) with parallel execution and group-based test filtering (e.g., `ui`, `api`, `regression`). The framework tests web applications, such as the Elements page for form submission, and APIs, such as `https://reqres.in/api/users`, generating detailed Allure reports viewable via GitHub Actions.

## Features
- **Cross-Browser Testing**: Tests run on Chromium, Firefox, and Safari (mapped to WebKit).
- **API Testing**: Supports API testing with Playwright’s APIRequestContext.
- **Parallel Execution**: Configured with `parallel="methods"` and `thread-count="3"`.
- **Test Groups**: Filter tests by groups (`ui`, `api`, `navigation`, `form`, `regression`).
- **Allure Reports**: Interactive reports with test steps and metadata, hosted temporarily via Surge.sh and intended for persistent hosting on GitHub Pages.
- **Cross-Platform**: Setup scripts for Windows, macOS, and Linux.
- **Modular Design**: Page Object Model with `BaseTest`, `ElementsTest`, `ElementsSteps`, `BasePage`, and API tests like `MinimalPlaywrightApiTest`.

## Project Structure
```
test-framework-playwright/
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   ├── api/
│   │   │   │   └── tests/
│   │   │   │       └── MinimalPlaywrightApiTest.java
│   │   │   ├── config/
│   │   │   │   └── ConfigManager.java
│   │   │   ├── factory/
│   │   │   │   ├── PlaywrightFactory.java
│   │   │   │   └── TestContext.java
│   │   │   ├── ui/
│   │   │   │   ├── helpers/
│   │   │   │   │   └── WaitHelper.java
│   │   │   │   ├── pages/
│   │   │   │   │   ├── BasePage.java
│   │   │   │   │   ├── HomePage.java
│   │   │   │   │   └── ElementsPage.java
│   │   │   │   ├── steps/
│   │   │   │   │   └── ElementsSteps.java
│   │   │   │   └── tests/
│   │   │   │       ├── BaseTest.java
│   │   │   │       └── ElementsTest.java
│   │   └── resources/
│   │       ├── schemas/
│   │       │   └── get/
│   │       │       └── requests/
│   │       │           └── user-list-schema.json
│   │       ├── config.properties
│   │       └── testng.xml
├── .github/
│   └── workflows/
│       └── playwright-tests.yml
├── pom.xml
├── setup.sh
├── setup.ps1
├── commands.txt
└── README.md
```

## Prerequisites
- **Java 17**: Install from [Adoptium](https://adoptium.net/) (version 17 recommended).
- **Node.js 18+**: Required for Surge.sh hosting in GitHub Actions (install from [nodejs.org](https://nodejs.org/)).
- **Git**: To clone the repository.
- **Internet**: For downloading Maven, Allure, and Surge.sh dependencies.
- **Surge.sh Account**: Needed for temporary Allure report hosting in GitHub Actions (sign up at [surge.sh](https://surge.sh/)).

## Setup Instructions

### 1. Clone the Repository
Clone the project and navigate to the root directory:

```bash
git clone https://github.com/Ruglon/test-framework-playwright.git
cd test-framework-playwright
```

### 2. Install Dependencies
Run the setup script for your operating system to install **Maven 3.9.6**, **Allure 2.28.0**, and configure `JAVA_HOME` and `M2_HOME`:

- **Windows**:
  ```powershell
  # Allow script execution
  Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
  .\setup.ps1
  # Open a new PowerShell session to refresh PATH for 'mvn' and 'allure'
  ```

- **macOS/Linux**:
  ```bash
  chmod +x setup.sh
  ./setup.sh
  # Open a new terminal to refresh PATH for 'mvn' and 'allure'
  ```

**Note**: If `JAVA_HOME` cannot be set automatically, the script will provide instructions to set it manually (e.g., `C:\Program Files\Java\jdk-17` on Windows, `/usr/lib/jvm/java-17-openjdk` on Linux).

### 3. Configure Application URL
Edit `src/test/resources/config.properties` to set the target application URL:

```properties
ui.url=https://demoqa.com
timeout.element=10000
headless=true
viewport.width=1920
viewport.height=1080
```

- Update `ui.url` to your application’s URL if different (e.g., `https://demoqa.com` for Elements tests).

### 4. Run Tests Locally
Use Maven commands to run TestNG tests with specific groups or the default `testng.xml` configuration. Refer to `commands.txt` for a complete list.

- **Run `ui` Group Tests**:
  ```bash
  mvn clean test -Dgroups=ui
  ```
  - Runs UI tests (e.g., `testGoToElementsPage`, `testFillTextBoxInElements`) in parallel for Chromium, Firefox, and Safari.

- **Run `api` Group Tests**:
  ```bash
  mvn clean test -Dgroups=api
  ```
  - Runs API tests (e.g., `testCreateUser` in `MinimalPlaywrightApiTest`).

- **Run `regression` Group Tests**:
  ```bash
  mvn clean test -Dgroups=regression
  ```
  - Runs regression tests (e.g., `testFillTextBoxInElements`).

- **Run Any Group**:
  ```bash
  mvn clean test -Dgroups=<group>
  ```
  - Example: `mvn clean test -Dgroups=navigation` (for `navigation` group).

- **Run Default Tests (testng.xml)**:
  ```bash
  mvn clean test
  ```
  - Runs tests defined in `testng.xml` (includes `ui` and `elements`, excludes `regression`).

- **Generate Allure Report Locally**:
  ```bash
  allure serve target/allure-results
  ```
  - Generates and opens an interactive HTML report in your browser after running tests.

- **Clean Project**:
  ```bash
  mvn clean
  ```
  - Removes the `target/` directory for a fresh build.

### 5. Run Tests via GitHub Actions
The framework is integrated with GitHub Actions to run tests and generate Allure reports automatically. Reports are hosted temporarily on Surge.sh, with plans for persistent hosting on GitHub Pages (currently troubleshooting a 404 error).

#### Steps to Run and View Reports
1. **Push Changes**:
   - Commit and push your changes to the `master` branch:
     ```bash
     git add .
     git commit -m "Run tests via GitHub Actions"
     git push origin master
     ```
   - This triggers the `playwright-tests.yml` workflow.

2. **Monitor the Workflow**:
   - Go to the **Actions** tab in your repository (`Ruglon/test-framework-playwright`).
   - Select the latest run of “Playwright TestNG Tests with Allure Report.”
   - Monitor the steps to ensure tests run and the report is generated.

3. **View the Allure Report**:
   - In the run summary, look for the “Allure Report (Temporary)” section.
   - Click the “View Temporary Allure Report” link (e.g., `https://allure-report-<run-id>.surge.sh`) to view the report in your browser.
   - Note: The GitHub Pages URL (`https://Ruglon.github.io/test-framework-playwright/`) is currently showing a 404 error. See the troubleshooting section below for details.

4. **Download the Report (Optional)**:
   - If Surge.sh hosting isn’t accessible, download the `allure-report` artifact from the workflow run (under “Artifacts”) and view it locally.

#### Configure Surge.sh Token
- **Create a Surge.sh Account**:
  - Sign up at `https://surge.sh/`.
  - Run `surge token` locally (after installing `surge` via `npm install -g surge`) to get your token.
- **Add Token to GitHub Secrets**:
  - Go to **Settings** > **Secrets and variables** > **Actions**.
  - Add a new secret named `SURGE_TOKEN` with the value from `surge token`.

### 6. IntelliJ IDEA
To run tests in IntelliJ IDEA:

1. Open the project in IntelliJ.
2. Configure Maven: `File > Settings > Build, Execution, Deployment > Build Tools > Maven > Maven home path` (e.g., `/usr/local/apache-maven-3.9.6` on Linux/macOS).
3. Run tests via terminal:
   ```bash
   mvn clean test -Dgroups=ui
   ```
4. Or create a TestNG run configuration:
    - `Run > Edit Configurations > Add New > TestNG`.
    - `Test kind`: `Group`, `Group`: `ui`, `api`, or `regression`.
    - Or, `Suite`: `src/test/resources/testng.xml`.
    - Save and run.
5. Generate Allure report:
   ```bash
   allure serve target/allure-results
   ```

### Troubleshooting
- **Maven Not Recognized**:
  - Verify: `mvn -version` (should show Maven 3.9.6).
  - Ensure `setup.ps1` or `setup.sh` was run and a new terminal session was opened.
  - Check `PATH`:
      - Windows: `echo $env:PATH` (look for Maven `bin` directory).
      - macOS/Linux: `echo $PATH` (look for `/usr/local/apache-maven-3.9.6/bin`).
  - Re-run `setup.ps1` or `setup.sh` if needed.

- **Allure Not Recognized**:
  - Verify: `allure --version` (should show 2.28.0 when installed locally).
  - Check `PATH`:
      - Windows: Look for Allure `bin` directory.
      - macOS/Linux: Look for `/usr/local/allure-2.28.0/bin`.
  - Open a new terminal session or manually add to `PATH`:
      - Windows: `$env:PATH += ";C:\path\to\allure-2.28.0\bin"`.
      - macOS/Linux: `export PATH=$PATH:/usr/local/allure-2.28.0/bin`.

- **Java 17 Not Found**:
  - Verify: `java -version` (should show 17.x).
  - Install from [Adoptium](https://adoptium.net/) if missing.
  - Ensure `JAVA_HOME` is set:
      - Windows: `echo $env:JAVA_HOME` (e.g., `C:\Program Files\Java\jdk-17`).
      - macOS/Linux: `echo $JAVA_HOME` (e.g., `/usr/lib/jvm/java-17-openjdk`).

- **Tests Not Running in Parallel**:
  - Confirm `testng.xml` has `parallel="methods"` and `thread-count="3"`.
  - Check `pom.xml` Surefire plugin:
    ```xml
    <parallel>methods</parallel>
    <threadCount>3</threadCount>
    ```

- **Form Assertion Fails**:
  - Update `SUBMISSION_OUTPUT` in `ElementsPage.java`:
    ```java
    private static final String SUBMISSION_OUTPUT = "div.output > p"; // Adjust based on DOM
    ```
  - Inspect the DOM at `ui.url` (e.g., `https://demoqa.com/elements`) after form submission.

- **Allure Reports Empty Locally**:
  - Ensure `target/allure-results` contains JSON files after running tests.
  - Verify `@Feature` and `@Description` annotations in `ElementsTest.java` or `MinimalPlaywrightApiTest.java`.
  - Run with debug: `mvn clean test -Dgroups=ui -X`.

## Contributing
1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`.
3. Commit changes: `git commit -m "Add feature"`.
4. Push: `git push origin feature-name`.
5. Open a pull request.

## Contact
For issues, open a ticket on the GitHub repository or contact the maintainer.

**Reference**: See `commands.txt` for a quick list of Maven commands to run tests and generate reports.