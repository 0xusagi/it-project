import mongoose from 'mongoose';
import config from '../config/config';

mongoose.set('useFindAndModify', false);

mongoose.connect(config[process.env.NODE_ENV].DBHost, { useNewUrlParser: true }, (err) => {
    if (!err) {
        console.log("Connected to " + config[process.env.NODE_ENV].DBHost);
    } else {
        console.log(err);
    }
});
