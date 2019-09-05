
module.exports = function(sequelize, DataTypes) {
    return sequelize.define('web_users', {

        business_id: {
            type: DataTypes.UUID,
            primaryKey: true,
            defaultValue: DataTypes.UUIDV4
        },
        first_name: DataTypes.STRING,
        last_name: DataTypes.STRING,
        email: DataTypes.STRING,
        password: DataTypes.STRING,
        business_name: DataTypes.STRING,
        phone: DataTypes.STRING,
        location: DataTypes.STRING
    });
};