import { Dependent } from "../../../models/user";

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
    let carerId = req.body.carerId;
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

export const dependentIndex = {
    get: getDependent,
    put: updateDependent,
    delete: deleteDependent,
    addCarer: addCarerToDependent
};
