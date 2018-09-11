const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const db = require('./models/db');

const routes = require('./routes');

const app = express();

app.use('view engine', 'ejs');
//app.use(bodyParser.json());
app.use('/', routes);

console.log("Oli: start the mongo shell and run 'use spain-server'");
console.log("Then run  db.placeholder.insert({name: \"Hello World});");
console.log("The database should work now");

app.listen(process.env.PORT || 3000, () => {
    console.log("App listening on port 3000");
});