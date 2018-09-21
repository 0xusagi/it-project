import { Carer } from "../../../models/user";

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
    const response = Carer.findByIdAndUpdate(req.params.id,
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
    const response = Carer.findByIdAndDelete(req.params.id, (err, carer) => {
        if (err) {
            return res.status(400).send(err);
        }

        return res.status(200).json(carer);
    });

    return response;
};

/**
 * Specifically adds a dependent to a carer based on supplied dependent id from the client.
 *
 * @param req
 * @param res
 * @param next
 * @returns {Query}
 */
const addDependentToCarer = (req, res, next) => {
    // console.log("here");
    // console.log("req.body", req.body);
    let depId = req.body.dependentId;
    console.log("depId", depId);
    // let depUpdate = {
    //     dependents: []
    // };
    // depUpdate.dependents.push(depId);
    // console.log("depUpdate", depUpdate);

    let options = {new: true};
    const response = Carer.findByIdAndUpdate(req.params.id,
        { $push: { dependents: depId } }, options,
        (err, carer) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).json(carer);
    });

    return response;
};

export const carerIndex = {
    get: getCarer,
    put: updateCarer,
    delete: deleteCarer,
    addDependent: addDependentToCarer
};
