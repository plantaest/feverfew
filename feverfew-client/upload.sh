#!/bin/bash

ZIP_NAME="feverfew-client.zip"

cd dist || exit

zip -r ../$ZIP_NAME ./*

cd ../

echo "Successfully created zip file: $ZIP_NAME"

CURRENT_TIME=$(date +%s)
EXPIRATION_TIME=$((CURRENT_TIME + 3600))
EXPIRATION=$(date -u -r $EXPIRATION_TIME +"%Y-%m-%dT%H:%M:%SZ")

RESPONSE=$(curl -s -X 'POST' \
  'https://file.io/' \
  -H 'accept: application/json' \
  -H 'Content-Type: multipart/form-data' \
  -F "file=@$ZIP_NAME;type=text/html" \
  -F "expires=$EXPIRATION" \
  -F 'maxDownloads=1' \
  -F 'autoDelete=true')

LINK=$(echo "$RESPONSE" | grep -o '"link":"[^"]*"' | sed 's/"link":"\([^"]*\)"/\1/')

echo "Uploaded $ZIP_NAME. Download link: $LINK"

rm $ZIP_NAME
