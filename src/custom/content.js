var fs = require('fs');

/**
 * This file exports the content of your website, as a bunch of concatenated
 * Markdown files. By doing this explicitly, you can control the order
 * of content without any level of abstraction.
 *
 * Using the brfs module, fs.readFileSync calls in this file are translated
 * into strings of those files' content before the file is delivered to a
 * browser: the content is read ahead-of-time and included in bundle.js.
 */
module.exports =
  '# Getting started\n' +
  fs.readFileSync('./content/2.0.0/getting_started.md', 'utf8') + '\n' +
  '# Navigation\n' +
  fs.readFileSync('./content/2.0.0/navigation.md', 'utf8') + '\n' +
  '# Geocoder\n' +
  fs.readFileSync('./content/2.0.0/geocoder.md', 'utf8') + '\n' +
  '# Telemetry\n' +
  fs.readFileSync('./content/2.0.0/telemetry.md', 'utf8') + '\n';
