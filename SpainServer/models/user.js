import mongoose from 'mongoose';
import { locationSchema } from "./location";

mongoose.set('useCreateIndex', true);

const options = { discriminatorKey: 'kind' };

const userSchema = new mongoose.Schema({
    _userId: mongoose.Schema.Types.ObjectId,
    mobile: {
        type: String,
        required: true,
        unique: true
    },
    name: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    firebaseToken: {
        type: String,
        required: true
    },
    avatarLink: String
}, {
    timestamps: true
}, options);

export const User = mongoose.model('user', userSchema);

const dependentSchema = new mongoose.Schema({
    pendingCarers: [Array],
    carers: [Array],
    locations: [String]
}, options);

const carerSchema = new mongoose.Schema({
    pendingDependents: [String],
    dependents: [String]
});

export const Carer = User.discriminator('Carer', carerSchema);
export const Dependent = User.discriminator('Dependent', dependentSchema);
