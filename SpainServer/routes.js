import express from 'express';
import {registrationController} from "./controllers/user/register";
import {loginController} from "./controllers/user/login";
import {carerIndex} from "./controllers/user/carer";
import {dependentIndex} from "./controllers/user/dependent";
import {locationIndex} from "./controllers/location/index";

const router = express.Router();

/**
 * Users
 */
 // create new, login
router.post('/users/new', registrationController.new);
router.post('/user/login', loginController.login);

/**
 * Carers
 */
 // basics
router.get('/carers/:id', carerIndex.get);
router.put('/carers/:id', carerIndex.put);
router.delete('/carers/:id', carerIndex.delete);
// adding and retrieving dependents
router.put('/carers/:id/addDependent', carerIndex.sendFriendRequest);
router.get('/carers/:id/dependents', carerIndex.getDependents);
// get a carer's name by mobile number
router.get('/carer/name/:mobile', carerIndex.getName);


/**
 * Dependents
 */
// basics
router.get('/dependents/:id', dependentIndex.get);
router.put('/dependents/:id', dependentIndex.put);
router.delete('/dependents/:id', dependentIndex.delete);
// accepting and retrieving carers
router.put('/dependents/:depId/acceptCarer/:carerId', dependentIndex.acceptCarer);
router.get('/dependents/:id/carers', dependentIndex.getCarers);

// get all pending carers for one dependent
router.get('/dependent/:id/carers/pending', dependentIndex.getPending);

// get a dependent's name by mobile number
router.get('/dependent/name/:mobile', dependentIndex.getName);

// Add a location to a dependent
router.post('/dependent/:id/addLocation', locationIndex.addToDependent);

// Get all locations for a dependent
router.get('/dependent/:id/locations', locationIndex.getLocationsForDependents);

/**
 * Locations
 */
// basics; create new, get, update, delete
router.get('/locations/:id', locationIndex.get);
router.put('/locations/:id', locationIndex.put);
router.delete('/locations/:id', locationIndex.delete);

export { router };
