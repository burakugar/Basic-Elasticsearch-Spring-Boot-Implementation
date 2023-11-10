#!/bin/bash

# Check if Homebrew is installed
if ! type "brew" >/dev/null; then
  echo "Installing Homebrew..."
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

# Install Java JDK v21
echo "Installing Java JDK..."
brew install openjdk@21

# Set JAVA_HOME for the installed Java version
export JAVA_HOME=$(/usr/libexec/java_home -v 21) # Replace 21 with the version number installed
export PATH=$JAVA_HOME/bin:$PATH

# Verify Java installation
java -version || {
  echo "Java installation verification failed"
  exit 1
}

# Install Maven
echo "Installing Maven..."
brew install maven

# Verify Maven installation
mvn -version || {
  echo "Maven installation verification failed"
  exit 1
}

# Install Elasticsearch
echo "Installing Elasticsearch..."
brew tap elastic/tap
brew install elastic/tap/elasticsearch-full

brew services start elastic/tap/elasticsearch-full

# Navigate to the project root directory
cd "$(dirname "$0")"/.. || {
  echo "Failed to navigate to project root directory"
  exit 1
}

# Build the project using Maven
mvn clean install || {
  echo "Maven build failed"
  exit 1
}

# Run the Spring Boot application
java -jar target/elasticsearch-1.0.0-SNAPSHOT.jar || {
  echo "Failed to start Spring Boot application"
  exit 1
}

echo "Setup and application startup completed."
