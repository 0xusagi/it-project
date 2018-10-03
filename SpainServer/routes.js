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

// adding and getting their dependents
router.put('/carers/:id/addDependent', carerIndex.addDependent);
router.get('/carers/:id/getDependents', carerIndex.getDependents);

// get a carer's name by mobile number
router.get('/carer/name/:mobile', carerIndex.getName);


/**
 * Dependents
 */
// basics
router.get('/dependents/:id', dependentIndex.get);
router.put('/dependents/:id', dependentIndex.put);
router.delete('/dependents/:id', dependentIndex.delete);

// adding and getting their carers
router.put('/dependents/:id/addCarer', dependentIndex.addCarer);

// get a dependent's name by mobile number
router.get('/dependent/name/:mobile', dependentIndex.getName);

router.post('/dependent/:id/addLocation', locationIndex.addToDependent);
/**
 * Locations
 */
 // get all locations
router.get('/locations', locationIndex.getAll);

// basics; create new, get, update, delete
router.post('/locations/new', locationIndex.new);
router.get('/locations/:id', locationIndex.get);
router.put('/locations/:id', locationIndex.put);
router.delete('/locations/:id', locationIndex.delete);

export { router };
