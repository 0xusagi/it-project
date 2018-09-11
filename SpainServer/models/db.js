import mongoose from 'mongoose';

mongoose.connect('mongodb://localhost/spain-server', (err) => {
    if (!err) {
        console.log("Connected to spain-server :D");
    } else {
        console.log(err);
    }
});