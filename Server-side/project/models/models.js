import sequelizeImport from 'sequelize-import';
import sequelizeConnection from './sequelize-connection';
import color from 'colors';

/**
 * Use `sequelize-import` npm lib to import all the models.
 */
export default (function() {
    let modelsDirName = __dirname.slice(0, __dirname.indexOf('tmp')) + 'models/smartCartModels';
    var smartCartModels = sequelizeImport(modelsDirName, sequelizeConnection);
   
    sequelizeConnection.sync()
    .then(() => console.log("Models were successfully synced to DB.".bgBlack.yellow))
    .catch(err => console.error(err))
    
    return smartCartModels;
})();