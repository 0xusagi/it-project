import { Location } from "../../models/location";
import {Dependent} from "../../models/user";

const getAllLocations = (req, res, next) => {
    let response = Location.find({}, (err, locations) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).json(locations);
    });

    return response;
};

const getLocation = (req, res, next) => {
    let response = Location.findById(req.params.id, (err, location) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).json(location);
    });
    return response;
};

const updateLocation = (req, res, next) => {
    let options = {new: true};
    let response = Location.findOneAndUpdate(req.params.id,
    req.body, options,
    (err, location) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).json(location);
    });
    return response;
};

const deleteLocation = (req, res, next) => {
    let response = Location.findOneAndDelete(req.params.id, (err, location) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).json(location);
    });
    return response;
};

const addLocationToDependent = (location, dependentId) => {
    return Dependent.findByIdAndUpdate(dependentId, {$push: {locations: location._id}}, {new: true})
};

/**
 * Add locations to a dependent
 * @param req
 * @param res
 */
const addToDependent = (req, res) => {
    return Location.create(req.body)
        .then(location => {
                if (!location) {
                    return false
                }
                addLocationToDependent(location, req.params.id)
        })
        .then(updatedDependent => {
            if (!updatedDependent) {
                return res.status(400).send({message: 'unable to add location to dependent.'})
            }
            return res.status(200).json(updatedDependent)
        })
        .catch(err => res.status(500).send({message: 'Server Error.'}));
};

export const locationIndex = {
    get: getLocation,
    getAll: getAllLocations,
    put: updateLocation,
    delete: deleteLocation,
    addToDependent: addToDependent
};
