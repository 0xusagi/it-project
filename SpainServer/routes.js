import express from 'express';
import {registrationController} from "./controllers/user/register";
import {carerIndex} from "./controllers/user/carer";
import {dependentIndex} from "./controllers/user/dependent";

const router = express.Router();

/**
 * Users
 */

router.post('/users/new', registrationController.new);

//router.post('/user/login', loginController.login);

/**
 * Carers
 */

router.get('/carers/:id', carerIndex.get);
router.put('/carers/:id', carerIndex.put);
router.delete('/carers/:id', carerIndex.delete);

/**
 * Dependents
 */
router.get('/dependents/:id', dependentIndex.get);
router.put('/dependents/:id', dependentIndex.put);
router.delete('/dependents/:id', dependentIndex.delete);

/**
 * Locations
 */
router.get('/locations');
router.post('/locations/new');
router.get('/locations/:id');
router.put('/locations/:id');
router.delete('/locations/:id');

export { router };