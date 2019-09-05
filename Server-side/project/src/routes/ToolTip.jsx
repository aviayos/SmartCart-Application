import React from "react";
import { Tooltip, OverlayTrigger }  from "@sketchpixy/rubix";

// TODO: Consider migration to functional component.
/**
 * Tooltip for an object.
 * @props `id`. `description` - Text to display. `placement` - "top", "bottom", "left", "right"
 */
export default class ToolTip extends React.Component {

    render() {
        let tooltip = <Tooltip id={this.props.id} desc={this.props.desc}></Tooltip>

        return (
            <OverlayTrigger
                overlay={tooltip}
                placement={this.props.placement}
                delayShow={200} 
                delayHide={200}>
            </OverlayTrigger>
        );
    }
}