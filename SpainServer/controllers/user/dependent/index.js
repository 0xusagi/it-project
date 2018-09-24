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
        console.log("carer[0].id;", carer[0].id);
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


export const dependentIndex = {
    get: getDependent,
    put: updateDependent,
    delete: deleteDependent,
    addCarer: addCarerToDependent,
    getName: getDependentByMobile
};
