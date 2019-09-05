
import models from "../models/models";
let appUsersModel = models.app_users;

/**
 * Create a new application user.
 * I.e add a new row at the table.
 * @param { Object } newUserForm The form with the detailes required to insert.
 */
export async function createAppUser(newUserForm) {
    return appUsersModel.create(newUserForm);
};

/**
 * Login request upon the server.
 * The dal with check whether the credentials are fit the database.
 * @param { Object } loginCredentials The credentials need to be authenticated.
 */
export async function checkLogin(loginCredentials) {
    return appUsersModel.findOne({ 
        where: {
            email: loginCredentials.email, 
            password: loginCredentials.password 
        }
    });
};

/**
 * Look for a user with a given uuid.
 * Look at the DB and check if such user exists. 
 * @param { String } expectedUserId UUID/V4.
 */
export async function findUserById(expectedUserId) {
    return appUsersModel.findOne({ 
        where: {
            userid: expectedUserId 
        }
    });
};

/**
 * Look for a user with a given email.
 * Look at the DB and check if such user exists. 
 * @param { String } expectedUserEmail The email needs to be checked on db.
 */
export async function findUserByEmail(expectedUserEmail) {
    return appUsersModel.findOne({ 
        where: {
            email: expectedUserEmail 
        }
    });
};

export async function getUserDetailsById(userId) {
    return appUsersModel.findOne({ 
        where: {
            userid: userId 
        }
    });
};