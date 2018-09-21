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
router.post('/users/new', registrationController.new);
router.post('/user/login', loginController.login);

/**
 * Carers
 */
router.get('/carers/:id', carerIndex.get);
router.put('/carers/:id', carerIndex.put);
router.delete('/carers/:id', carerIndex.delete);

router.put('/carers/:id/addDependent', carerIndex.addDependent);
router.get('/carers/:id/getDependents', carerIndex.getDependents);

/**
 * Dependents
 */
router.get('/dependents/:id', dependentIndex.get);
router.put('/dependents/:id', dependentIndex.put);
router.delete('/dependents/:id', dependentIndex.delete);

router.put('/dependents/:id/addCarer', dependentIndex.addCarer);

/**
 * Locations
 */
router.get('/locations', locationIndex.getAll);
router.post('/locations/new', locationIndex.new);
router.get('/locations/:id');
router.put('/locations/:id');
router.delete('/locations/:id');

export { router };
