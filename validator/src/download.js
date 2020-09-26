const fetch = require("node-fetch");
const fs = require('fs')
const util = require('util')
const releases = require('./releases')

function downloadUrl(release, service) {
    // https://dl.bintray.com/<SUBJECT_NAME>/<REPO_NAME>/<VERSION>/<FILE_PATH>
    const path = `mapbox/mapbox/com/mapbox/mapboxsdk/${service}`
    const query = `${release}/${service}-${release}.jar`
    return `https://dl.bintray.com/${path}/${query}`
}

async function write(release, service, response) {
    const releaseDirectory = releases.mkdir(release)
    const relativePath = `${releaseDirectory}/${service}.jar`
    if (!fs.existsSync(relativePath)) {
        const streamPipeline = util.promisify(require('stream').pipeline)
        await streamPipeline(response.body, fs.createWriteStream(relativePath))
    }
};

async function downloadService(release, service) {
    const releaseInfo = releases.info(release, service)
    const url = downloadUrl(release, service)
    if (releaseInfo.exists) {
        return {
            url:`${url}`,
            service:`${service}`,
            release:`${release}`,
            result:"success"
        };
    }

    const response = await fetch(url)
        .then( response => {
            if (response.ok) {
                write(release, service, response);
            }
            const result = response.ok ? "success" : "retry"
            console.log(`${url} ${response.status}`)
            const downloadStatus = {
                url:`${url}`,
                service:`${service}`,
                release:`${release}`,
                result:`${result}`
            };
            return downloadStatus
        })
        .catch( error => {
            return {
                url:`${url}`,
                service:`${service}`,
                release:`${release}`,
                result:"failure"
            };
        });
    
    return response
};

module.exports = {
    downloadRelease: async function(release) {
        const sdkServices = downloadService(release, "mapbox-sdk-services")
        const sdkGeojson = downloadService(release, "mapbox-sdk-geojson")
        const directionsModels = downloadService(release, "mapbox-sdk-directions-models")
        const directionsRefreshModels = downloadService(release, "mapbox-sdk-directions-refresh-models")
        return await Promise.all([sdkServices, sdkGeojson, directionsModels, directionsRefreshModels]);
    }
}
