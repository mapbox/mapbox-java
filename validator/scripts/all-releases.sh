#!/usr/bin/env bash

#
# This script helps run the validator. The validator loads jars and validates
# json on that loaded jar.
#
# The reason we loop through this is because the Node.js command line interface
# can only load one executable per run. If there is better way to clear the
# library then we could consider that and delete this script.
#

# This script requires a file with a DirectionsResponse in the form of json.
echo $#
if [[ $# -lt 1 ]]; then
  echo "Provide a path to the json file"
  exit 1
fi
JSON_FILE=$1

for var in "$@"; do
  JSON_FILE=$var
  echo Testing $JSON_FILE
  # Validate the json with each release that has been downloaded.
  # To download releases, use the validator command line interface
  FILES=releases/*
  for f in $FILES; do
    release="${f:9}"
    validator json $release $JSON_FILE
  done
done
