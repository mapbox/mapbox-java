const download = require('../src/download')

const chai = require('chai');
const assert = chai.assert
const should = chai.should();

describe('#downloadRelease()', function() {
  context('should successfully download a release', function() {
  
    it('should download results for 4 services', async function () {
      const services = await download.downloadRelease('5.5.0')

      services.should.be.an('array').to.have.lengthOf(4)
    });

    it('should return success for each result', async function() {
      const services = await download.downloadRelease('5.5.0')

      const success = services.every(value => value.result == "success")

      assert.isTrue(success, `expecting result values to be 'retry' ${JSON.stringify(services)}`)
    });
  })
});


// "https://dl.bintray.com/mapbox/mapbox/com/mapbox/mapboxsdk/mapbox-sdk-services/5.4.0/mapbox-sdk-services-5.4.0.jar",
// "https://dl.bintray.com/mapbox/mapbox/com/mapbox/mapboxsdk/mapbox-sdk-geojson/5.4.0/mapbox-sdk-geojson-5.4.0.jar",
// "https://dl.bintray.com/mapbox/mapbox/com/mapbox/mapboxsdk/mapbox-sdk-directions-models/5.4.0/mapbox-sdk-directions-models-5.4.0.jar",
// "https://dl.bintray.com/mapbox/mapbox/com/mapbox/mapboxsdk/mapbox-sdk-directions-refresh-models/5.4.0/mapbox-sdk-directions-refresh-models-5.4.0.jar"