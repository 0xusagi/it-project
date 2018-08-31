mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/spain-server', (err) => {
    if (err) {
        console.log(err);
    }
    console.log("Connected to spain-server :D");
});
