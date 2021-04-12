var integration = require('blaze-expression-monaco');
var monaco = require('monaco-editor');
if (typeof window === 'undefined') {

} else {
    window.BlazeExpressionContributor = integration;
    window.monaco = monaco;
}