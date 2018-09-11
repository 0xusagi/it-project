const express = require('express');
export var router = express.Router();
var path = require("path");
import { createUser } from '../controllers/users';

router.get('/', function(req, res, next) {
    res.sendFile(path.join(__dirname, '../views', 'index.html'));
});

router.get('/addUser', function(req, res, next) {
    res.sendFile(path.join(__dirname, '../views', 'addUser.html'));
});

router.post('/user/add', function(req, res, next) {
    // do some stuff
});

module.exports = router;
