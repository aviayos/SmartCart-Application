import React from 'react';
import axios from 'axios';
var MarkerClusterer = require('node-js-marker-clusterer');
import {
  Row,
  Col,
  Grid,
  Form,
  Panel,
  Button,
  FormGroup,
  PanelBody,
  InputGroup,
  FormControl,
  PanelHeader,
  PanelContainer,
} from '@sketchpixy/rubix';

class MapContainer extends React.Component {
  render() {
    return (
      <PanelContainer>
        <Panel>
          <PanelBody style={{padding: 25}}>
            <h4 className='text-center' style={{marginTop: 0}}>{this.props.name}</h4>
            {this.props.children}
            <div id={this.props.id} style={{height: 300}}></div>
          </PanelBody>
        </Panel>
      </PanelContainer>
    );
  }
}

class MapBoxContainer extends React.Component {

  componentDidMount() {  
    mapboxgl.accessToken = 'pk.eyJ1IjoibmFvcmlrbyIsImEiOiJjanZzNTdjamkzMjloNDNsNjRjNjIwYmNhIn0.rtVWoE2fInrUnsF2Y9A_hw';
    var map = new mapboxgl.Map({
        container: 'Mapbox', // container id
        style: 'mapbox://styles/mapbox/streets-v9', // stylesheet location
        //style: 'mapbox://styles/mapbox/dark-v9',
        center: [6.8853,51.1167], // starting position [lng, lat]
        zoom: 14// starting zoom
    });
    map.on('load', function() {
      map.addSource('trees', {
        type: 'geojson',
        data: '/imgs/app/avatars/trees.geojson'
      });

      map.addLayer({
        id: 'trees-heat',
        type: 'heatmap',
        source: 'trees',
        maxzoom: 15,
        paint: {
          // increase weight as diameter breast height increases
          'heatmap-weight': {
            property: 'dbh',
            type: 'exponential',
            stops: [
              [1, 0],
              [62, 1]
            ]
          },
          // increase intensity as zoom level increases
          'heatmap-intensity': {
            stops: [
              [11, 1],
              [15, 3]
            ]
          },
          // assign color values be applied to points depending on their density
          'heatmap-color': [
            'interpolate',
            ['linear'],
            ['heatmap-density'],
            0, 'rgba(236,222,239,0)',
            0.2, 'rgb(208,209,230)',
            0.4, 'rgb(166,189,219)',
            0.6, 'rgb(103,169,207)',
            0.8, 'rgb(28,144,153)'
          ],
          // increase radius as zoom increases
          'heatmap-radius': {
            stops: [
              [11, 15],
              [15, 20]
            ]
          },
          // decrease opacity to transition into the circle layer
          'heatmap-opacity': {
            default: 1,
            stops: [
              [14, 1],
              [15, 0]
            ]
          },
        }
      }, 'waterway-label');

      map.addLayer({
        id: 'trees-point',
        type: 'circle',
        source: 'trees',
        minzoom: 14,
        paint: {
          // increase the radius of the circle as the zoom level and dbh value increases
          'circle-radius': {
            property: 'dbh',
            type: 'exponential',
            stops: [
              [{ zoom: 15, value: 1 }, 5],
              [{ zoom: 15, value: 62 }, 10],
              [{ zoom: 22, value: 1 }, 20],
              [{ zoom: 22, value: 62 }, 50],
            ]
          },
          'circle-color': {
            property: 'dbh',
            type: 'exponential',
            stops: [
              [0, 'rgba(236,222,239,0)'],
              [10, 'rgb(236,222,239)'],
              [20, 'rgb(208,209,230)'],
              [30, 'rgb(166,189,219)'],
              [40, 'rgb(103,169,207)'],
              [50, 'rgb(28,144,153)'],
              [60, 'rgb(1,108,89)']
            ]
          },
          'circle-stroke-color': 'white',
          'circle-stroke-width': 1,
          'circle-opacity': {
            stops: [
              [14, 0],
              [15, 1]
            ]
          }
        }
      }, 'waterway-label');
    })

  }
  render() {
    return (
      <div>

            <h4 className='text-center' style={{marginTop: 0}}>{this.props.name}</h4>
            <div id={this.props.id} style={{ width:'100%' ,height: 600}} ></div>

      </div>
    );
  }
}
export default class Maps extends React.Component {
  constructor(props) {
    super(props);

    this.geocode = null;
    this.routingmap = null;
    this.state = {
      routeslist: []
    };
  }


  render() {
    return (
      <div>
        <Row>
          <Col xs={12}>
            <PanelContainer>
              <Panel>
                <PanelBody >
                  <Grid>
                    <Row style={{padding: 25}}>
                     <MapBoxContainer id='Mapbox' name='Mapbox' /> 
                    </Row>
                  </Grid>
                </PanelBody>
              </Panel>
            </PanelContainer>
          </Col>
        </Row>
      </div>
    );
  }
}
