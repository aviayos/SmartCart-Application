import React from 'react';
import Alert from '@sketchpixy/rubix/lib/Alert';

export default function FailureMessage(props) {
    return (props.show == false) ?
        <div>
            <br/>
            <Alert danger>
                <strong>InputError! </strong><br/><span>{props.msg}</span>
            </Alert>
        </div>
        :
        null;
};