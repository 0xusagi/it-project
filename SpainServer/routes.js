import express from 'express';
import {registrationController} from "./controllers/user/register";

const router = express.Router();
// POST /user/new

router.post('/users/new', registrationController.new);

// POST /user/login

//router.post('/user/login', loginController.login);

export { router };