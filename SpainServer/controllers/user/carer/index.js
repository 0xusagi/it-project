import {
    Carer
} from "../../../models/user";
import {
    Dependent
} from "../../../models/user";

/**
 * Gets a carer from the database from a supplied id parameter.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const getCarer = (req, res, next) => {
    const response = Carer.findById(req.params.id).exec()
        .then((carer) => {
            return res.status(200).json(carer);
        }).catch((err) => {
            console.log('error: ', err);
            return res.status(400).send(err);
        });
    return response;
};

/**
 * Gets all dependents (objects) of a carer from the database
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const getDependents = (req, res, next) => {
    const response = Carer.findById(req.params.id).exec()
        .then((carer) => {
            return Dependent.find({'_id': { $in: carer.dependents }}, (err, dependents) => {
                return res.status(200).json(dependents);
            });
        }).catch((err) => {
            console.log('error: ', err);
            return res.status(400).send(err);
        });
    return response;
};

/**
 * Updates a carer based on supplied params from the client.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const updateCarer = (req, res, next) => {
    let options = {
        new: true
    };
    const response = Carer.findOneAndUpdate(req.params.id,
        req.body, options,
        (err, carer) => {
            if (err) {
                return res.status(400).send(err);
            }
            return res.status(200).json(carer);
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
const deleteCarer = (req, res, next) => {
    const response = Carer.findOneAndDelete(req.params.id, (err, carer) => {
        if (err) {
            return res.status(400).send(err);
        }

        return res.status(200).json(carer);
    });

    return response;
};

/**
 *
 * @param mobile
 * @param carerId
 * @returns {*}
 */
const isAlreadyAddedOrPending = (mobile, carerId) => {
    return new Promise((resolve, reject) => {
        Dependent.find({
            $or: [{
                    carers: carerId
                },
                {
                    pendingCarers: carerId
                }
            ]
        }).then((dependents) => {
            // console.log("dependents", dependents);
            if (dependents.length > 0) {
                resolve(true);
            } else {
                resolve(false);
            }
        }).catch((err) => {
            // console.log("error checking whether dependent/carer already added; ", err);
            resolve(true);
        })
    });
};

/**
 *
 * @param mobile
 * @param carerId
 * @returns {*}
 */
const isAlreadyPending = (mobile, carerId) => {
    return new Promise((resolve, reject) => {
        Dependent.find({
                pendingCarers: carerId
            }).then((dependents) => {
            if (dependents.length > 0) {
                resolve(true);
            } else {
                resolve(false);
            }
        }).catch((err) => {
            // console.log("error checking whether dependent/carer already added; ", err);
            resolve(true);
        })
    });
};

/**
 *
 * @param mobile
 * @param carerId
 * @returns {*}
 */
const isAlreadyAdded = (mobile, carerId) => {
    return new Promise((resolve, reject) => {
        Dependent.find({
                carers: carerId
            }).then((dependents) => {
            if (dependents.length > 0) {
                resolve(true);
            } else {
                resolve(false);
            }
        }).catch((err) => {
            // console.log("error checking whether dependent/carer already added; ", err);
            resolve(true);
        })
    });
};


/**
 * Deals with pending carers and dependents based on a carer id and a dependent mobile
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const sendFriendRequest = (req, res, next) => {
    let options = {
        new: true
    };
    let carerId = req.params.id;
    // console.log("carerId", carerId);
    let mobile = req.body.mobile;
    let response = Dependent.find({
            mobile: mobile
        }).exec()
        .then(function(dependents) {
        // use doc
        // console.log("dependents[0]", dependents[0]);
        if (dependents.length === 0) {
            return res.status(400).send({
                message: 'Dependent not found in database.'
            });
        }
        // console.log("carerId", carerId);

        // Firstly check whether the carer exists
        Carer.findById(carerId).exec()
            .then((carer) => {
            // do nothing
            })
            .catch((err) => {
                return res.status(400).send({
                    message: 'Carer not found in database.'
                });
                console.log("error finding carer:", err);
            });

        // Then check if the mobile has already been added by this carer before.
        isAlreadyAddedOrPending(mobile, carerId).then((check) => {
                // if so, return 400 already sent request

                // console.log("check", check);
                if (check === true) {
                    return res.status(400).send({
                        message: 'Dependent already friend or request already sent.'
                    });
                } else {
                    // console.log("searching for carer: ", carerId);
                    return Carer.findOneAndUpdate({
                            _id: carerId
                        }, {
                            $push: {
                                pendingDependents: dependents[0]._id
                            }
                        }, options)
                        .then((carer) => {
                            // console.log("carer", carer);
                            // Add carer to list of pending carers for dependent
                            dependents[0].pendingCarers.push(carer._id);

                            return dependents[0].save().then((dependent) => {
                                    // console.log("saving dependent: ", dependent);
                                    return res.status(200).send({
                                        message: "Friend request sent",
                                        name: dependent.name
                                    });
                                })
                                .catch((err) => {
                                    return res.status(400).send(err);
                                });
                        })
                        .catch((err) => {
                            return res.status(400).send(err);
                        });
                }
            })
            .catch((err) => {
                console.log(err);
            });
    }).catch((err) => {
        console.log("err", err);
        return res.status(500).send({
            message: 'Internal server error.'
        })
    });
    return response;
};

/**
 * Specifically gets a carer's name given their mobile number
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const getCarerByMobile = (req, res, next) => {
    let userMobile = req.params.mobile;
    const response = Carer.find({
        mobile: userMobile
    }, (err, carer) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).send({
            name: carer[0].name
        });
    });

    return response;
}

export const carerIndex = {
    get: getCarer,
    put: updateCarer,
    delete: deleteCarer,
    sendFriendRequest: sendFriendRequest,
    getDependents: getDependents,
    getName: getCarerByMobile
};
