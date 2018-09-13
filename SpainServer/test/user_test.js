// Switch to the test database for testing
process.env.NODE_ENV = 'test';
require('babel-polyfill');

let mongoose = require('mongoose');
let chai = require('chai');
let chaiHttp = require('chai-http');
let app = require('../app');

import { User } from "../models/user";
import { Carer } from "../models/user";
import { Dependent } from "../models/user";

let should = chai.should();
let expect = chai.expect;

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
    const sampleCarer = {
        mobile: '12345678',
        password: '1234',
        name: 'John Smith',
        userType: 'Carer'
    };

    const sampleDependent = {
        mobile: '12345678',
        password: '1234',
        name: 'Joel Smith',
        userType: 'Dependent'
    };

    it('should create a new carer', (done) => {
        chai.request(app)
            .post('/users/new')
            .send(sampleCarer)
            .end((err, res) => {
                res.should.have.status(201);
                done();
            })
    });

    it('should create a new dependent', (done) => {
        chai.request(app)
            .post('/users/new')
            .send(sampleDependent)
            .end((err, res) => {
                res.should.have.status(201);
                done();
            })
    });

    it('should get a carer', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            chai.request(app)
                .get(`/carers/${carer._id}`)
                .end((err,res) => {
                    res.body.name.should.equal(carer.name);
                    done();
                });
        })
    });

    it('should update a carer', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            const newName = {name: 'Gary Lyon'};

            chai.request(app)
                .put(`/carers/${carer._id}`)
                .send(newName)
                .end((err, res) => {
                    res.body.name.should.equal(newName.name);
                    done();
                })
        })
    });

    it('should delete a carer', (done) => {
        Carer.create(sampleCarer, (err, carer) => {

            chai.request(app)
                .delete(`/carers/${carer._id}`)
                .end((err, res) => {
                    res.body.name.should.equal(carer.name);
                    done();
                })
        })
    })


    it('should get a dependent', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {
            chai.request(app)
                .get(`/dependents/${dependent._id}`)
                .end((err,res) => {
                    res.body.name.should.equal(dependent.name);
                    done();
                });
        })
    });

    it('should update a dependent', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {
            const newName = {name: 'Gary Lyon'};

            chai.request(app)
                .put(`/dependents/${dependent._id}`)
                .send(newName)
                .end((err, res) => {
                    res.body.name.should.equal(newName.name);
                    done();
                })
        })
    });

    it('should delete a dependent', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {

            chai.request(app)
                .delete(`/dependents/${dependent._id}`)
                .end((err, res) => {
                    res.body.name.should.equal(dependent.name);
                    done();
                })
        })
    })
});
