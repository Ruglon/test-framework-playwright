# Makefile for Playwright TestNG project
.PHONY: run run_ui run_regression clean report help

# Default target
help:
	@echo "Usage: make <target>"
	@echo "Targets:"
	@echo "  run <group>       Run tests for the specified group (e.g., make run ui)"
	@echo "  run_ui            Run tests for the 'ui' group"
	@echo "  run_regression    Run tests for the 'regression' group"
	@echo "  clean             Clean the project (remove target directory)"
	@echo "  report            Generate and serve Allure report"
	@echo "  help              Show this help message"

# Run tests for a specific group
run:
	@echo "Usage: make run <group>"
	@echo "Example: make run ui"
	@exit 1

run_%:
	mvn clean test -Dgroups=$*

# Run ui group tests
run_ui:
	mvn clean test -Dgroups=ui

# Run regression group tests
run_regression:
	mvn clean test -Dgroups=regression

# Clean project
clean:
	mvn clean

# Generate and serve Allure report
report:
	allure serve target/allure-results