import {Carer, Dependent} from "../../models/user";
import {User} from "../../models/user";
// import {Carer, Dependent} from "../../models/user";
import bcrypt from 'bcryptjs';

const loginUser = (req, res, next) => {
    // console.log("req.body", req.body);
    const response = User.find({mobile: req.body.mobile})
        .then(users => {
            // console.log("users", users);
            if (users.length == 0) {
                return res.status(404).json({message: "User not found"});
            } else {
                return users[0];
            }
        })
        .then(user => {
            let hashed_password = user.password;
            if (bcrypt.compareSync(req.body.password, hashed_password)) {
                return res.status(200).json(user);
            } else {
                return res.status(401).json({message: "Invalid credentials"});
            }
        })
        .catch(error => {
            // handle error
            return res.status(500).json({message: "Internal server error"});
        });
    return response;
};

export const loginController = {
    login: loginUser
};
