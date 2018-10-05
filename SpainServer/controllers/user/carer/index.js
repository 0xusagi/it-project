import { Carer } from "../../../models/user";
import { Dependent } from "../../../models/user";

/**
 * Gets a carer from the database from a supplied id parameter.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const getCarer = (req, res, next) => {
    const response = Carer.findById(req.params.id, (err, carer) => {
        if (err) {
            return res.status(400).send(err);
        }

        return res.status(200).json(carer);
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
    let options = {new: true};
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
 * Specifically adds a dependent to a carer's list of dependents
 * based on supplied dependent id from the client.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const addDependentToCarer = (req, res, next) => {
    const options = {new: true};
    const carerId = req.params.id;
    const mobile = req.body.mobile;
    const response = Dependent.find({mobile: mobile}, (err, dependents) => {
        // Could not find dependent's mobile number in the database.
        if (err || dependents.length === 0) {
            return res.status(400).send({message: 'Dependent not found in database.'})
        }
        // Mobile number found, finding the carer and adding it to their list first
        return Carer.findOneAndUpdate(carerId,
            { $push: { dependents: dependents[0]._id } }, options,
            (err, carer) => {
                if (err) {
                    return res.status(400).send(err);
                }
                // Add carer to list of pending carers for dependent
                dependents[0].pendingCarers.push(carer);

                return dependents[0].save((err, dependent) => {
                    if (err) {
                        return res.status(400).send(err);
                    }

                    return res.status(200).send({name: dependents[0].name});
                });
            })
    });
    // return name of the requested dependent.
    return response;
};

/**
 * Gets all dependents of a carer from the database
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const getDependentsOfCarer = (req, res, next) => {
    const response = Carer.findById(req.params.id, (err, carer) => {
        if (err) {
            return res.status(400).send(err);
        }
        // console.log("carer", carer);
        let dependents_array = handleCarerDependents(carer.dependents);
        return res.status(200).json(dependents_array);
    });

    return response;
};

function handleCarerDependents(dependents) {
    var response2, dependents_arr = [];
    dependents.forEach((dependent_id) => {
        // console.log("dependent_id", dependent_id);
        response2 = Dependent.findById(dependent_id, (err, dep) => {
            if (err) {
                return res.status(400).send(err);
            }
            // console.log("dep",dep);
            dependents_arr.push(dep);
            // console.log("dependents_arr (1)",dependents_arr);
        });
        // console.log("dependents_arr (2)",dependents_arr);
    });
    // console.log("returning dependents_arr", dependents_arr);
    return dependents_arr;
}

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
    const response = Carer.find({mobile: userMobile}, (err, carer) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).send({name: carer[0].name});
    });

    return response;
}

export const carerIndex = {
    get: getCarer,
    put: updateCarer,
    delete: deleteCarer,
    addDependent: addDependentToCarer,
    getDependents: getDependentsOfCarer,
    getName: getCarerByMobile
};
