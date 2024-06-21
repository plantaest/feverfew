#!/bin/bash

# Check parameters
if [ $# -ne 1 ]; then
  echo "Missing FILE_LINK parameter!"
  echo "Usage: ./deploy-server.sh <FILE_LINK>"
  exit 1
fi

# Get parameters
FILE_LINK=$1

# Create the builds directory if it doesn't exist
mkdir -p ~/jar

# Download jar file
curl -o ~/jar/feverfew-"$VERSION"-runner.jar "$FILE_LINK"

# Display success message
echo "Done!"
