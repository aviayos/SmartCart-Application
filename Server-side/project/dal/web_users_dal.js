import models from "../models/models";
let webUsersModel = models.web_users;

/**
 * Dal function to create a row in the web user table.
 * The arguments have already been validated by middlewares.
 * So, by getting here, all left is pushing into the table.
 *
 * @param { Object } newUserForm The validated form of a new user coming.
 * @returns { Promise } Promise of the sequelize response.
*/
export async function createWebUser(newUserForm) {
    return webUsersModel.create(newUserForm);
}

/**
 * Dal function to search for a user who wants to login to website.
 * The arguments have already been validated by middlewares.
 * So, by getting here, all left is to check whether these credentials appear in DB.
 * 
 * @param { Object } loginCredentials The validated form of a user wants to login.
 * @returns { Promise } Promise of the sequelize response.
 */
export async function checkLogin(loginCredentials) {
    console.log(loginCredentials)
    return webUsersModel.findOne({
        where: {
            email: loginCredentials.email,
            password: loginCredentials.password
        }
    });
};

export async function findUserById(expectedUsertId) {
    return webUsersModel.findOne({
        where: {
            business_id: expectedUsertId
        }
    });
};

export async function findUserByEmail(expectedUsertEmail) {
    return webUsersModel.findOne({
        where: {
            email: expectedUsertEmail
        }
    });
};

export async function getUsersIdAddressPhone() {
    return webUsersModel.findAll({
        attributes: ['business_id', 'location', 'phone']
    });
};

export async function getUsersMailById(userId) {
    return webUsersModel.findOne({
        attributes: ['email'],
        where: {
            business_id: userId
        }
    });
};

export async function getAddressById(userId) {
    return webUsersModel.findOne({
        attributes: ['location'],
        where: {
            business_id: userId
        }
    });
};

export async function getNameById(userId) {
    return webUsersModel.findOne({
        attributes: ['business_name'],
        where: {
            business_id: userId
        }
    });
};