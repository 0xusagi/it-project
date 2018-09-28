import {Carer, Dependent} from "../../models/user";
import {User} from "../../models/user";
import bcrypt from 'bcryptjs';

/**
 * Create a new user on the supplied user type.
 * @param userType
 * @param data
 * @returns boolean | User
 */
const newUserFromType = (data) => {
    // console.log(data);
    // salt and hash password
    let salt = bcrypt.genSaltSync(10);
	let hash = bcrypt.hashSync(data.password, salt);
    // console.log("password hashed: ", hash);

    if (data.userType === 'Dependent') {
        return new Dependent({
            mobile: data.mobile,
            name: data.name,
            password: hash
        });
    } else if (data.userType === 'Carer') {
        return new Carer({
            mobile: data.mobile,
            name: data.name,
            password: hash
        });
    } else {
        return false;
    }
};

/**
 * Create a new user in the mongo database from client-supplied parameters and
 * return a response if successful.
 * @param req
 * @param res
 * @param next
 */
const newUser = (req, res, next) => {
    const newUser = newUserFromType(req.body);

    const response = newUser.save((error, user) => {
        if (error) {
            if (error.code === 11000) {
                return res.status(400).json({message: "Mobile number already registered"});
            } else {
                return res.status(400).json(error);
            }
        }
        return res.status(201).json(user);
    });

    return response;
};

export const registrationController = {
    new: newUser
};
