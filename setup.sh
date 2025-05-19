#!/bin/bash

# setup.sh: Install Maven and Allure for macOS/Linux

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

# Create install directory
mkdir -p "$INSTALL_DIR"

# Install unzip if not present
if ! command -v unzip &> /dev/null; then
    echo "Installing unzip..."
    if [ "$OS" = "Darwin" ]; then
        brew install unzip
    else
        sudo apt-get update && sudo apt-get install -y unzip || sudo yum install -y unzip
    fi
fi

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
        echo "export M2_HOME=$MAVEN_DIR" >> ~/.bashrc
        echo "export PATH=$MAVEN_DIR/bin:$PATH" >> ~/.bashrc
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
            echo "export M2_HOME=$M2_HOME" >> ~/.bashrc
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
    ZIP_FILE="allure-$ALLURE_VERSION.zip"
    curl -O "https://github.com/allure-framework/allure2/releases/download/$ALLURE_VERSION/$ZIP_FILE"
    unzip -o "$ZIP_FILE" -d "$INSTALL_DIR"
    rm "$ZIP_FILE"
    if ! [ -f "$ALLURE_DIR/bin/allure" ]; then
        echo -e "${RED}Allure installation failed: allure not found in $ALLURE_DIR/bin${NC}"
        exit 1
    fi
    # Add Allure to PATH
    if ! echo "$PATH" | grep -q "$ALLURE_DIR/bin"; then
        echo "export PATH=$ALLURE_DIR/bin:$PATH" >> ~/.bashrc
        export PATH=$ALLURE_DIR/bin:$PATH
        echo -e "${GREEN}Allure added to PATH: $ALLURE_DIR/bin${NC}"
    fi
else
    echo -e "${GREEN}Allure $ALLURE_VERSION found.${NC}"
    if ! echo "$PATH" | grep -q "$ALLURE_DIR/bin"; then
        echo "export PATH=$ALLURE_DIR/bin:$PATH" >> ~/.bashrc
        export PATH=$ALLURE_DIR/bin:$PATH
        echo -e "${GREEN}Allure added to PATH: $ALLURE_DIR/bin${NC}"
    fi
fi
echo -e "${GREEN}Allure $ALLURE_VERSION installed.${NC}"

echo -e "${GREEN}Setup complete! Run 'mvn clean test -Dgroups=ui' to execute tests.${NC}"
echo -e "${YELLOW}Note: Open a new terminal to ensure PATH updates for 'mvn' and 'allure' take effect.${NC}"