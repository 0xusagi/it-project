const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const db = require('./model/db');

const routes = require('./routes');

const app = express();

app.use('/', routes);
app.use(bodyParser.json());