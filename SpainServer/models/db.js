import mongoose from 'mongoose';
import config from '../config/config';

mongoose.connect('mongodb://localhost/spain-server',  { useNewUrlParser: true }, (err) => {
    if (!err) {
        console.log("Connected to db: spain-server");
    } else {
        console.log(err);
    }
});

export function submitUser(user) {
    user.save(function(error){
        if(error) console.log("error: '"+error+"' when saving "+name+" to db");
    });
}
