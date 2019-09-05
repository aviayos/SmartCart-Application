
import {findUserByEmail} from "../dal/app_users_dal";
import {findUserByEmail as findWebUserByEmail} from '../dal/web_users_dal';
import ServerError from '../controllers/error';
import { sendEmail } from '../controllers/externalAPI';

export default async function (req, res, next) {
    var email = (req.body.email).toLowerCase();
    var appUser = await findUserByEmail(email);
    var webUser = await findWebUserByEmail(email);
    
    if (!appUser && !webUser)
        res.status(400).send([new ServerError('body', 'email', email, 'Email does not exist')]);

    const password = appUser ? appUser.dataValues.password : webUser.dataValues.password;
    var subject = 'Recovery password for SmartCart';
    var html = `<p>Please Do not reply to this email.<br/>Your password is: <strong>${password}</strong></p>`;
    
    var status = sendEmail(email,subject,html);
    status ? res.status(500).send(status) :
    res.status(200).send({msg: `Password has been sent to ${req.body.email}\nEmail will arrive at the next couple of minutes`});
}

