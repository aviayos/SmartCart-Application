import React from 'react';
import Image from '@sketchpixy/rubix/lib/Image';
import ToolTip from '@sketchpixy/rubix/lib/Tooltip';
import OverlayTrigger from '@sketchpixy/rubix/lib/OverlayTrigger';

export default class LogoPanel extends React.Component {
    render() {
        let tooltip =(<ToolTip id={"SmartCart"}>{"SmartCart"} </ToolTip>)
        return(
            <OverlayTrigger
                overlay={tooltip}
                placement='bottom'
                delayShow={200}
                delayHide={200}
            >
                <Image responsive={this.props.responsive} src='/imgs/app/logo_small.png' width={this.props.width} height={this.props.height}></Image>
            </OverlayTrigger>
        );
    }
};