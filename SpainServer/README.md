# Spain Server README

## Running the Server
firstly, run `yarn install` or `npm install` to install dependencies
ensure your local instance of mongo is running (`mongod`)
run the server using `yarn nodemon-babel` or `npm run nodemon-babel`

## Project Structure
'./controllers' - our app routes and their logic
'./models' - represents data, implements business logic, and handles storage
'./public' - contains all static files like images, styles and javascript
'./views' - provides templates which are rendered and served by our routes
'./tests' - self explanatory
'./middlewares' - express js middelwares that process requests before handing them to the routes (controllers)
