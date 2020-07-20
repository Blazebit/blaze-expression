<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="style.css">
    <script src="webjars/monaco-editor/0.20.0/min/vs/loader.js"></script>
</head>
<body>
    <script src="js/main.js"></script>
    <script>
        require.config({ paths: { 'vs': 'webjars/monaco-editor/0.20.0/min/vs' }});

        require(['vs/editor/editor.main'], function() {
            var request = new XMLHttpRequest();
            request.addEventListener('load', function(event) {
                if (request.status >= 200 && request.status < 300) {
                    var json = request.responseText;
                    var symbols = JSON.stringify({
                        post: {type: "Post", doc: "The current post"}
                    });
                    BlazeExpressionContributor.createEditor(
                        monaco,
                        document.getElementById('containerSingleLine'),
                        json.substring(0, json.length - 1) + ',"symbols":' + symbols + '}',
                        true
                    );
                    BlazeExpressionContributor.createEditor(
                        monaco,
                        document.getElementById('containerMultiLine'),
                        json.substring(0, json.length - 1) + ',"symbols":' + symbols + '}',
                        false
                    );
                } else {
                    console.warn(request.statusText, request.responseText);
                }
            });
            request.open("GET","${pageContext.servletContext.contextPath}/api/model");
            request.send();
        });
    </script>
    <div id="containerSingleLine" style="width:800px;height:1.2em;border:1px solid grey">
        <textarea style="display: none">SUBSTRING(post.writer.name, 1)</textarea>
    </div>
    <div id="containerMultiLine" style="width:800px;height:6em;border:1px solid grey">
        <textarea style="display: none">SUBSTRING(post.writer.name, 1)</textarea>
    </div>
</body>
</html>
