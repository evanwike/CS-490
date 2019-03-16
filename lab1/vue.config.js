const path = require('path');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
	outputDir: './dist',
	publicPath: '/',
	configureWebpack: {
		entry: {
			bootstrap: path.resolve(__dirname, 'node_modules/bootswatch/dist/lux/bootstrap.min.css'),
			jquery: path.resolve(__dirname, 'node_modules/bootstrap/dist/js/bootstrap.min.js'),
			app: './src/main.js'
		},
		plugins: [
			/**
			 * Since assets aren't being kept in /public, make sure they still
			 * get copied over to /dist on build
			 */
			new CopyWebpackPlugin([
				{
					from: path.resolve(__dirname, 'src', 'assets'),
					to: path.resolve(__dirname, 'dist', 'assets'),
					toType: 'dir',
					ignore: [
						'.DS_Store'
					]
				}
			])
		]
	},
	/**
	 * Override the HTML template generator
	 */
	chainWebpack: config =>
		config.plugin('html').tap(
			([arg]) => [{
				...arg,
				template: path.resolve(__dirname, 'src', 'index.html')
			}]
		)
}
