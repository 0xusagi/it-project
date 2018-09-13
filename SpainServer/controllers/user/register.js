import {Carer, Dependent} from "../../models/user";

/**
 * Create a new user on the supplied user type.
 * @param userType
 * @param data
 * @returns boolean | User
 */
const newUserFromType = (data) => {
    if (data.userType === 'Dependent') {
        return new Dependent({
            mobile: data.mobile,
            name: data.name,
            password: data.password
        });
    } else if (data.userType === 'Carer') {
        return new Carer({
            mobile: data.mobile,
            name: data.name,
            password: data.password
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

    if (newUser === false) {
        return res.status(400).json({message: "User Error (Wrong user type inputted)"});
    }

    const response = newUser.save((error, user) => {
        if (error) {
            return res.status(400).json(error);
        }
        return res.status(201).json(user);
    });

    return response;
};

export const registrationController = {
    new: newUser
};
