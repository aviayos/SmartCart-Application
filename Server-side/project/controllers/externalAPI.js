import NodeGeocoder from 'node-geocoder';
import geodist from 'geodist';
import nodemailer from 'nodemailer';    

export async function getCoordinates(address) {
    var geocoder = NodeGeocoder({
        provider: 'opencage',
        apiKey: '56fd3560d9b74245b78e2fa6c1b35227'
    });

    try {
        return geocoder.geocode(address);
    }
    catch (err) {
        console.error(err);
    }
};

export async function getDistance(pointA, pointB) {

    try {
        var dist = geodist(pointA, pointB, {exact: true, unit: 'km'});
        return dist;
    }
    catch (err) {
        console.error(err);
    }
};

export function sendEmail(mailAddress,mailSubject,mailHtml) {
    var transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            user: 'smartcartnoreply@gmail.com',
            pass: 'q1w2e3r4t5!'
        }
    });

    var mailOptions = {
        from: 'smartcartnoreply@gmail.com',
        to: mailAddress,
        subject: mailSubject,
        html: mailHtml
    };

    transporter.sendMail(mailOptions, function (err, info) {
        return err
    });
}