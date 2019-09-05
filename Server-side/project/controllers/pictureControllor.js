import ServerError from '../controllers/error';
import fs from 'fs';

export function getImage(req, res, next) {
    let imgsDir = `${process.cwd().slice(0, process.cwd().indexOf('tmp'))}t/uploads/images`;
    let imgs = fs.readdirSync(imgsDir);
    if (!imgs.includes(`${req.params.id}.png`)) {
        res.status(400).send([new ServerError('params', 'imageId', req.params.id, 'No Such Image')]);
        return;
    }

    res.status(200).sendFile(`${imgsDir}/${req.params.id}.png`);
};

export function uploadImage(req, res, next) {
    let imgsDir = `${process.cwd().slice(0, process.cwd().indexOf('tmp'))}t/uploads/images/`;
    let imgArrNames = fs.readdirSync(imgsDir);
    let img;
    for (let i = 0; i < imgArrNames.length; i++) {
        if (imgArrNames[i].split('.')[1] === 'png')
            continue;
        else {
            img = imgArrNames[i];
            break;
        }
    }
    fs.rename(`${imgsDir}${img}`, `${imgsDir}${req.params.id}.png`, () => {
        res.json(req.file);
    });
};