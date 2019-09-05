
import models from "../models/models";
let productsModel = models.products;

/**
 * Create a new product at the products table.
 * 
 * @param { Object } newProductForm The form with the details required for the insert.
 * `{ product_name: string, price: number, createdby: uuid }`   
 */
export async function createProduct(newProductForm) {
    return productsModel.create(newProductForm);
};

/**
 * Update a product price at the database.
 * 
 * @param { Object } productUpdate The form with the details required for the insert.
 * `{ producid: string, newPrice: number }` 
 */
export async function updateProduct(productUpdate) {
    return productsModel.update({
        price: productUpdate.newPrice
    },
        {
            where: {
                productid: productUpdate.productid
            }
        });
};

/**
 * Remove product from the database.
 * 
 * @param { String } productId UUID/V4 which represent the id of the product to be deleted.
 */
export async function removeProduct(productId) {
    return productsModel.destroy({
        where: {
            productid: productId
        }
    });
};

/**
 * Get all the products by the owner.
 * The table `products` on database represents all the products off all bussiness.
 * Therefor, this function will return all the products of own the `ownerId`.
 * @param { String } ownerId UUID/V4 which represents the bussiness id.
 */
export async function getProductsByOwner(ownerId) {
    return productsModel.findAll({
        where: {
            createdby: ownerId
        }
    });
};

/**
 * Get products metadata.
 * This query to the database returns all the products of all of the supermarkets.
 * The data that filtered from the table is `productid`, `product_name`.
 */
export async function getProductsMetaData() {
    return productsModel.findAll({
        attributes: ['productid', 'product_name','category']
    });
}

export async function getProductsByPrice(productList, ownerId) {
    return productsModel.sum('price',
        {
            where: {
                createdby: ownerId,
                product_name: productList
            }
        })

}

export async function getProductsCount(productList, ownerId) {
    return productsModel.
    findAndCountAll({
        where: {
            createdby: ownerId,
            product_name: productList
        }
     })
}
