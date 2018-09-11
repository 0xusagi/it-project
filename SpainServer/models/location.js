import mongoose from 'mongoose';

const locationSchema = mongoose.Schema({
    _locationId: Schema.Types.ObjectId
});

export const Location = mongoose.model('location', locationSchema);