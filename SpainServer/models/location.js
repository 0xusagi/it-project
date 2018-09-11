import mongoose from 'mongoose';

export const locationSchema = mongoose.Schema({
    _locationId: mongoose.Schema.Types.ObjectId,
    name: String
});

export const Location = mongoose.model('location', locationSchema);