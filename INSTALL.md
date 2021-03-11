# Building & Developing Mapbox Android Services from Source

**Just trying to use Mapbox Android Services in your application? You don't need to read this stuff! We provide [easy-to-install, prebuilt versions of MAS for Android and Java that you can download instantly and get started with fast](https://www.mapbox.com/android-sdk/#mapbox_android_services).**

Still with us? These are the instructions you'll need to build Mapbox Android Services from source.

Your journey will start with getting the source code, adding your Mapbox access token for testing, and begin developing.

## 1: Getting the Source

Clone the git repository:

    git clone https://github.com/mapbox/mapbox-java.git
    cd mapbox-java

## 2: Adding Mapbox access token

Using the MAS testapp will require a valid [Mapbox access token](https://www.mapbox.com/help/define-access-token/). It is recommend to create a `res/values/developer-config.xml` development file which will include your access token. Use the example below for reference:

```xml
<resources>
    <string name="mapbox_access_token"><your access token here></string>
</resources>
```

## 3. Add an `SDK_REGISTRY_TOKEN`

Add the gradle property or env variable with a **secret token**. The token needs to have the `DOWNLOADS:READ` scope. You can obtain the token from your [Mapbox Account page](https://account.mapbox.com/access-tokens/).

## 4: Begin developing

We recommend using [Android Studio](https://developer.android.com/studio/index.html) to work on the code base.
