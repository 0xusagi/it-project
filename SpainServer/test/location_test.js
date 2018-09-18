// Switch to the test database for testing
process.env.NODE_ENV = 'test';
require('babel-polyfill');

let mongoose = require('mongoose');
let chai = require('chai');
let chaiHttp = require('chai-http');
let app = require('../app');

let should = chai.should();
let expect = chai.expect;

chai.use(chaiHttp);

import { Location } from "../models/location";