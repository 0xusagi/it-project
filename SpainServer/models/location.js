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
    displayName: {
        type: String,
        required: true
    },
    googleId: {
        type: String,
        required: true
    }
},{
    timestamps: true
});

export const Location = mongoose.model('location', locationSchema);
