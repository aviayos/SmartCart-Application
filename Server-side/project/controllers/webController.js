import { validator } from '../middleware/utils';
import { createWebUser, checkLogin, findUserById, getAddressById } from '../dal/web_users_dal';
import { createProduct, updateProduct, removeProduct, getProductsByOwner } from '../dal/products_dal';
import { getOrdersListById, getLocationsBySupermarketId } from '../dal/app_orders_dal';
import { getUserDetailsById } from '../dal/app_users_dal';
import ServerError from './error';
import { unlink } from 'fs';
import { getCoordinates } from './externalAPI';

export async function webSignUp(req, res, next) {

    if (!validator(req, res)) {
        res.end()
        return;
    }

    var newWebUser = {
        first_name: req.body.first_name,
        last_name: req.body.last_name,
        business_name: req.body.business_name,
        location: req.body.business_address,
        email: (req.body.email).toLowerCase(),
        phone: req.body.phone,
        password: req.body.password,
    };

    try {
        const newUser = await createWebUser(newWebUser);
        res.status(200).send({ id: newUser.dataValues.business_id });
    }
    catch (err) {
        console.error(err);
        const error = new ServerError('body', 'email', req.body.email, err.errors[0].message)
        res.status(400).send([error]);
    }
};

export async function webLogin(req, res, next) {

    if (!validator(req, res)) {
        res.end()
        return;
    }

    var loginCredentials = {
        email: (req.body.email).toLowerCase(),
        password: req.body.password
    };

    try {
        const login = await checkLogin(loginCredentials);
        const err = new ServerError('body', 'email/password', 'any', 'Incorrect Credentials!');
        login ? res.status(200).send({ token: login.dataValues.business_id }) :
            res.status(400).send([err]);
    }

    catch (err) {
        res.status(500).end(err);
        console.error(err);
    }
};

export async function addProduct(req, res) {
    if (!validator(req, res)) {
        res.end()
        return;
    }
    var newProduct = {
        createdby: req.headers["authorization"],
        product_name: (req.body.product_name).toLowerCase(),
        price: req.body.price,
        category: req.body.category
    };

    try {
        const product = await createProduct(newProduct);
        res.status(200).send({ id: product.dataValues.productid });
    }
    catch (err) {
        console.error(err);
        res.status(500).send(err);
    }
};

export async function editProduct(req, res, next) {
    if (!validator(req, res)) {
        res.end()
        return;
    }
    var productUpdate = {
        productid: req.body.productid,
        newPrice: req.body.newPrice
    };
    try {
        const editedItems = await updateProduct(productUpdate);
        const err = new ServerError('body', 'productid', req.body.productid, 'Item id does not exist');
        editedItems[0] == 0 ?
            res.status(400).send([err])
            : res.status(200).end();
    }
    catch (err) {
        console.error(err);
        res.status(500).send(err);
    }
};

export async function deleteProduct(req, res, next) {

    if (!validator(req, res)) {
        res.end()
        return;
    }
    try {
        const deletedItems = await removeProduct(req.body.productid);
        const err = new ServerError('body', 'productid', req.body.productid, 'Item id does not exist');
        deletedItems == 0 ? res.status(400).send([err]) : res.status(200).end();
        unlink(`${process.cwd()}/uploads/images/${req.body.productid}.png`, () => { });
    }
    catch (err) {
        console.error(err);
        res.status(500).send(err);
    }
};

export async function getProducts(req, res, next) {
    try {
        var superId = req.headers['authorization'];

        const productDetails = (await getProductsByOwner(superId));
        const products = productDetails.map((productDetail) => (productDetail.dataValues.product_name));
        const orders = (await getOrdersListById(superId)).map((orderDetail) => (orderDetail.dataValues.order_list));;

        addPurchases(productDetails, products, orders);
        res.set('Cache-Control', 'no-cache');
        res.status(200).send(productDetails);
    }
    catch (err) {
        console.error(err);
        res.status(500).send(err);
    }
};

export async function getUserById(req, res, next) {
    const user = await findUserById(req.params.userId);
    user ? res.status(200).send({ first_name: user.dataValues.first_name, last_name: user.dataValues.last_name, business_name: user.dataValues.business_name }) :
        res.status(400).send([new ServerError('params', 'userId', req.params.userId, 'User with such id doesn\'t exist')]);
}

export async function getCooradinatesByAddress(req, res, next) {
    var businesId = req.headers['authorization'];

    var address = (await getAddressById(businesId)).dataValues.location;

    var latlng = (await getCoordinates(address))[0];

    var location = {
        address: address,
        latlng: {
            lat: latlng.latitude,
            lng: latlng.longitude
        }
    };
    location ? res.status(200).send(location) :
        res.status(400).send([new ServerError('request', 'body', address, 'Invalid address ')]);
}

export async function getOrdersLocationBySupermarketId(req, res, next) {
    var webUserId = req.headers['authorization'];

    var answer = [];
    var locations = (await getLocationsBySupermarketId(webUserId));
    if (locations == null)
    {
        res.status(400).send([new ServerError('request', 'headers', req.headers['authorization'], 'Invalid token')]);
    }
    else
    {
        for (var i = 0; i < locations.length; i++) {
            answer.push({
                lat: locations[i].dataValues.location[0],
                lng: locations[i].dataValues.location[1],
                orderTime: new Date().toLocaleString(),
                clientName: (await getUserDetailsById(locations[i].dataValues.userid)).dataValues.first_name
            })
        }
        res.status(200).send(answer);
    }
}

function addPurchases(productDetails, products, orders) {
    for (var i = 0; i < products.length; i++) {
        var counter = 0
        for (var j = 0; j < orders.length; j++) {
            if (orders[j].includes(products[i])) {
                counter++;
            }
        }
        productDetails[i].dataValues.numberOfPurchases = counter;
    }
}