
## Introduction
This project is made to validate any json response with all releases.

The command line interface is called "validator". Use it to download releases,

and then run json on the releases. Check out the Makefile to get started.

You can also use the scripts/all-releases.sh to run the validator on every release.

## Getting started
cd validator
npm install

## Examples
### Download and test a release
validator download 5.6.0

sh scripts/all-releases.sh ../services-directions-models/src/test/resources/directions_*

### Download all releases and test a json
validator download-all

sh scripts/all-releases.sh json/tokyo-2020-09-22.json

### Explore the releases
- validator list-services
- validator list-releases
- validator list-releases mapbox-sdk-geojson
- validator list-releases mapbox-sdk-directions-models
- rm -rf releases