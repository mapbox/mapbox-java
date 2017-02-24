# Mapbox Java and Android Services Documentation

The documentation for this repository's currently hosted publicly on as a [Github page](https://mapbox.github.io/mapbox-java/). The documentation content's versioned and once a new release of mapbox-java occurs, a new version folder should be created were new changes should go. All content files are written in markdown and contribution and fixes are welcome inside the documentation.

### Requirements

* Node v4 or higher
* NPM
* Git

### Running locally

1. Clone this repository
2. switch to a new branch from `gh-pages`
3. `npm install`
4. `npm start`
5. Open http://localhost:9966/

Begin making your content or code changes and open a new pull request when you are ready for review.

### Before merging

The `npm run build` command builds a `bundle.js` file that contains all the JavaScript code and content needed to show the site, and creates an `index.html` file that already contains the site content. Note that this _replaces_ the existing `index.html` file, so it's best to run this only when deploying the site and to undo changes to `index.html` if you want to keep working on content.

This is required before merging a pull request so your changes actually show up on the public website. 
