import {Dependent} from "../../models/user";

const getAllFromDependent = (req, res, next) => {
    // sort popularity in ascending order
    const response = Dependent.findById(req.params.id,
        {destinations: { $sort: {popularity: 1}}},
        (err, dependent) => {
        if (err) {
            return res.status(400).send(err);
        }
        const locations = {};

        if (req.params.homeLocation === 'true') {
            locations.homeLocation = dependent.homeLocation;
        }

        if (req.params.destinations === 'true') {
            locations.destinations = dependent.destinations;
        }

        return res.status(200).json(locations);
    })
};

const newLocationDependent = (req, res, next) => {
    
};