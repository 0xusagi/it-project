import mongoose from 'mongoose';
import config from '../config/config';

mongoose.set('useFindAndModify', false);
mongoose.set('useCreateIndex', true);

mongoose.connect(config[process.env.NODE_ENV].DBHost, { useNewUrlParser: true }, (err) => {
    if (!err && process.env.NODE_ENV !== 'test') {
        console.log("Connected to " + config[process.env.NODE_ENV].DBHost);
    } else if (err) {
        console.log(err);
    }
});

export function submitUser(user) {
    user.save(function(error){
        if(error) console.log("error: '"+error+"' when saving "+name+" to db");
    });
}
