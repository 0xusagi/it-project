import mongoose from 'mongoose';
import config from '../config/config';

mongoose.connect(config[process.env.NODE_ENV].DBHost, (err) => {
    if (!err) {
        console.log("Connected to spain-server :D");
    } else {
        console.log(err);
    }
});