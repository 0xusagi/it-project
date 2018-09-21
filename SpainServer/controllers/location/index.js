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
    return res.status(200).json({message: "test"});
};

export const locationIndex = {
    new: newLocation,
    getAll: getAllLocations
};
