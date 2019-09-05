import models from "../models/models";
let appOrdersModels = models.app_orders;

export async function createOrder (newOrder){
    return appOrdersModels.create(newOrder);
}


export async function getOrdersListById(superId) {
    return appOrdersModels.findAll({
        where: {
            supermarketid: superId 
        }
    });
}

export async function getLocationsBySupermarketId(superId) {
    return appOrdersModels.findAll({
        attributes: ['location','userid'],
        where: {
            supermarketid: superId 
        }
    });
}