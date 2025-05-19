# Playwright TestNG Automation Framework

A robust test automation framework for UI testing using **Playwright**, **TestNG**, and **Allure** reporting. Built with Java 11+, it supports cross-browser testing (Chromium, Firefox, Safari/WebKit) with parallel execution and group-based test filtering (e.g., `ui`, `regression`). The framework tests web applications, such as the Elements page for form submission, and generates detailed Allure reports.

## Features
- **Cross-Browser Testing**: Tests run on Chromium, Firefox, and Safari (mapped to WebKit).
- **Parallel Execution**: Configured with `parallel="methods"` and `thread-count="3"`.
- **Test Groups**: Filter tests by groups (`ui`, `navigation`, `form`, `regression`).
- **Allure Reports**: Interactive reports with test steps and metadata.
- **Cross-Platform**: Setup scripts for Windows, macOS, and Linux.
- **Modular Design**: Page Object Model with `BaseTest`, `ElementsTest`, `ElementsSteps`, and `BasePage`.

## Project Structure
```
test-framework-playwright/
├── src/
│   ├── test/
│   │   ├── java/
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
│   │       ├── config.properties
│   │       └── testng.xml
├── pom.xml
├── setup.sh
├── setup.ps1
├── commands.txt
└── README.md
```

## Prerequisites
- **Java 11 or higher**: Install from [Adoptium](https://adoptium.net/) (e.g., 11, 17, or later).
- **Git**: To clone the repository.
- **Internet**: For downloading Maven and Allure.

## Setup Instructions

### 1. Clone the Repository
Clone the project and navigate to the root directory:

```bash
git clone <repository-url>
cd test-framework-playwright
```

### 2. Install Dependencies
Run the setup script for your operating system to install **Maven 3.9.6**, **Allure 2.30.0**, and configure `JAVA_HOME` and `M2_HOME`:

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

**Note**: If `JAVA_HOME` cannot be set automatically, the script will provide instructions to set it manually (e.g., `C:\Program Files\Java\jdk-17` on Windows, `/usr/lib/jvm/java-17-openjdk` on Linux). The setup supports Java 11 or higher.

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

### 4. Run Tests
Use Maven commands to run TestNG tests with specific groups or the default `testng.xml` configuration. Refer to `commands.txt` for a complete list.

- **Run `ui` Group Tests**:
  ```bash
  mvn clean test -Dgroups=ui
  ```
    - Runs six tests (`testGoToElementsPage`, `testFillTextBoxInElements`) in parallel for Chromium, Firefox, and Safari.

- **Run `regression` Group Tests**:
  ```bash
  mvn clean test -Dgroups=regression
  ```
    - Runs three tests (`testFillTextBoxInElements`).

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

- **Generate Allure Report**:
  ```bash
  allure serve target/allure-results
  ```
    - Generates and opens an interactive HTML report in your browser after running tests.

- **Clean Project**:
  ```bash
  mvn clean
  ```
    - Removes the `target/` directory for a fresh build.

### 5. IntelliJ IDEA
To run tests in IntelliJ IDEA:

1. Open the project in IntelliJ.
2. Configure Maven: `File > Settings > Build, Execution, Deployment > Build Tools > Maven > Maven home path` (e.g., `C:\Users\belar\tools\apache-maven-3.9.6` on Windows, `/home/user/tools/apache-maven-3.9.6` on Linux).
3. Run tests via terminal:
   ```bash
   mvn clean test -Dgroups=ui
   ```
4. Or create a TestNG run configuration:
    - `Run > Edit Configurations > Add New > TestNG`.
    - `Test kind`: `Group`, `Group`: `ui` or `regression`.
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
        - Windows: `echo $env:PATH` (look for `C:\Users\belar\tools\apache-maven-3.9.6\bin`).
        - macOS/Linux: `echo $PATH` (look for `/home/user/tools/apache-maven-3.9.6/bin`).
    - Re-run `setup.ps1` or `setup.sh` if needed.

- **Allure Not Recognized**:
    - Verify: `allure --version` (should show 2.30.0).
    - Check `PATH`:
        - Windows: `C:\Users\belar\tools\allure-2.30.0\bin`.
        - macOS/Linux: `/home/user/tools/allure-2.30.0/bin`.
    - Open a new terminal session or manually add to `PATH`:
        - Windows: `$env:PATH += ";C:\Users\belar\tools\allure-2.30.0\bin"`.
        - macOS/Linux: `export PATH=$PATH:/home/user/tools/allure-2.30.0/bin`.

- **Java 11+ Not Found**:
    - Verify: `java -version` (should show 11.x, 17.x, or later).
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

- **Allure Reports Empty**:
    - Ensure `target/allure-results` contains JSON files after running tests.
    - Verify `@Feature` and `@Description` annotations in `ElementsTest.java`.
    - Run with debug: `mvn clean test -Dgroups=ui -X`.

## Contributing
1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`.
3. Commit changes: `git commit -m "Add feature"`.
4. Push: `git push origin feature-name`.
5. Open a pull request.

## License
[MIT License](LICENSE) (update as needed).

## Contact
For issues, open a ticket on the Git repository or contact the maintainer.

**Reference**: See `commands.txt` for a quick list of Maven commands to run tests and generate reports.