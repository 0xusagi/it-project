const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const index = require('./routes/index');
import { router } from './routes/index';
import { createUser } from "./controllers/users"

createUser('0457 813 001', 'Oliver Murray', 'mypassword');

export var app = express();
app.use('/', index);

app.listen(process.env.PORT || 3000, () => {
    console.log("App listening on port 3000");
});
