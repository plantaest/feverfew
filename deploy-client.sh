#!/bin/bash

# Check parameters
if [ $# -ne 1 ]; then
  echo "Missing FILE_LINK parameter!"
  echo "Usage: ./deploy-client.sh <FILE_LINK>"
  exit 1
fi

# Get parameters
FILE_LINK=$1

# Create the builds directory if it doesn't exist
mkdir -p ~/html

# Create the VERSION directory (handle existing directory gracefully)
VERSION_DIR=~/html/$VERSION
if [ -d "$VERSION_DIR" ]; then
  echo "Version directory ($VERSION_DIR) already exists. Removing contents..."
  rm -rf "${VERSION_DIR:?}"/* || {
    echo "Error: Failed to remove contents of $VERSION_DIR. Please check permissions or try manually."
    exit 1
  }
fi
mkdir -p "$VERSION_DIR"

# Download zip file
curl -o "$VERSION_DIR"/feverfew-client.zip "$FILE_LINK"

# Unzip
cd "$VERSION_DIR" || exit
unzip feverfew-client.zip

# Remove zip file
rm feverfew-client.zip

# Display success message
echo "Done!"
