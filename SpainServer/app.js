const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');

import { router } from "./routes";

const db = require('./models/db');
const app = express();

app.use(bodyParser.urlencoded({extended: true}));
app.use('/', router);

app.listen(process.env.PORT || 3000, () => {
    if (process.env.NODE_ENV !== 'test') {
        console.log("App listening on port 3000");
    }
});

module.exports = app;
