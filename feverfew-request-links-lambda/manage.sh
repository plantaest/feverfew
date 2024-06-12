#!/bin/bash

export "$(grep -v '^#' .env | xargs)"

if [ -z "$REGION" ]; then
  echo "Usage: REGION=<region> $0"
  exit 1
fi

if [ -z "$INDEX" ]; then
  echo "Usage: INDEX=<index> $0"
  exit 1
fi

function cmd_create() {
  echo Creating function
  set -x
  aws lambda create-function \
    --function-name ${FUNCTION_NAME} \
    --zip-file ${ZIP_FILE} \
    --handler ${HANDLER} \
    --runtime ${RUNTIME} \
    --role ${LAMBDA_ROLE_ARN} \
    --timeout 30 \
    --memory-size ${MEMORY_SIZE} \
    --architectures arm64 \
    --region ${REGION} \
    ${LAMBDA_META}
# Enable and move this param above ${LAMBDA_META}, if using AWS X-Ray
#    --tracing-config Mode=Active \
}

function cmd_delete() {
  echo Deleting function
  set -x
  aws lambda delete-function --function-name ${FUNCTION_NAME} --region ${REGION}
}

function cmd_invoke() {
  echo Invoking function

  inputFormat=""
  if [ $(aws --version | awk '{print substr($1,9)}' | cut -c1-1) -ge 2 ]; then inputFormat="--cli-binary-format raw-in-base64-out"; fi

  set -x

  aws lambda invoke response.txt \
    ${inputFormat} \
    --function-name ${FUNCTION_NAME} \
    --payload file://payload.json \
    --log-type Tail \
    --query 'LogResult' \
    --region ${REGION} \
    --output text |  base64 --decode
  { set +x; } 2>/dev/null
  cat response.txt && rm -f response.txt
}

function cmd_update() {
  echo Updating function
  set -x
  aws lambda update-function-code \
    --function-name ${FUNCTION_NAME} \
    --zip-file ${ZIP_FILE} \
    --region ${REGION}
}

FUNCTION_NAME="FeverfewRequestLinksLambda_JVM_${REGION}_${INDEX}"
HANDLER=io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest
RUNTIME=java21
ZIP_FILE=fileb:///Users/dang/Documents/GitHub/plantaest/feverfew/feverfew-request-links-lambda/target/function.zip
if [ "$1" == "native" ]; then
  MEMORY_SIZE=128
else
  MEMORY_SIZE=256
fi

function usage() {
  [ "_$1" == "_" ] && echo -e "\nUsage (JVM): \n$0 [create|delete|invoke]\ne.g.: $0 invoke"
  [ "_$1" == "_" ] && echo -e "\nUsage (Native): \n$0 native [create|delete|invoke]\ne.g.: $0 native invoke"

  [ "_" == "_`which aws 2>/dev/null`" ] && echo -e "\naws CLI not installed. Please see https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html"
  [ ! -e $HOME/.aws/credentials ] && [ "_$AWS_ACCESS_KEY_ID" == "_" ] && echo -e "\naws configure not setup.  Please execute: aws configure"
  [ "_$LAMBDA_ROLE_ARN" == "_" ] && echo -e "\nEnvironment variable must be set: LAMBDA_ROLE_ARN\ne.g.: export LAMBDA_ROLE_ARN=arn:aws:iam::123456789012:role/my-example-role"
}

if [ "_$1" == "_" ] || [ "$1" == "help" ]
 then
  usage
fi

if [ "$1" == "native" ]
then
  RUNTIME=provided.al2023
  ZIP_FILE=fileb:///Users/dang/Documents/GitHub/plantaest/feverfew/feverfew-request-links-lambda/target/function.zip
  FUNCTION_NAME="FeverfewRequestLinksLambda_Native_${REGION}_${INDEX}"
  LAMBDA_META="--environment Variables={DISABLE_SIGNAL_HANDLERS=true}"
  shift
fi

while [ "$1" ]
do
  eval cmd_${1}
  { set +x; } 2>/dev/null
  shift
done
