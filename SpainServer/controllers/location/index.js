import { Location } from "../../models/location";
import {Dependent} from "../../models/user";

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
        .then(location => addLocationToDependent(location, req.params.id))
        .then(updatedDependent => res.status(200).json(updatedDependent))
        .catch(err => {
            console.log(err)
            return res.status(400).send({message: 'Server Error. Unable to add location to dependent.'});
        });
};

const getLocationsForDependents = (req, res) => {
    return Dependent.findById(req.params.id)
        .then(dependent => {
            if (!dependent) {
                throw "Dependent does not exist."
            }
            console.log(dependent)
            return Location.find({_id: {$in: dependent.locations}})
        })
        .then(locations => res.status(200).json(locations))
        .catch(err => res.send(400).json(err));
};

export const locationIndex = {
    get: getLocation,
    put: updateLocation,
    delete: deleteLocation,
    addToDependent: addToDependent,
    getLocationsForDependents: getLocationsForDependents
};
