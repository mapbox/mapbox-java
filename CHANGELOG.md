# Change Log

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

<a name="1.0.4"></a>
## [1.0.4](https://github.com/mapbox/docbox/compare/v1.0.3...v1.0.4) (2017-01-25)



<a name="1.0.3"></a>
## [1.0.3](https://github.com/mapbox/docbox/compare/v1.0.2...v1.0.3) (2017-01-25)



### March 22, 2016

* Fixes non-unique IDs on section headers.
* You can now run `npm run build` multiple times without any problems.

### March 17, 2016

* Support for linking to specific languages. URLs were previously like
  `#the-section` but now are `?language=JavaScript#the-section` when a language
  is selected, so that you can link to both a specific section and a specific
  language.
* Changes how Docbox uses highlight.js - instead of including tons and tons
  of languages, we include only a few.

### March 14, 2016

* Support for toggling between 1 and 2 column mode
