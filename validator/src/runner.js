const fs = require('fs')
const java = require('java');

function validateJsonData(release, data) {
    const releaseDirectory = `releases/${release}`

    java.classpath.push(`./${releaseDirectory}/mapbox-sdk-services.jar`);
    java.classpath.push(`./${releaseDirectory}/mapbox-sdk-geojson.jar`);
    java.classpath.push(`./${releaseDirectory}/mapbox-sdk-directions-models.jar`);
    java.classpath.push(`./${releaseDirectory}/mapbox-sdk-directions-refresh-models.jar`);

    // TODO there should be a better place to put this.
    java.classpath.push("lib/gson-2.8.6.jar");

    const result = {
        release:`${release}`
    }
    try {
        const directionStatic = java.import("com.mapbox.api.directions.v5.models.DirectionsResponse");
        const fromJsonResult = directionStatic.fromJsonSync(data)
        result.success=true
    } catch (err) {
        result.success=false
        result.reason=`${err}`
    }

    return result
}

module.exports = {
    validateJson: function(release, file) {
        const data = fs.readFileSync(file, 'utf8');
        return validateJsonData(release, data);
    }
}
