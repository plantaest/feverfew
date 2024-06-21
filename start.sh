#!/bin/bash

./caddy/caddy run --config ./Caddyfile &

./jre/jdk-21.0.3+9-jre/bin/java -jar ./jar/feverfew-"$VERSION"-runner.jar &

wait
