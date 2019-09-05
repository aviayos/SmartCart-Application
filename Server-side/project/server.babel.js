import path from 'path';
import express from 'express';
import compression from 'compression';
import cookieParser from 'cookie-parser';
import app_routs from "./APIRouts/app_routes";
import web_routs from "./APIRouts/web_routes";
import morgan from 'morgan';
import bodyParser from 'body-parser';
import routes from './src/routes';
import { renderHTMLString } from '@sketchpixy/rubix/lib/node/router';
import RubixAssetMiddleware from '@sketchpixy/rubix/lib/node/RubixAssetMiddleware';
import guardMiddleware from './middleware/guardMiddleware';
import colors from 'colors';
import sequelize from './models/sequelize-connection';
import smartCartModels from './models/models';
import recoverPassword from './middleware/recoverPassword';
import { removeCookie, removeClientCookie } from './middleware/utils';
import { getImage, uploadImage } from './controllers/pictureControllor';
import multer from 'multer';

const upload = multer({dest: __dirname.slice(0, __dirname.indexOf('tmp')) + '/uploads/images'});
const port = process.env.PORT || 9000;

let app = express();
app.use(bodyParser.json());
app.use(cookieParser());
app.use('/ltr/login', removeCookie);
app.use(morgan('dev'));
app.use(compression());
app.use(express.static(path.join(process.cwd(), 'public')));
app.use('/*/api', guardMiddleware);
app.use ("/app", app_routs);
app.use ("/web", web_routs);
app.use('/*/forgot-password', recoverPassword);
app.use('/image/:id', getImage);
app.use('/web/api/upload-image/:id', upload.single('avatar'), uploadImage);
app.use('/delete-cookie', removeClientCookie);
app.set('views', path.join(process.cwd(), 'views'));
app.set('view engine', 'pug');

function renderHTML(req, res) {
  renderHTMLString(routes, req, (error, redirectLocation, html) => {
    if (error) {
      if (error.message === 'Not found') {
        res.status(404).send(error.message);
      } else {
        res.status(500).send(error.message);
      }
    } else if (redirectLocation) {
      res.redirect(302, redirectLocation.pathname + redirectLocation.search);
    } else {
      res.render('index', {
        content: html
      });
    }
  });
}

app.get('*', RubixAssetMiddleware('ltr'), (req, res, next) => {
  renderHTML(req, res);
});


let server = app.listen(port, () => {
  console.log(`Node.js app is running at port: ${port}.`.bgBlack.yellow);
});

var io = require('./server.io').initialize(server);