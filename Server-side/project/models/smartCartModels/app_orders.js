
module.exports = function(sequelize, DataTypes) {
    return sequelize.define('app_orders', {
        orderid: {
            type: DataTypes.UUID,
            primaryKey: true,
            defaultValue: DataTypes.UUIDV4
        },
        userid: DataTypes.UUID,
        supermarketid: DataTypes.UUID,
        price: DataTypes.REAL,
        order_list: DataTypes.ARRAY(DataTypes.TEXT) ,
        location :DataTypes.ARRAY(DataTypes.REAL)
    });
};