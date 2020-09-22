const fetch = require("node-fetch");
const fs = require('fs');

module.exports = {
    listServices: function(callback) {
        const servicesUrl = "https://api.bintray.com/search/packages/maven?g=com.mapbox.mapboxsdk"
        fetch(servicesUrl, { method: 'GET' })
            .then( response => response.json() )
            .then( json => callback(json) )
            .catch( error => console.error('Unable to list services:', error) );
    },
    listReleases: function(service, callback) {
        const versionUrl = `https://api.bintray.com/search/packages/maven?g=com.mapbox.mapboxsdk&a=${service}`
        fetch(versionUrl, { method: 'GET' })
            .then( response => response.json() )
            .then( json => callback(json) )
            .catch( error => console.error('Unable to list release versions:', error) );
    },
    info: function(release, service) {
        const releaseDirectory = this.mkdir(release)
        const relativePath = `${releaseDirectory}/${service}.jar`
        const exists = fs.existsSync(relativePath)
        const result = {
            release:`${release}`,
            exists:exists
        }
        if (exists) {
            const stats = fs.statSync(relativePath);
            result.sizeMB = stats.size / 1000000.0
            result.downloaded = stats.birthtime
        }
        return result
    },
    mkdir: function(release) {
        const outputDirectory = "releases"
        if (!fs.existsSync(outputDirectory)) {
            fs.mkdirSync(outputDirectory);
        }
        const releaseDirectory = `${outputDirectory}/${release}`
        if (!fs.existsSync(releaseDirectory)) {
            fs.mkdirSync(releaseDirectory);
        }
        return `./${releaseDirectory}`
    }
};
