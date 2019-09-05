
module.exports = function(sequelize, DataTypes) {
    return sequelize.define('app_users', 
    {
        userid: {
            type: DataTypes.UUID,
            primaryKey: true,
            defaultValue: DataTypes.UUIDV4
        },
        first_name: DataTypes.STRING,
        last_name: DataTypes.STRING,
        email: DataTypes.STRING,
        password: DataTypes.STRING
    });
};