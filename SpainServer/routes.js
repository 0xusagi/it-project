import express from 'express';
import {registrationController} from "./controllers/user/register";
import {carerIndex} from "./controllers/user/carer";
import {dependentIndex} from "./controllers/user/dependent";

const router = express.Router();

/**
 * Users
 */

// POST /user/new

router.post('/users/new', registrationController.new);

// POST /user/login

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

export { router };