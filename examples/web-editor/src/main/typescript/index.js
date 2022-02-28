if (typeof window !== 'undefined') {
    window.process = {
        env: {
            NODE_ENV: 'development'
        }
    }
}
var integration = require('blaze-expression-monaco');
var monaco = require('monaco-editor/esm/vs/editor/editor.api');
if (typeof window === 'undefined') {

} else {
    window.BlazeExpressionContributor = integration;
    window.monaco = monaco;
}