import { validator, predicateBy } from '../middleware/utils';
import { sendEmail } from '../controllers/externalAPI';
import { getCoordinates, getDistance } from '../controllers/externalAPI';
import { createAppUser, checkLogin, getUserDetailsById } from '../dal/app_users_dal';
import { createOrder } from '../dal/app_orders_dal';
import { getProductsMetaData, getProductsByPrice as getProductsPrice, getProductsCount } from '../dal/products_dal';
import { getUsersIdAddressPhone, findUserById, getUsersMailById, getNameById } from '../dal/web_users_dal';
import ServerError from './error';


export async function appSignUp(req, res, next) {

    if (!validator(req, res)) {
        res.end()
        return;
    }

    var newAppUser = {
        first_name: req.body.first_name,
        last_name: req.body.last_name,
        email: (req.body.email).toLowerCase(),
        password: req.body.password,
    };

    try {
        const newUser = await createAppUser(newAppUser);
        res.status(200).send({ id: newUser.dataValues.userid });
    }
    catch (err) {
        console.error(err);
    }
};

export async function appLogin(req, res) {

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
        login ? res.status(200).send({ token: login.dataValues.userid }) :
            res.status(400).send([new ServerError('body', 'email/password', 'any', 'Incorrect Credentials!')]);
    }

    catch (err) {
        res.status(500).end(err);
    }
};

export async function appMetadata(req, res, next) {
    try {
        const productList = await getProductsMetaData();

        var cleanList = productList.filter((productList, index, self) =>
            index === self.findIndex((t) => (t.product_name === productList.product_name)))

        var listByCategories = {
            meat: cleanList.filter((product) => (product.dataValues.category == 'MEAT')),
            dairy: cleanList.filter((product) => (product.dataValues.category == 'DAIRY')),
            fruitsAndVegs: cleanList.filter((product) => (product.dataValues.category == 'FRUITS AND VEGS')),
            other: cleanList.filter((product) => (product.dataValues.category == 'OTHER'))
        }
        res.status(200).send(listByCategories);
    }
    catch (err) {
        res.status(500).end(err);
    }
};

const scoreMap = {
    0: 0,
    1: 0.125,
    2: 0.250,
    3: 0.500
}
export async function appMultipleFilter(req, res, next) {
    if (!validator(req, res)) {
        res.end()
        return;
    }

    var priorities = req.body.filterType;
   
    var scoreWeight = {
        totalprice: scoreMap[priorities.totalprice],
        distanceToSuper: scoreMap[priorities.distanceToSuper],
        productAvailabilty: scoreMap[priorities.productAvailabilty],
    }
    console.log(scoreWeight.totalprice);

    var productList = (req.body.productList).map((product) => (product.toLowerCase()));
    var supermarketDetails = (await getUsersIdAddressPhone());
    var supermarketIds = supermarketDetails.map((userDetail) => (userDetail.dataValues.business_id));
    var supermarketAddresses = supermarketDetails.map((userDetail) => (userDetail.dataValues.location));
    var supermarketPhones = supermarketDetails.map((userDetail) => (userDetail.dataValues.phone));

    var list = [];
    for (var i = 0; i < supermarketIds.length; i++) {
        var superLocation = {
            lat: (await getCoordinates(supermarketAddresses[i]))[0].latitude,
            lng: (await getCoordinates(supermarketAddresses[i]))[0].longitude,
        }

        list.push({
            superid: supermarketIds[i],
            supername: (await findUserById(supermarketIds[i])).dataValues.business_name,
            totalprice: await getProductsPrice(productList, supermarketIds[i]),
            productAvailabilty: (await getProductsCount(productList, supermarketIds[i])).count / productList.length * 100,
            distanceToSuper: await getDistance(superLocation, req.body.clientLoc),
            location: superLocation,
            address: supermarketAddresses[i],
            phone: supermarketPhones[i],
            get score(){return this.totalprice * scoreWeight.totalprice +
                this.distanceToSuper * scoreWeight.distanceToSuper +
                this.productAvailabilty * scoreWeight.productAvailabilty}
        });
    }
    console.log((list[0]).score);
    list.sort(predicateBy('score'))
    res.status(200).send(list);
};

export async function appOrder(req, res, next) {
    if (!validator(req, res)) {
        res.end()
        return;
    }
    try {
        var location = req.body.location;
        var order = {
            supermarketid: req.body.supermarketid,
            price: req.body.price,
            order_list: req.body.order_list,
            userid: req.headers.authorization,
            location: [location.lat, location.lng]
        }
        var supermarketMail = (await getUsersMailById(order.supermarketid)).dataValues.email;
        var supermarketName = (await getNameById(order.supermarketid)).dataValues.business_name;
        var subject = "New Order";
        var html = `<p>New Order:
                        <br/>from: ${(await getUserDetailsById(order.userid)).dataValues.first_name}
                        <br/>order list: ${order.order_list}
                        <br/>price: ${order.price}
                        <br/>date: ${new Date().toLocaleString()}
                        <br/>order Id: ${(await createOrder(order)).dataValues.orderid}
                    </p>`;

        var status = sendEmail(supermarketMail, subject, html);
        status ? res.status(500).send(status) :
            res.status(200).send({ msg: `an order has been submited to ${supermarketName}` });
    }
    catch (err) {
        console.log(err);
        res.status(500).send(err);
    }
};


export async function appFilter(req, res, next) {
    if (!validator(req, res)) {
        res.end()
        return;
    }
    var filter = req.body.filterType;
    var productList = req.body.productList;
    var supermarketDetails = (await getUsersIdAddressPhone());
    var supermarketIds = supermarketDetails.map((userDetail) => (userDetail.dataValues.business_id));
    var supermarketAddresses = supermarketDetails.map((userDetail) => (userDetail.dataValues.location));
    var supermarketPhones = supermarketDetails.map((userDetail) => (userDetail.dataValues.phone));

    var list = [];
    for (var i = 0; i < supermarketIds.length; i++) {
        var superLocation = {
            lat: (await getCoordinates(supermarketAddresses[i]))[0].latitude,
            lng: (await getCoordinates(supermarketAddresses[i]))[0].longitude,
        }

        list.push({
            superid: supermarketIds[i],
            supername: (await findUserById(supermarketIds[i])).dataValues.business_name,
            totalprice: await getProductsPrice(productList, supermarketIds[i]),
            productAvailabilty: (await getProductsCount(productList, supermarketIds[i])).count / productList.length * 100,
            distanceToSuper: await getDistance(superLocation, req.body.clientLoc),
            location: superLocation,
            address: supermarketAddresses[i],
            phone: supermarketPhones[i]
        });
    }

    list.sort(predicateBy(filter))
    res.status(200).send(list);
};