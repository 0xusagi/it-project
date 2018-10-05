import * as admin from 'firebase-admin';
import { Carer } from "../../models/user";
import { Dependent } from "../../models/user";

var serviceAccount = require('../../config/spain-a0ca5-firebase-adminsdk-feod7-242ceecc87.json');

// hit post '/dependent/:id/getHelp' ->

// This registration token comes from the client FCM SDKs.
var registrationToken = 'fTJTi55ObN4:APA91bHOwqJMTHGam2hA2A0Gt4iBlvBb6JDqU8TfK2Ixg6a3hGvnT9IvGLqjUDec0q8XHI4RlsoG3_5ZZ9j0n-TThmKUvAZvCWTzCOQCztI4R907bL280Cs4tjOSMhFZ0OdeMgsjFNeO';

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://spain-a0ca5.firebaseio.com/'
});

var exampleChatMessage = {
    notification: {
        title: 'Your Dependent Needs Help',
        body: 'Your dependent urgently needs help. Tap for more info'
    },
    data: {
        time: '00:00 1 Jan 2018',
        type: 'chat',
        message: 'Hey, I have a message for you',
        _id: 'my user id (database)',
        name: 'My Name'
    },
    // structure specific to android notifications
    android: {
        ttl: 3600 * 1000, // 1 hour in milliseconds
        priority: 'normal',
        notification: {
          title: 'Your Dependent Needs Help',
          body: 'Your dependent urgently needs help. Tap for more info',
          icon: 'stock_ticker_update',
          color: '#f45342'
        }
    },
    token: registrationToken
};

const sendNotification = (req, res, next) => {
    // first, find the dependent with specified id, then send message to all their carers
    const response = Dependent.findById(req.params.id).exec()
        .then((dependent) => {
            console.log('Found dependent: ', dependent);
            return Carer.find({'_id': { $in: dependent.carers }}, (err, carers) => {
                console.log('Found carers: ', carers);
                if (carers.length === 0) {
                    return res.status(400).json({
                        message: "Sorry, this dependent has no carers."
                    });
                }
                // now we have their carers
                carers.forEach((carer) => {
                    // Send a message to the device corresponding to the provided
                    // registration token.
                    var chatMessage = {
                        notification: {
                            title: 'Your Dependent Needs Help',
                            body: 'Your dependent urgently needs help. Tap for more info'
                        },
                        data: {
                            time: '00:00 1 Jan 2018',
                            type: 'help',
                            message: req.body.message,
                            _id: dependent._id,
                            name: dependent.name
                        },
                        // structure specific to android notifications
                        android: {
                            ttl: 3600 * 1000, // 1 hour in milliseconds
                            priority: 'normal',
                            notification: {
                              title: 'Your Dependent Needs Help',
                              body: 'Your dependent urgently needs help. Tap for more info',
                              icon: 'stock_ticker_update',
                              color: '#f45342'
                            }
                        },
                        token: carer.firebaseToken
                    };
                    admin.messaging().send(chatMessage)
                      .then((response) => {
                        // Response is a message ID string.
                        console.log('Successfully sent a message: ', response);
                        return res.status(200).send({
                            response: response,
                            depId: dependent._id,
                            message: "Sent message to carer",
                            carerId: carer._id
                        });
                      })
                      .catch((err) => {
                        console.log('Error sending message(s)', error);
                        return res.status(400).send({
                            error: err,
                            message: "Error sending message(s)",
                            carerId: carer._id
                        });
                      });
                })
                return res.status(200).json(carers);
            });
        }).catch((err) => {
            console.log('error: ', err);
            return res.status(400).send(err);
        });
    return response;
}

export const notificationController = {
    getHelp: sendNotification
};
