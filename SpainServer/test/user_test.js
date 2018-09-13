// Switch to the test database for testing
process.env.NODE_ENV = 'test';
require('babel-polyfill');

let mongoose = require('mongoose');
let chai = require('chai');
let chaiHttp = require('chai-http');

let app = require('../app');
import { User } from "../models/user";
let should = chai.should();

chai.use(chaiHttp);

describe('Users', () => {
    beforeEach((done) => {
        User.remove({}, (err) => {
            done();
        });
    });

    /**
     * Test POST /users/new
     */
    const sampleUser = {
        mobile: '12345678',
        password: '1234',
        name: 'John Smith',
        userType: 'Carer'
    };

    it('should create a new carer', (done) => {
        chai.request(app)
            .post('/users/new')
            .send(sampleUser)
            .end((err, res) => {
                res.should.have.status(201);
                done();
            })
    });
});
