import { Location } from "../../models/location";

/**
 * Create a new location in the mongo database from client-supplied parameters and
 * return a response if successful.
 * @param req
 * @param res
 * @param next
 */
const newLocation = (req, res, next) => {
    const newLocation = new Location({
        // firstly, the required fields:
        lat: req.body.lat,
        long: req.body.long,
        displayName: req.body.displayName,
        // all other optional fields
        addressLine1: req.body.addressLine1,
        addressLine2: req.body.addressLine2,
        postcode: req.body.postcode,
        state: req.body.state,
        description: req.body.description,
        imageUrl: req.body.imageUrl,
        popularity: req.body.popularity
    });

    if (newLocation === false) {
        return res.status(400).json({message: "User Error (Wrong user type inputted)"});
    }

    const response = newLocation.save((error, location) => {
        if (error) {
            return res.status(400).json(error);
        }
        return res.status(201).json(location);
    });

    return response;
};

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
    let response = Location.findByIdAndUpdate(req.params.id,
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
    let response = Location.findByIdAndDelete(req.params.id, (err, location) => {
        if (err) {
            return res.status(400).send(err);
        }
        return res.status(200).json(location);
    });
    return response;
};

export const locationIndex = {
    new: newLocation,
    get: getLocation,
    getAll: getAllLocations,
    put: updateLocation,
    delete: deleteLocation
};
