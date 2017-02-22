## Topics

Topics contain information that applies to web services in general (not specific to APIs).

## API endpoints

Each individual API should have its own markdown file in the /content directory. Use snake case for filenames.

Each API file should have:

- H2 naming the API (i.e. "Wobbles")
  - description of the API
  - H3 naming the object or resource that is retrieved/created/deleted by the API
    - description of the method
    - list of parameters
    - example requests and responses

Make sure the API is also included in the nav by adding it to `content.js`.

### Description

A one- or two-sentence description explaining what the API does (not how to use it).

### Object

Each API should have a description of the primary resources returned and manipulated
using the API.

- H3 naming the object (i.e. "The wobble object")
- One sentence explaining what the object is.
- Two-column table to describe the object:
  - property name / type / required
  - property description
- If the object is severely nested, use a nested list instead of a table

### Methods

List all methods for interacting with the API.

Each method:

- H3 naming the method (i.e. "Retrieve a font")
- Endpoint (h4?)
  - do not include base URL in endpoint
  - endpoints should use [three backtick markdown format with syntax highlighting](https://help.github.com/articles/github-flavored-markdown/#syntax-highlighting) with `url` as the language
- A description of what the method does. (NOT how to use it.)
- (how do we define scopes?)
- If necessary, a description of accepted values/filetypes and limits/restrictions
- Two-column table to describe parameters
  - parameter name
  - parameter description and accepted values

### Examples

Each method should have H4 headers for examples:

- Example request
- Example request body (if applicable)
- Example response

There should be four examples under each header, one for each library. Use
[three backtick markdown format with syntax highlighting](https://help.github.com/articles/github-flavored-markdown/#syntax-highlighting):

## Adding a new SDK/language to examples pane

To add a new SDK/language to the right side examples pane:

- add the new language to the list of `languageOptions` in `/src/components/app.js`
  to add it to the language toggle
- add to the list of SDKs in `/content/introduction.md` with a link to the SDK documentation
- add highlighting for the new language in `/src/highlight.js` by importing the
  language and registering the language:

```
import java from 'highlight.js/lib/languages/java';

...

hljs.registerLanguage('java', java);
```

- add a test for the new SDK in `/test/content.js`:

```
it('has java example', () => {
  expect(select(chunk, 'code[lang=java]').length).toBeGreaterThan(0);
})
```

- add a syntax-highlighted example to each method in each API -- if the new SDK
  does not have methods for a particular API, include a syntax-highlighted
  comment in the comment structure of the langauge saying that the API cannot
  be accessed with this SDK.

## Style conventions

- Always JSON, never `JSON` or json.
- Do **not** include access tokens in example URLs.
- h2 and h3 will be included in side nav
- code blocks/h4/blockquotes will be pushed to the right

## Lingo

- the parts of a JSON object are called **properties**
- querystring parameters are called **parameters**

## Formatting JSON

We need to show JSON examples, but we want to make the documentation readable
on a wide range of monitors: so it needs to be somewhat narrow and compact.
Usually stringifying JSON with indentation of 2 spaces does the trick:
if that isn't enough, use [json-pretty-compact-cli](https://github.com/tmcw/json-pretty-compact-cli)
or another more tasteful formatter.
