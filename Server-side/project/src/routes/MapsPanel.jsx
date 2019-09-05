import React from "react";
import MapBoxContainer from "./MapBoxContainer.jsx";
import { PanelContainer, Panel } from "@sketchpixy/rubix";
import config from '../../config/config';
import axios from "axios";
import Cookies from "universal-cookie";

const TAG = "[MapsPanel]";

/*
* Component: MapsPanel.js
*
* The component gets a mapBox JS object as prop and renders it on a BPanel.
*
*/

export default class MapsPanel extends React.Component {
    constructor(props) {
        super(props);
        this.cookies = new Cookies();
        this.accessToken = 'pk.eyJ1IjoibmFvcmlrbyIsImEiOiJjanZzNTdjamkzMjloNDNsNjRjNjIwYmNhIn0.rtVWoE2fInrUnsF2Y9A_hw';
        this.Markers = [];
        this.state = {
            token: this.cookies.get('token'),
            map: null,
            marker: null,
            ordersMarkers: []
        };
    }

    componentDidMount() {
        const hours = new Date().getHours();
        const headers = { 'Content-Type': 'application/json', 'Authorization': this.state.token };

        mapboxgl.accessToken = this.accessToken;
        let theMap = new mapboxgl.Map({
            container: "Mapbox", // container id
            style: hours < 7 || hours > 18 ? 'mapbox://styles/mapbox/navigation-guidance-night-v2' : "mapbox://styles/mapbox/streets-v10",
            center: config.DEFAULT_MAP_CENTER, // starting position [lng, lat]
            zoom: 12 // starting zoom
        });
        this.setState({ map: theMap }, async () => {
            const ordersRes = await axios.get(config.WEB_GET_ORDERS, { headers: headers });
            this.setState({ ordersMarkers: ordersRes.data }, () => {
                this.state.ordersMarkers.forEach(marker => {
                    const popUp = new mapboxgl.Popup({offset: 25}).setText(`Name: ${marker.clientName}\nTime: ${marker.orderTime}`);
                    this.Markers.push(new mapboxgl.Marker().setLngLat([marker.lng, marker.lat]).setPopup(popUp).addTo(this.state.map));
                });
            });
           setTimeout(() => {
            const popUp = new mapboxgl.Popup({offset: 25}).setText(`Your Address: ${this.props.secName}`);
            let marker = new mapboxgl.Marker({color: '#FF0000'}).setLngLat(this.props.center).setPopup(popUp).addTo(this.state.map);
            this.state.map.flyTo({center: this.props.center})
            this.setState({marker: marker});
           }, 800);
        });
    };

    render() {
        return (
            <PanelContainer>
                <Panel>
                    <MapBoxContainer
                        id="Mapbox"
                        name="On The Map"
                        secName={this.props.secName}
                        map={this.state.map}
                    />
                </Panel>
            </PanelContainer>
        );
    };
};