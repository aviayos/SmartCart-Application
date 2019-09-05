import { check, validationResult } from "express-validator/check";

export const webSignUpValidation = [
    check("email").isEmail().withMessage("Email must be valid!"),
    check('password').isLength({ min: 6 }).withMessage("Password should be at least 6 characters!"),
    check('first_name').not().isEmpty().withMessage("Fisrt name cannot be empty!"),
    check('last_name').not().isEmpty().withMessage("Last name cannot be empty!"),
    check('business_name').not().isEmpty().withMessage("Business Name cannot be empty!"),
    check('business_address').not().isEmpty().withMessage("Business address cannot be empty!"),
    check('phone').not().isEmpty().withMessage("phone number cannot be empty!"),
    check('phone').isMobilePhone().withMessage("phone number must be valid!")
];

export const appSignUpValidation = [
    check("email").isEmail().withMessage("Email must be valid!"),
    check('password').isLength({ min: 6 }).withMessage("Password should be at least 6 characters!"),
    check('first_name').not().isEmpty().withMessage("Fisrt name cannot be empty!"),
    check('last_name').not().isEmpty().withMessage("Last name cannot be empty!"),
];

export const loginValidation = [
    check("email").isEmail().withMessage("Email must be valid."),
    check("password").isLength({ min: 6 }).withMessage("Password should be at least 6 characters..")
];

export const addProductValidation = [
    check("product_name").not().isEmpty(),
    check("price").not().isEmpty().isNumeric().withMessage("value must contain numbers only"),
    check("Authorization").not().isEmpty().isUUID().withMessage("value either empty or not UUID"),
    check("category").not().isEmpty().matches(/(FRUITS AND VEGS|DAIRY|MEAT|OTHER)/).withMessage("Invalid category")
];

export const editProductValidation = [
    check("productid").not().isEmpty().isUUID().withMessage("value either empty or not UUID"),
    check("newPrice").isNumeric().withMessage("value must contain numbers only"),
    check("newPrice").not().isEmpty().withMessage("value cannot be empty")
];

export const deleteProductValidation = [
    check("productid").not().isEmpty().isUUID().withMessage("value either empty or not UUID")
];

export const filterValidation = [
    check("filterType").matches(/(totalprice|distanceToSuper|productAvailabilty)/).withMessage("Invalid filter type"),
    check("location.lat").isNumeric().withMessage("Invalid lat"),
    check("location.lng").isNumeric().withMessage("Invalid lng"),
    check("productList").not().isEmpty().isArray().withMessage("Invalid product list")
];

export const multipleFilterValidation = [
    check("filterType.totalprice").matches(/[0-3]/).withMessage("Invalid total price"),
    check("filterType.distanceToSuper").matches(/[0-3]/).withMessage("Invalid distance To Super"),
    check("filterType.productAvailabilty").matches(/[0-3]/).withMessage("Invalid product Availabilty"),
    check("location.lat").isNumeric().withMessage("Invalid lat"),
    check("location.lng").isNumeric().withMessage("Invalid lng"),
    check("productList").not().isEmpty().isArray().withMessage("Invalid product list")
];

export const orderValidation = [
    check("supermarketid").not().isEmpty().isUUID().withMessage("Invalid supermarket Id"),
    check("price").not().isEmpty().isNumeric().withMessage("value must contain numbers only"),
    check("order_list").not().isEmpty().isArray().withMessage("Invalid order list"),
    check("location.lat").isNumeric().withMessage("Invalid lat"),
    check("location.lng").isNumeric().withMessage("Invalid lng"),
];

export function validator(req, res) {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        res.status(400).send(errors.array());
        return false;
    }
    return true;
};

export function predicateBy(prop) {
    return function (a, b) {
        if (a[prop] > b[prop]) {
            return 1;
        } else if (a[prop] < b[prop]) {
            return -1;
        }
        return 0;
    }
}

export function removeCookie(req, res, next) {
    res.clearCookie('token', { path: '/' });
    res.clearCookie('token', { path: '/ltr' });
    res.clearCookie('token', { path: '/ltr/login' });
    next();
};

export function removeClientCookie(req, res, next) {
    res.clearCookie('token', { path: '/' });
    res.clearCookie('token', { path: '/ltr' });
    res.clearCookie('token', { path: '/ltr/login' });
    res.status(200).end();
}