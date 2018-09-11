import mongoose from 'mongoose';
import { Location } from "./location";

const options = { discriminatorKey: 'kind' };

const userSchema = new mongoose.Schema({
    _userId: Schema.Types.ObjectId,
    mobile: {
        type: String,
        required: true
    },
    name: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    avatarLink: String
}, {
    timestamps: true
}, options);

export const User = mongoose.model('user', userSchema);

const dependentSchema = new mongoose.Schema({
    carers: Array,
    homeLocation: Location,
    destinations: Array
}, options);

const carerSchema = new mongoose.Schema({
    dependents: Array
});

export const Carer = User.discriminator('Carer', carerSchema);
export const Dependent = User.discriminator('Dependent', dependentSchema);
