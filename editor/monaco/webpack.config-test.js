var MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

module.exports = {
  target: 'node', // webpack should compile node compatible code
  devtool: "inline-cheap-module-source-map",
  output: {
    globalObject: 'this',
    devtoolModuleFilenameTemplate: '[absolute-resource-path]',
    devtoolFallbackModuleFilenameTemplate: '[absolute-resource-path]?[hash]'
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
      use: ['file-loader']
    }]
  },
  resolve: {
    modules: ['node_modules'],
    extensions: [ '.tsx', '.ts', '.js' ]
  },
  mode: 'development',
  node: {
    fs: 'empty',
    global: true,
    crypto: 'empty',
    tls: 'empty',
    net: 'empty',
    process: true,
    module: false,
    clearImmediate: false,
    setImmediate: false
  },
  plugins: [
    new MonacoWebpackPlugin({ languages: [] })
  ]
}