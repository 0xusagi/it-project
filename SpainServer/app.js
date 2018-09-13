const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');

// Tells node what the environment is (So we can use a 'clean' test database when using mocha for tests)
process.env.NODE_ENV = 'dev';

const db = require('./models/db');

const app = express();

app.listen(process.env.PORT || 3000, () => {
    console.log("App listening on port 3000");
});