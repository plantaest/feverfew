#!/bin/bash

# Check parameters
if [ $# -ne 1 ]; then
  echo "Missing VERSION parameter!"
  echo "Usage: ./upload.sh <VERSION>"
  exit 1
fi

# Get parameters
VERSION=$1

JAR_NAME="feverfew-$VERSION-runner.jar"

CURRENT_TIME=$(date +%s)
EXPIRATION_TIME=$((CURRENT_TIME + 3600))
EXPIRATION=$(date -u -r $EXPIRATION_TIME +"%Y-%m-%dT%H:%M:%SZ")

RESPONSE=$(curl -s -X 'POST' \
  'https://file.io/' \
  -H 'accept: application/json' \
  -H 'Content-Type: multipart/form-data' \
  -F "file=@target/$JAR_NAME;type=text/html" \
  -F "expires=$EXPIRATION" \
  -F 'maxDownloads=1' \
  -F 'autoDelete=true')

LINK=$(echo "$RESPONSE" | grep -o '"link":"[^"]*"' | sed 's/"link":"\([^"]*\)"/\1/')

echo "Uploaded $JAR_NAME. Download link: $LINK"
