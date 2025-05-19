# setup.ps1: Install Maven and Allure for Windows

# Exit on error
$ErrorActionPreference = "Stop"

Write-Host "Checking Java..."
$javaExe = (Get-Command java -ErrorAction SilentlyContinue).Source
if (-not $javaExe) {
    Write-Host "Java is not installed or not in PATH. Please install JDK 11 or higher." -ForegroundColor Red
    exit 1
}
# Use --version to prefer stdout, fall back to -version
$javaVersionOutput = (cmd /c "$javaExe" --version 2>$null) | Out-String
if (-not $javaVersionOutput) {
    $javaVersionOutput = (cmd /c "$javaExe" -version 2>&1) | Out-String
}
$javaVersion = ($javaVersionOutput | Select-String -Pattern '(\d+)\.' | ForEach-Object { $_.Matches.Groups[1].Value })
if (-not $javaVersion -or [int]$javaVersion -lt 11) {
    Write-Host "Java 11 or higher is required. Found version: $javaVersion" -ForegroundColor Red
    exit 1
}
Write-Host "Java $javaVersion found." -ForegroundColor Green

# Check JAVA_HOME
Write-Host "Checking JAVA_HOME..."
if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    Write-Host "JAVA_HOME is not set or invalid. Attempting to find Java installation..."
    $javaHome = $javaExe
    for ($i = 0; $i -lt 4; $i++) {
        $javaHome = Split-Path -Path $javaHome -Parent
        if (Test-Path "$javaHome\bin\java.exe") {
            $env:JAVA_HOME = $javaHome
            [Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, [System.EnvironmentVariableTarget]::User)
            Write-Host "JAVA_HOME set to $javaHome" -ForegroundColor Green
            break
        }
    }
    if (-not $env:JAVA_HOME) {
        Write-Host "Warning: Could not find valid Java installation for JAVA_HOME." -ForegroundColor Yellow
        Write-Host "Please set JAVA_HOME manually to your JDK 11+ installation (e.g., C:\Program Files\Java\jdk-17)." -ForegroundColor Yellow
        Write-Host "Continuing setup, but JAVA_HOME may need to be set for some tools." -ForegroundColor Yellow
    }
} else {
    Write-Host "JAVA_HOME is set to $env:JAVA_HOME" -ForegroundColor Green
}

# Set variables
$MAVEN_VERSION = "3.9.6"
$ALLURE_VERSION = "2.30.0"
$INSTALL_DIR = "$env:USERPROFILE\tools"
$MAVEN_DIR = "$INSTALL_DIR\apache-maven-$MAVEN_VERSION"
$ALLURE_DIR = "$INSTALL_DIR\allure-$ALLURE_VERSION"

# Create install directory
New-Item -ItemType Directory -Force -Path $INSTALL_DIR | Out-Null

# Install Maven
Write-Host "Checking Maven..."
if (-not (Get-Command mvn -ErrorAction SilentlyContinue) -or -not (mvn -version | Select-String $MAVEN_VERSION)) {
    Write-Host "Installing Maven $MAVEN_VERSION..."
    Invoke-WebRequest -Uri "https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip" -OutFile "maven.zip"
    Expand-Archive -Path "maven.zip" -DestinationPath $INSTALL_DIR -Force
    Remove-Item "maven.zip"
    # Add Maven to PATH
    $currentPath = [Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::User)
    if (-not $currentPath.Contains("$MAVEN_DIR\bin")) {
        $newPath = "$currentPath;$MAVEN_DIR\bin"
        [Environment]::SetEnvironmentVariable("Path", $newPath, [System.EnvironmentVariableTarget]::User)
        $env:Path = $newPath
        Write-Host "Maven added to PATH: $MAVEN_DIR\bin" -ForegroundColor Green
    }
    # Set M2_HOME
    $env:M2_HOME = $MAVEN_DIR
    [Environment]::SetEnvironmentVariable("M2_HOME", $MAVEN_DIR, [System.EnvironmentVariableTarget]::User)
    Write-Host "M2_HOME set to $MAVEN_DIR" -ForegroundColor Green
} else {
    Write-Host "Maven $MAVEN_VERSION found." -ForegroundColor Green
    if (-not $env:M2_HOME -or -not (Test-Path "$env:M2_HOME\bin\mvn.cmd")) {
        $mvnPath = (Get-Command mvn).Source
        $m2Home = Split-Path -Path (Split-Path -Path $mvnPath -Parent) -Parent
        if (Test-Path "$m2Home\bin\mvn.cmd") {
            $env:M2_HOME = $m2Home
            [Environment]::SetEnvironmentVariable("M2_HOME", $m2Home, [System.EnvironmentVariableTarget]::User)
            Write-Host "M2_HOME set to $m2Home" -ForegroundColor Green
        } else {
            Write-Host "M2_HOME is invalid. Please set M2_HOME manually." -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "M2_HOME is set to $env:M2_HOME" -ForegroundColor Green
    }
}

# Install Allure
Write-Host "Checking Allure..."
if (-not (Get-Command allure -ErrorAction SilentlyContinue) -or -not (cmd /c allure --version 2>&1 | Select-String $ALLURE_VERSION)) {
    Write-Host "Installing Allure $ALLURE_VERSION..."
    $zipFile = "allure-$ALLURE_VERSION.zip"
    $downloadUrl = "https://github.com/allure-framework/allure2/releases/download/$ALLURE_VERSION/$zipFile"
    try {
        Invoke-WebRequest -Uri $downloadUrl -OutFile $zipFile
        Expand-Archive -Path $zipFile -DestinationPath $INSTALL_DIR -Force
        Remove-Item $zipFile
        if (-not (Test-Path "$ALLURE_DIR\bin\allure.bat")) {
            Write-Host "Allure installation failed: allure.bat not found in $ALLURE_DIR\bin" -ForegroundColor Red
            exit 1
        }
    } catch {
        Write-Host "Failed to download or extract Allure: $_" -ForegroundColor Red
        exit 1
    }
    # Add Allure to PATH
    $currentPath = [Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::User)
    if (-not $currentPath.Contains("$ALLURE_DIR\bin")) {
        $newPath = "$currentPath;$ALLURE_DIR\bin"
        [Environment]::SetEnvironmentVariable("Path", $newPath, [System.EnvironmentVariableTarget]::User)
        $env:Path = $newPath
        Write-Host "Allure added to PATH: $ALLURE_DIR\bin" -ForegroundColor Green
    }
}
Write-Host "Allure $ALLURE_VERSION installed." -ForegroundColor Green

Write-Host "Setup complete! Run 'mvn clean test -Dgroups=ui' to execute tests." -ForegroundColor Green
Write-Host "Note: Open a new PowerShell session to ensure PATH updates take effect." -ForegroundColor Yellow