#!/bin/sh

while ! timeout 1 bash -c "echo > /dev/tcp/vote/80"; do
    sleep 1
done

curl -sS -X POST --data "vote=b" http://vote > /dev/null
sleep 10

if phantomjs render.js http://result | grep -q '1 vote'; then
  state = 0
  echo "Tests passed"
  echo "::set-output name=state::$state"
  exit 0
else
  state = 1
  echo "Tests failed"
  echo "::set-output name=state::$state"
  exit 1
fi
