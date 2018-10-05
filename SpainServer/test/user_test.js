// Switch to the test database for testing
require('babel-polyfill');
import bcrypt from 'bcryptjs';

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

/**
* Need tests for:
    two users with one mobile number
**/

describe('Users', () => {
    beforeEach((done) => {
        User.deleteMany({}, (err) => {
            done();
        });
    });

    let salt = bcrypt.genSaltSync(10);

    /**
     * Some mock test objects
     */
    const sampleCarer = {
        mobile: '12345678',
        password: bcrypt.hashSync('1234', salt),
        name: 'John Smith',
        userType: 'Carer'
    };

    const sampleDependent = {
        mobile: '12345678',
        password: bcrypt.hashSync('1234', salt),
        name: 'Joel Smith',
        userType: 'Dependent'
    };

    const sampleDependent2_hashed = {
        mobile: '12345678',
        password: bcrypt.hashSync('my-password', salt),
        name: 'Joel Smith',
        userType: 'Dependent'
    };

    const sampleDependent2_nohash = {
        mobile: '12345678',
        password: 'my-password',
        name: 'Joel Smith',
        userType: 'Dependent'
    };

    /*
    * Actual tests
    */

    it('should create a new carer', (done) => {
        chai.request(app)
            .post('/users/new')
            .type('form')
            .send(sampleDependent2_nohash)
            .end((err, res) => {
                res.should.have.status(201);
                done();
            });
    });

    it('should create a new dependent', (done) => {
        chai.request(app)
            .post('/users/new')
            .type('form')
            .send(sampleDependent2_nohash)
            .end((err, res) => {
                res.should.have.status(201);
                done();
            });
    });

    it('should get a carer by id', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            chai.request(app)
                .get(`/carers/${carer._id}`)
                .end((err,res) => {
                    res.body.name.should.equal(carer.name);
                    done();
                });
        });
    });

    it('should get a carer\'s name by mobile', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            chai.request(app)
                .get(`/carer/name/${carer.mobile}`)
                .end((err,res) => {
                    res.body.name.should.equal(carer.name);
                    done();
                });
        });
    });

    it('should get a carer\'s list of dependents', (done) => {
        Dependent.create(sampleCarer, (err, carer) => {
            chai.request(app)
                .get('/carers/' + carer._id + '/dependents')
                .end((err,res) => {
                    res.should.have.status(200);
                    done();
                });
        });
    });

    it('should update a carer', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            const newName = {name: 'Gary Lyon'};

            chai.request(app)
                .put(`/carers/${carer._id}`)
                .type('form')
                .send(newName)
                .end((err, res) => {
                    res.body.name.should.equal(newName.name);
                    done();
                });
        });
    });

    it('should delete a carer', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            chai.request(app)
                .delete(`/carers/${carer._id}`)
                .end((err, res) => {
                    res.body.name.should.equal(carer.name);
                    done();
                });
        });
    });

    it('should get a dependent by id', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {
            chai.request(app)
                .get(`/dependents/${dependent._id}`)
                .end((err,res) => {
                    res.body.name.should.equal(dependent.name);
                    done();
                });
        });
    });

    it('should get a dependent\'s name by mobile', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {
            chai.request(app)
                .get(`/dependent/name/${dependent.mobile}`)
                .end((err,res) => {
                    res.body.name.should.equal(dependent.name);
                    done();
                });
        });
    });

    it('should get a dependent\'s list of carers', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {
            chai.request(app)
                .get('/dependents/' + dependent._id + '/carers')
                .end((err,res) => {
                    res.should.have.status(200);
                    done();
                });
        });
    });

    it('should update a dependent', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {
            const newName = {name: 'Gary Lyon'};

            chai.request(app)
                .put(`/dependents/${dependent._id}`)
                .type('form')
                .send(newName)
                .end((err, res) => {
                    res.body.name.should.equal(newName.name);
                    done();
                });
        });
    });

    it('should delete a dependent', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {
            chai.request(app)
                .delete(`/dependents/${dependent._id}`)
                .end((err, res) => {
                    res.body.name.should.equal(dependent.name);
                    done();
                });
        });
    });

    it('should login a dependent with correct credentials', (done) => {
        // console.log("sampleDependent", sampleDependent_v3);
        Dependent.create(sampleDependent2_hashed, (err, dependent) => {
            chai.request(app)
                .post('/user/login')
                .type('form')
                .send(sampleDependent2_nohash)
                .end((err, res) => {
                    // console.log(res.message);
                    res.should.have.status(200);
                    done();
                });
        });
    });

    it('should deny login for a dependent with incorrect credentials', (done) => {
        Dependent.create(sampleDependent, (err, dependent) => {
            chai.request(app)
                .post('/user/login')
                .type('form')
                .send(sampleDependent2_nohash)
                .end((err, res) => {
                    // console.log(res.message);
                    res.should.have.status(401);
                    done();
                });
        });
    });

    it('should allow a carer to add a dependent as a friend', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            Dependent.create(sampleDependent, (err, dependent) => {
                chai.request(app)
                    .put('/carers/' + carer._id + '/addDependent')
                    .type('form')
                    .send({mobile: dependent.mobile})
                    .end((err, res) => {
                        res.should.have.status(200);
                        done();
                    });
            });
        });
    });

    it('should allow a dependent to accept a friend request from a carer', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            Dependent.create(sampleDependent, (err, dependent) => {
                chai.request(app)
                    .put('/carers/'+carer._id+'/addDependent')
                    .type('form')
                    .send({mobile: dependent.mobile})
                    .end((err, res) => {
                        // console.log(res.message);
                        res.should.have.status(200);
                        chai.request(app)
                            .put('/dependents/'+dependent._id+'/acceptCarer/'+carer._id)
                            .type('form')
                            .send({accept: 'accept'})
                            .end((err, res) => {
                                // console.log(res.message);
                                res.body.message.should.equal("Friend request accepted");
                                res.body.name.should.equal(carer.name);
                                res.should.have.status(200);
                                done();
                            });
                    });
            });
        });
    });

    it('should allow a dependent to decline a friend request from a carer', (done) => {
        Carer.create(sampleCarer, (err, carer) => {
            Dependent.create(sampleDependent, (err, dependent) => {
                chai.request(app)
                    .put('/carers/'+carer._id+'/addDependent')
                    .type('form')
                    .send({mobile: dependent.mobile})
                    .end((err, res) => {
                        // console.log(res.message);
                        res.should.have.status(200);
                        chai.request(app)
                            .put('/dependents/'+dependent._id+'/acceptCarer/'+carer._id)
                            .type('form')
                            .send({accept: 'decline'})
                            .end((err, res) => {
                                // console.log(res.message);
                                res.body.message.should.equal("Friend request declined");
                                res.body.name.should.equal(carer.name);
                                res.should.have.status(200);
                                done();
                            });
                    });
            });
        });
    });
});
