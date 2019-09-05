import React from 'react';
import Alert from '@sketchpixy/rubix/lib/Alert';

export default function SuccessMessages(props) {
    return (props.show) ?
        <div>
            <br/>
            <Alert success>
                <strong>Well Done! </strong><br/><span>{props.msg}</span>
            </Alert>
        </div>
        :
        null;
};