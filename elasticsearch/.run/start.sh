#!/bin/bash
chmod +x start.sh
# Navigate to the script's directory
cd "$(dirname "$0")" || {
  echo "Failed to navigate to script directory"
  exit 1
}

# Update system repositories
apt-get update || {
  echo "Failed to update system repositories"
  exit 1
}
yes | sudo apt-get install systemd

apt-get update && apt-get install -y wget
# Check if JDK is already installed
if [ -d "/opt/jdk/jdk-21" ] && [ "$(ls -A /opt/jdk/jdk-21)" ]; then
  echo "JDK already installed, skipping JDK installation."
else
  # Install JDK
  wget https://download.java.net/java/GA/jdk21/fd2272bbf8e04c3dbaee13770090416c/35/GPL/openjdk-21_linux-x64_bin.tar.gz -O jdk.tar.gz
  tar -xzf jdk.tar.gz -C /opt/
  mv /opt/jdk-21 /opt/jdk
fi

# Set JAVA_HOME and update PATH
export JAVA_HOME=/opt/jdk
export PATH=$JAVA_HOME/bin:$PATH

# Check if Java is installed
if type -p java >/dev/null; then

  # Java found, print the version
  echo "Java installed"
  java -version

else

  # Java not found
  echo "Java not installed"

fi

# Install Maven
sudo apt-get install -y maven || {
  echo "Failed to install Maven"
  exit 1
}

# Check if Maven is installed
if type -p mvn >/dev/null; then

  # Maven found, print the version
  echo "Maven installed"
  mvn -version

else

  # Maven not found
  echo "Maven not installed"

fi
# Check if Elasticsearch 8.11.0 is already installed
if dpkg -l | grep -q "elasticsearch.*8.11.0"; then
  echo "Elasticsearch 8.11.0 is already installed, skipping installation."
else
  # Install Elasticsearch 8.11.0
  wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-8.11.0-amd64.deb || {
    echo "Failed to download Elasticsearch"
    exit 1
  }
  sudo dpkg -i elasticsearch-8.11.0-amd64.deb || {
    echo "Failed to install Elasticsearch"
    exit 1
  }
  # Enable and start Elasticsearch service
  sudo systemctl enable elasticsearch.service || {
    echo "Failed to enable Elasticsearch service"
    exit 1
  }
  sudo systemctl start elasticsearch.service || {
    echo "Failed to start Elasticsearch service"
    exit 1
  }
fi
# Navigate to the project root directory
cd .. || {
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
