import Sequelize from 'sequelize';
import color from 'colors';

const sequelize = new Sequelize('cart', 'smartcartuser', 'a123456A', {
    host: process.env.postgres || 'localhost',
    logging: false,
    dialect: 'postgres',
    define: {
        freezeTableName: true,
        timestamps: false
      },
    pool: {
        max: 5,
        min: 0,
        idle: 10000
    },
});

sequelize.authenticate()
.then(() => { console.log('Connection to postgres has been established successfully.'.bgBlack.yellow);})
.catch(err => { console.error('Unable to connect to the database:'.bgRed.black, err); });

module.exports = sequelize;