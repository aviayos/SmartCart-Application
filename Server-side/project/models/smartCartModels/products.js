
module.exports = function(sequelize, DataTypes) {
    return sequelize.define('products', {
        
        productid: {
            type: DataTypes.UUID,
            primaryKey: true,
            defaultValue: DataTypes.UUIDV4
        },
        createdby: DataTypes.UUID,
        product_name: DataTypes.STRING,
        price: DataTypes.REAL,
        createdon: {
            type: DataTypes.DATE, 
            defaultValue: DataTypes.NOW
        },
        category: DataTypes.ENUM('FRUITS AND VEGS','DAIRY','MEET','OTHER')
    });
};