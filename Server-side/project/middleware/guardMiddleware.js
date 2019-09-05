import {findUserById as findWebUserById}  from "../dal/web_users_dal";
import {findUserById}  from "../dal/app_users_dal";
import ServerError from '../controllers/error';
import validator from 'validator';

async function validateToken(token) {
    return (await findUserById(token) != null)
    || (await findWebUserById(token) != null);
};

export default async function(req, res, next) {
    
    if(!("authorization" in req.headers)) {
        res.status(401).send(new ServerError('headers', 'authorization', "", 'No Token provided'));
        return;
    };

    var token = req.headers.authorization;
    
    if(!validator.isUUID(token)){
        res.status(401).send(new ServerError('headers', 'authorization', token, 'Bad token'));
        return;
    }
    
    if(! (await validateToken(req.headers['authorization']))) {
        res.status(403).send(new ServerError('headers', 'authorization', token, 'Unauthorized token'));
        return;
    };

    next();
}