const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const db = require('./models/db');
import { Location } from "./models/location";
import { Dependent } from "./models/user";
import { User } from "./models/user";
import { Carer } from "./models/user";

const app = express();

app.listen(process.env.PORT || 3000, () => {
    console.log("App listening on port 3000");
});