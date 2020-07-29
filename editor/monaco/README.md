Blaze-Expression Monaco Editor
==========
An integration for Blaze-Expression with the Monaco editor.

Installation
===========

```
npm install blaze-expression-monaco
```

Usage
==============

You can parse the JSON representation of a domain model with symbols and create a monaco editor.

```typescript
import * as expression from 'blaze-expression-monaco';
expression.BlazeExpressionContributor.createEditor(
    monaco,
    document.getElementById('editorId'),
    modelAndSymbolJsonPayload,
    true // Single-line mode
);
```

In a web application this will roughly look like the following

```html
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="style.css">
    <script src="webjars/monaco-editor/0.20.0/min/vs/loader.js"></script>
</head>
<body>
    <!-- The webpacked JS file -->
    <script src="js/main.js"></script>
    <script>
        require.config({ paths: { 'vs': 'webjars/monaco-editor/0.20.0/min/vs' }});

        // Await loading of the editor
        require(['vs/editor/editor.main'], function() {
            var request = new XMLHttpRequest();
            // Load the domain model JSON payload
            request.open("GET","/api/model");
            request.addEventListener('load', function(event) {
                if (request.status >= 200 && request.status < 300) {
                    var json = request.responseText;
                    var symbols = JSON.stringify({
                        post: {type: "Post", doc: "The current post"}
                    });
                    // Concatenate the symbols to the domain model json payload
                    var modelAndSymbolJsonPayload = json.substring(0, json.length - 1) + ',"symbols":' + symbols + '}';
                    // Create the monaco editor for the div with the id containerSingleLine
                    BlazeExpressionContributor.createEditor(
                        monaco,
                        document.getElementById('containerSingleLine'),
                        modelAndSymbolJsonPayload,
                        true // Single-line mode
                    );
                } else {
                    console.warn(request.statusText, request.responseText);
                }
            });
            request.send();
        });
    </script>
    <div id="containerSingleLine" style="width:800px;height:1.2em;border:1px solid grey">
        <textarea style="display: none">SUBSTRING(post.writer.name, 1)</textarea>
    </div>
</body>
</html>
````

Take a look at the following example project: https://github.com/Blazebit/blaze-expression/examples/web-editor

Licensing
=========

This distribution, as a whole, is licensed under the terms of the Apache
License, Version 2.0 (see LICENSE.txt).

References
==========

Project Site:              https://expression.blazebit.com (coming at some point)
