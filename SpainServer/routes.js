import express from 'express';

const router = express.Router();
// POST /user/new

router.post('/user/new', userController.new);

// POST /user/login

router.post('/user/login', loginController.login);
