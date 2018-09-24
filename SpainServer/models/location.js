import mongoose from 'mongoose';

mongoose.set('useCreateIndex', true);

export const locationSchema = mongoose.Schema({
    _locationId: mongoose.Schema.Types.ObjectId,
    lat: {
        type: Number,
        required: true
    },
    long: {
        type: Number,
        required: true
    },
    addressLine1: {
        type: String
    },
    addressLine2: {
        type: String
    },
    postcode: {
        type: Number
    },
    state: {
        type: String
    },
    displayName: {
        type: String,
        required: true
    },
    description: {
        type: String
    },
    imageUrl: {
        type: String
    },
    popularity: {
        type: Number
    }
},{
    timestamps: true
});

export const Location = mongoose.model('location', locationSchema);
