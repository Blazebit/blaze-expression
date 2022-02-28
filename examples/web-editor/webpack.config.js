var MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

module.exports = {
  entry: './src/main/typescript/index.js',
  output: {
    filename: 'js/main.js',
  },
  module: {
    rules: [{
      test: /\.tsx?$/,
      use: 'ts-loader',
      exclude: /node_modules/
    },{
      test: /\.css$/,
      use: ['style-loader', 'css-loader']
    }, {
      test: /\.ttf$/,
      type: 'asset/resource'
    }]
  },
  resolve: {
    modules: ['node_modules'],
    extensions: [ '.tsx', '.ts', '.js' ],
    fallback: {
      "util": require.resolve("util/"),
      "assert": require.resolve("assert/")
    }
  },
  mode: 'none',
  plugins: [
    new MonacoWebpackPlugin({
      languages: [],
      features: [
        // 'accessibilityHelp',
        // 'anchorSelect',
        'bracketMatching',
        // 'caretOperations',
        // 'clipboard',
        // 'codeAction',
        // 'codelens',
        // 'colorPicker',
        // 'comment',
        // 'contextmenu',
        // 'coreCommands',
        // 'cursorUndo',
        // 'dnd',
        // 'documentSymbols',
        // 'find',
        // 'folding',
        // 'fontZoom',
        // 'format',
        // 'gotoError',
        // 'gotoLine',
        // 'gotoSymbol',
        'hover',
        'iPadShowKeyboard',
        // 'inPlaceReplace',
        // 'indentation',
        'inlayHints',
        'inlineCompletions',
        // 'inspectTokens',
        // 'lineSelection',
        // 'linesOperations',
        // 'linkedEditing',
        // 'links',
        // 'multicursor',
        'parameterHints',
        // 'quickCommand',
        // 'quickHelp',
        // 'quickOutline',
        // 'referenceSearch',
        // 'rename',
        // 'smartSelect',
        // 'snippet',
        'suggest',
        // 'toggleHighContrast',
        // 'toggleTabFocusMode',
        // 'tokenization',
        // 'unicodeHighlighter',
        // 'unusualLineTerminators',
        // 'viewportSemanticTokens',
        'wordHighlighter',
        // 'wordOperations',
        // 'wordPartOperations',
      ]
    })
  ]
}