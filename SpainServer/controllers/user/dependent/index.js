import { Dependent } from "../../../models/user";
import { Carer } from "../../../models/user";

/**
 * Gets a dependent from the database from a supplied id parameter.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const getDependent = (req, res, next) => {
    const response = Dependent.findById(req.params.id, (err, dependent) => {
        if (err) {
            return res.status(400).send(err);
        }

        return res.status(200).json(dependent);
    });

    return response;
};

/**
 * Gets all carers (objects) of a carer from the database
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const getCarers = (req, res, next) => {
    const response = Dependent.findById(req.params.id).exec()
        .then((dependent) => {
            return Carer.find({'_id': { $in: dependent.carers }}, (err, carers) => {
                return res.status(200).json(carers);
            });
        }).catch((err) => {
            console.log('error: ', err);
            return res.status(400).send(err);
        });
    return response;
};

/**
 * Updates a dependent based on supplied params from the client.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const updateDependent = (req, res, next) => {
    let options = {new: true};
    const response = Dependent.findOneAndUpdate(req.params.id,
        req.body, options,
        (err, dependent) => {
            if (err) {
                return res.status(400).send(err);
            }
            return res.status(200).json(dependent);
        });

    return response;
};

/**
 * Deletes a carer from the database.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const deleteDependent = (req, res, next) => {
    const response = Dependent.findOneAndDelete(req.params.id, (err, dependent) => {
        if (err) {
            return res.status(400).send(err);
        }

        return res.status(200).json(dependent);
    });

    return response;
};

/**
 * Specifically adds a carer to a dependent's list of carers
 * based on supplied dependent id from the client.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const addCarerToDependent = (req, res, next) => {
    let carerMobile = req.body.carerMobile;
    let carerId = getCarerIdFromMobile(carerMobile);
    // console.log("carerId", carerId);
    // some sort of Asynchronous stuff going on here
    let options = {new: true};
    const response = Dependent.findOneAndUpdate(req.params.id,
        { $addToSet: { carers: carerId } }, options,
        (err, dependent) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).json(dependent);
    });

    return response;
};

function getCarerIdFromMobile(m) {
    let id;
    Carer.find({mobile: parseInt(m, 10)}, (err, carer) => {
        if (err) {
            return -1;
        }
        // console.log("carer[0].id;", carer[0].id);
        return carer[0].id;
    });
}

/**
 * Specifically gets a dependent's name given their mobile number
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const getDependentByMobile = (req, res, next) => {
    let userMobile = req.params.mobile;
    const response = Dependent.find({mobile: userMobile}, (err, dependent) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).send({name: dependent[0].name});
    });

    return response;
}

/**
 * Deals with pending carers and dependents based on a carer id and a dependent mobile
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const acceptFriendRequest = (req, res, next) => {
    const options = {
        new: true
    };
    const carerId = req.params.carerId;
    const depId = req.params.depId;
    const acceptOrDecline = req.body.accept;

    const response = Dependent.find({_id: depId}).exec()
        .then(function(dependents) {
            if (dependents.length === 0) {
                return res.status(400).send({
                    message: 'Dependent not found in database.'
                });
            }
            let current_dependent = dependents[0];
            // if this dependent has a request from this carer
            if (current_dependent.pendingCarers.includes(carerId)) {
                    // if this dependent already has this carer in their list of carers
                    if (current_dependent.carers.includes(carerId)) {
                        // return 400
                        return res.status(400).send({
                            message: 'These users are already friends'
                        });
                    } else {
                        return Carer.findById(carerId)
                            .then((current_carer) => {
                                // console.log("acceptOrDecline", acceptOrDecline);
                                let res_message = "Friend request declined";
                                // remove from pending lists
                                current_carer.pendingDependents = current_carer.pendingDependents.filter(id => id != current_dependent._id);
                                current_dependent.pendingCarers = current_dependent.pendingCarers.filter(id => id != current_carer._id);

                                // add to actual lists (if they wish to accept)
                                if (acceptOrDecline.toUpperCase() === 'ACCEPT') {
                                    current_dependent.carers.push(current_carer._id);
                                    current_carer.dependents.push(current_dependent._id);
                                    res_message = "Friend request accepted";
                                }

                                return current_dependent.save().then((dependent) => {
                                        return current_carer.save().then((carer) => {
                                            return res.status(200).send({
                                                message: res_message,
                                                name: current_carer.name
                                            });
                                        }).catch((err) => {
                                            console.log("error: ", err);
                                            return res.status(500).send({
                                                message: 'Internal server error'
                                            });
                                        });
                                    })
                                    .catch((err) => {
                                        console.log("error: ", err);
                                        return res.status(500).send({
                                            message: 'Internal server error'
                                        });
                                    });
                            })
                            .catch((err) => {
                                console.log("error: ", err);
                                return res.status(500).send({
                                    message: 'Internal server error'
                                });
                            });
                    }
                } else {
                    // return 400
                    return res.status(400).send({
                        message: 'A friend request between these users does not exist'
                    });
                }
        }).catch((err) => {
            console.log("error: ", err);
            return res.status(500).send({
                message: 'Internal server error'
            });
        });
    return response;
};

// Get all carers who are in the stage 'pending' for a certain dependent.
const getPendingCarers = (req, res) => {
    // Find by id, then get pending carers. find all who are in that range
    return Dependent.findById(req.params.id).exec()
        .then(dependent => {
            if (!dependent) {
                return res.status(400).send({
                    message: 'Dependent does not exist.'
                })
            }
            return dependent.pendingCarers;
        })
        .then(pendingCarerIds => Carer.find({_id: {$in: pendingCarerIds}}).exec())
        .then(carers => res.status(200).send(carers))
        .catch(err => {
            return res.status(500).send({
                message: 'Internal server error'
            });
        });
};

export const dependentIndex = {
    get: getDependent,
    getPending: getPendingCarers,
    put: updateDependent,
    delete: deleteDependent,
    acceptCarer: acceptFriendRequest,
    getName: getDependentByMobile,
    getCarers: getCarers
};
