#!/usr/bin/env node

const releases = require('./releases')
const download = require('./download')
const runner = require('./runner')

const DEFAULT_SERVICE = "mapbox-sdk-services"

const argv = require('yargs')
    .command('list-services', 'Request info on all available services', (argv) => {
        releases.listServices(services => {
            console.log(services)
        });
    })
    .command('list-releases [service]', 'Request releases for a specific service', (argv) => {
        argv.positional('service', {
            describe: 'The service you want to know the releases available',
            type: 'string',
            default:DEFAULT_SERVICE
        });
    }, handler = (argv) => {
        releases.listReleases(argv.service, release => {
            console.log(release)
        });
    })
    .command('download <release>', 'Download specific release', (argv) => {
        argv.positional('release', {
            describe: 'The release version',
            type: 'string'
        });
    }, handler = (argv) => {
        download.downloadRelease(argv.release);
    })
    .command('download-all', 'Download all the releases', (argv) => {
        releases.listReleases(DEFAULT_SERVICE, releases => {
            releases[0].versions.forEach(element => {
                download.downloadRelease(element)
            });
        });
    })
    .command('json <release> <file>', 'Validate the contents of a json file', (argv) => {
        argv.positional('release', {
            describe: 'The release version',
            type: 'string'
        });
        argv.positional('file', {
            describe: 'Path to a json file',
            type: 'string'
        });
    }, handler = (argv) => {
        const result = runner.validateJson(argv.release, argv.file);
        const resultsMessage = result.success ? "success" : `failure ${result.reason}`
        console.log(`${result.release} ${resultsMessage}`);
    })
    .help()
    .demandCommand(1)
    .parse()
