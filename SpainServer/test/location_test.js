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

describe('Locations', () => {
    beforeEach((done) => {
        Location.deleteMany({}, (err) => {
            done();
        });
    });

    /**
     * Test POST /locations/new
     */
    const sampleLocation = {
        lat: 37.8138,
        long: 144.9641,
        displayName: "Brunetti Cafe",
        addressLine1: "250 Flinders Ln",
        addressLine2: "Melbourne, VIC",
        postcode: "3000",
        state: "VIC",
        description: "Italian cafe known for sweets, serving pizza and pasta in a modern space with an outdoor area.",
        imageUrl: "https://cdn.concreteplayground.com/content/uploads/2015/03/Vertue-Coffee-Drink-Carlton-Melbourne-01.jpeg",
        popularity: 5
    };

    it('should create a new location', (done) => {
        chai.request(app)
            .post('/locations/new')
            .type('form')
            .send(sampleLocation)
            .end((err, res) => {
                res.should.have.status(201);
                done();
            })
    });

    it('should get a location', (done) => {
        Location.create(sampleLocation, (err, location) => {
            chai.request(app)
                .get(`/locations/${location._id}`)
                .end((err,res) => {
                    res.body.displayName.should.equal(location.displayName);
                    done();
                });
        });
    });

    it('should update a location', (done) => {
        Location.create(sampleLocation, (err, location) => {
            const newDisplayName = {displayName: "Brunetti's"};

            chai.request(app)
                .put(`/locations/${location._id}`)
                .type('form')
                .send(newDisplayName)
                .end((err, res) => {
                    res.body.displayName.should.equal(newDisplayName.displayName);
                    done();
                })
        })
    });

    it('should delete a location', (done) => {
        Location.create(sampleLocation, (err, location) => {
            chai.request(app)
                .delete(`/locations/${location._id}`)
                .end((err, res) => {
                    res.body.displayName.should.equal(location.displayName);
                    done();
                })
        })
    })
});
