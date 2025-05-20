#!/bin/bash

# setup.sh: Install Maven, Allure, and Playwright dependencies for macOS/Linux

# Exit on error
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Check Java 11+
echo "Checking Java..."
if ! command -v java &> /dev/null; then
    echo -e "${RED}Java is not installed. Please install JDK 11 or higher.${NC}"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 || true)
JAVA_MAJOR_VERSION=$(echo "$JAVA_VERSION" | grep -oP '"\K\d+(?=\.)' | head -1)
if [ -z "$JAVA_MAJOR_VERSION" ] || [ "$JAVA_MAJOR_VERSION" -lt 11 ]; then
    echo -e "${RED}Java 11 or higher is required. Found version: $JAVA_MAJOR_VERSION${NC}"
    exit 1
fi
echo -e "${GREEN}Java $JAVA_MAJOR_VERSION found.${NC}"

# Check JAVA_HOME
echo "Checking JAVA_HOME..."
if [ -z "$JAVA_HOME" ] || ! [ -x "$JAVA_HOME/bin/java" ]; then
    echo "JAVA_HOME is not set or invalid. Attempting to find Java installation..."
    JAVA_PATH=$(which java || true)
    if [ -n "$JAVA_PATH" ]; then
        JAVA_HOME=$(dirname $(dirname $(readlink -f "$JAVA_PATH" 2>/dev/null || realpath "$JAVA_PATH" 2>/dev/null || echo "$JAVA_PATH")))
        for i in {1..4}; do
            if [ -x "$JAVA_HOME/bin/java" ]; then
                echo "export JAVA_HOME=$JAVA_HOME" >> ~/.bashrc
                export JAVA_HOME
                echo -e "${GREEN}JAVA_HOME set to $JAVA_HOME${NC}"
                break
            fi
            JAVA_HOME=$(dirname "$JAVA_HOME")
        done
        if [ -z "$JAVA_HOME" ] || ! [ -x "$JAVA_HOME/bin/java" ]; then
            echo -e "${YELLOW}Warning: Could not find valid Java installation for JAVA_HOME.${NC}"
            echo -e "${YELLOW}Please set JAVA_HOME manually to your JDK 11+ installation.${NC}"
            echo -e "${YELLOW}Continuing setup, but JAVA_HOME may need to be set for some tools.${NC}"
        fi
    else
        echo -e "${RED}Java not found in PATH. Please install JDK 11 or higher.${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}JAVA_HOME is set to $JAVA_HOME${NC}"
fi

# Set variables
MAVEN_VERSION="3.9.6"
ALLURE_VERSION="2.30.0"
INSTALL_DIR="$HOME/tools"
MAVEN_DIR="$INSTALL_DIR/apache-maven-$MAVEN_VERSION"
ALLURE_DIR="$INSTALL_DIR/allure-$ALLURE_VERSION"
OS=$(uname -s)
SHELL_CONFIG="$HOME/.bashrc"
if [ "$SHELL" = "/bin/zsh" ]; then
    SHELL_CONFIG="$HOME/.zshrc"
fi

# Create install directory
mkdir -p "$INSTALL_DIR"

# Install unzip if not present
if ! command -v unzip &> /dev/null; then
    echo "Installing unzip..."
    if [ "$OS" = "Darwin" ]; then
        brew install unzip
    else
        sudo apt-get update && sudo apt-get install -y unzip || sudo dnf install -y unzip
    fi
fi

# Install Node.js for Playwright (version 20)
echo "Checking Node.js..."
REQUIRED_NODE_VERSION=18
NODE_VERSION=$(node --version 2>/dev/null | grep -oP '\d+' | head -1 || echo 0)
if ! command -v node &> /dev/null || [ "$NODE_VERSION" -lt "$REQUIRED_NODE_VERSION" ]; then
    echo "Installing Node.js 20..."
    if [ "$OS" = "Linux" ]; then
        if command -v apt-get &> /dev/null; then
            # Remove old nodejs to avoid conflicts
            sudo apt-get remove -y nodejs npm || true
            sudo apt-get autoremove -y || true
            # Add NodeSource repository and install
            curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash - || { echo -e "${RED}Failed to add NodeSource repository${NC}"; exit 1; }
            sudo apt-get update || { echo -e "${RED}Failed to run apt-get update${NC}"; exit 1; }
            sudo apt-get install -y nodejs || { echo -e "${RED}Failed to install Node.js${NC}"; exit 1; }
        elif command -v dnf &> /dev/null; then
            curl -fsSL https://rpm.nodesource.com/setup_20.x | sudo bash - || { echo -e "${RED}Failed to add NodeSource repository${NC}"; exit 1; }
            sudo dnf install -y nodejs || { echo -e "${RED}Failed to install Node.js${NC}"; exit 1; }
        else
            echo -e "${YELLOW}Warning: Unknown package manager. Please install Node.js 18+ manually.${NC}"
            exit 1
        fi
    elif [ "$OS" = "Darwin" ]; then
        brew install node@20
        echo 'export PATH="/usr/local/opt/node@20/bin:$PATH"' >> "$SHELL_CONFIG"
    fi
    # Verify Node.js installation
    if ! command -v node &> /dev/null || [ "$(node --version | grep -oP '\d+' | head -1)" -lt "$REQUIRED_NODE_VERSION" ]; then
        echo -e "${RED}Node.js installation failed or version is still < 18${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}Node.js $(node --version) found.${NC}"
fi

# Install Playwright dependencies
echo "Installing Playwright dependencies..."
if command -v npx &> /dev/null; then
    npx playwright install-deps || { echo -e "${RED}Failed to install Playwright dependencies${NC}"; exit 1; }
else
    echo "Installing Playwright dependencies manually..."
    if [ "$OS" = "Linux" ]; then
        if command -v apt-get &> /dev/null; then
            sudo apt-get update && sudo apt-get install -y libevent-2.1-7 libflite1 || { echo -e "${RED}Failed to install libevent or libflite${NC}"; exit 1; }
        elif command -v dnf &> /dev/null; then
            sudo dnf install -y libevent flite || { echo -e "${RED}Failed to install libevent or flite${NC}"; exit 1; }
        else
            echo -e "${YELLOW}Warning: Unknown package manager. Please install libevent and flite manually.${NC}"
        fi
    fi
fi
echo -e "${GREEN}Playwright dependencies installed.${NC}"

# Install Playwright browsers
if command -v npx &> /dev/null; then
    npx playwright install || { echo -e "${RED}Failed to install Playwright browsers${NC}"; exit 1; }
else
    echo -e "${YELLOW}Warning: Node.js not found. Please run 'npx playwright install' manually after installing Node.js.${NC}"
fi
echo -e "${GREEN}Playwright browsers installed.${NC}"

# Install Maven
echo "Checking Maven..."
if ! command -v mvn &> /dev/null || ! mvn -version | grep -q "$MAVEN_VERSION"; then
    echo "Installing Maven $MAVEN_VERSION..."
    if [ "$OS" = "Darwin" ] && command -v brew &> /dev/null; then
        brew install maven
    else
        curl -O "https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz"
        tar xzvf "apache-maven-$MAVEN_VERSION-bin.tar.gz" -C "$INSTALL_DIR"
        rm "apache-maven-$MAVEN_VERSION-bin.tar.gz"
    fi
    # Add Maven to PATH
    if ! echo "$PATH" | grep -q "$MAVEN_DIR/bin"; then
        echo "export M2_HOME=$MAVEN_DIR" >> "$SHELL_CONFIG"
        echo "export PATH=$MAVEN_DIR/bin:$PATH" >> "$SHELL_CONFIG"
        export M2_HOME=$MAVEN_DIR
        export PATH=$MAVEN_DIR/bin:$PATH
        echo -e "${GREEN}M2_HOME set to $MAVEN_DIR${NC}"
        echo -e "${GREEN}Maven added to PATH: $MAVEN_DIR/bin${NC}"
    fi
else
    echo -e "${GREEN}Maven $MAVEN_VERSION found.${NC}"
    if [ -z "$M2_HOME" ] || ! [ -x "$M2_HOME/bin/mvn" ]; then
        MAVEN_PATH=$(which mvn)
        M2_HOME=$(dirname $(dirname "$MAVEN_PATH"))
        if [ -x "$M2_HOME/bin/mvn" ]; then
            echo "export M2_HOME=$M2_HOME" >> "$SHELL_CONFIG"
            export M2_HOME
            echo -e "${GREEN}M2_HOME set to $M2_HOME${NC}"
        else
            echo -e "${RED}M2_HOME is invalid. Please set M2_HOME manually.${NC}"
            exit 1
        fi
    else
        echo -e "${GREEN}M2_HOME is set to $M2_HOME${NC}"
    fi
fi

# Install Allure
echo "Checking Allure..."
if ! command -v allure &> /dev/null || ! allure --version | grep -q "$ALLURE_VERSION"; then
    echo "Installing Allure $ALLURE_VERSION..."
    # Try Homebrew first
    if command -v brew &> /dev/null; then
        brew install allure || echo -e "${YELLOW}Homebrew failed, trying manual install${NC}"
    fi
    # Manual install with .tgz
    if ! command -v allure &> /dev/null; then
        TGZ_FILE="allure-$ALLURE_VERSION.tgz"
        curl -O "https://github.com/allure-framework/allure2/releases/download/$ALLURE_VERSION/$TGZ_FILE" || { echo -e "${RED}Failed to download Allure${NC}"; exit 1; }
        tar xzvf "$TGZ_FILE" -C "$INSTALL_DIR" || { echo -e "${RED}Failed to extract Allure${NC}"; exit 1; }
        rm "$TGZ_FILE"
        EXTRACTED_DIR=$(find "$INSTALL_DIR" -maxdepth 1 -type d -name "allure*")
        if [ -z "$EXTRACTED_DIR" ] || ! [ -f "$EXTRACTED_DIR/bin/allure" ]; then
            echo -e "${RED}Allure installation failed: allure not found in $EXTRACTED_DIR/bin${NC}"
            exit 1
        fi
        ALLURE_DIR="$EXTRACTED_DIR"
        chmod +x "$ALLURE_DIR/bin/allure"
    fi
    # Add Allure to PATH
    if ! echo "$PATH" | grep -q "$ALLURE_DIR/bin"; then
        echo "export PATH=$ALLURE_DIR/bin:$PATH" >> "$SHELL_CONFIG"
        export PATH=$ALLURE_DIR/bin:$PATH
        source "$SHELL_CONFIG"
        echo -e "${GREEN}Allure added to PATH: $ALLURE_DIR/bin${NC}"
    fi
else
    echo -e "${GREEN}Allure $ALLURE_VERSION found.${NC}"
fi
echo -e "${GREEN}Allure $ALLURE_VERSION installed.${NC}"

echo -e "${GREEN}Setup complete! Run 'mvn clean test -Dgroups=ui' to execute tests.${NC}"
echo -e "${YELLOW}Note: Open a new terminal or run 'source ~/.bashrc' to ensure PATH updates for 'mvn' and 'allure' take effect.${NC}"