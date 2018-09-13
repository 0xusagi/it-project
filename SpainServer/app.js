const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');

// Tells node what the environment is (So we can use a 'clean' test database when using mocha for tests)

import { router } from "./routes";

const db = require('./models/db');
const app = express();

app.use(bodyParser.json());
app.use('/', router);

app.listen(process.env.PORT || 3000, () => {
    console.log("App listening on port 3000");
});

module.exports = app;