import { Dependent } from "../models/user";
import { User } from "../models/user";
import { Carer } from "../models/user";
import { submitUser } from '../models/db';

export function createUser(mobile, name, password) {
    var newUser = new User();
    newUser.mobile = mobile;
    newUser.name = name;
    newUser.password = password;
    submitUser(newUser);
};
