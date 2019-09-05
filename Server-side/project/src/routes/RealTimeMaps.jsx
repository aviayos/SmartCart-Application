import React from 'react';
import axios from 'axios';

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

export default class RealTimeMaps extends React.Component {
  constructor(props) {
    super(props);

    this.geocode = null;
    this.routingmap = null;
    this.state = {
      routeslist: []
    };
  }

  geoCode(address) {
    GMaps.geocode({
      address: address,
      callback: (results, status) => {
        if (status == 'OK') {
          var latlng = results[0].geometry.location;
          this.geocode.setCenter(latlng.lat(), latlng.lng());
          this.geocode.addMarker({
            lat: latlng.lat(),
            lng: latlng.lng(),
            infoWindow: {
              content: '<div><strong>Address:</strong> '+results[0].formatted_address+'</div>'
            }
          });
        }
      }
    });
  }
  addPositionToMap2 =(data)=>
  {
    if(this.Markermap)
    {
      this.Markermap.removeMarkers();
    }
    var splitedLocation;
    //var curData=data;
    data.forEach(function(element) {
    splitedLocation= element._source.location.split(",");

    this.Markermap.addMarker({
    lat: splitedLocation[0],
    lng: splitedLocation[1],
    title: element._source.fmsuid,
    click: (e) => {
      alert(`alert type ${element._source.alert_type} time  ${element._source.gps_ts} on pos ${element._source.location}`);
    }
  });
    }, this);
    this.Markermap.setCenter(splitedLocation[0],splitedLocation[1])
    if (this.Markermap.markers[0].getAnimation() == null) {
      this.Markermap.markers[0].setAnimation(google.maps.Animation.BOUNCE);
    }
    console.log("this.Markermap",this.Markermap)
  };


  refreshData=()=>{

    axios.post('/elastic/get_the_last_location')
    .then((response)=>{
      console.log("this is a success");
      console.log(response);
      this.addPositionToMap2(response.data);

    })
    .catch(err => {
      console.log("this is an error");
      console.log(err);
    });
    
  }
  componentWillUnmount(){
    clearInterval( this.intervalID);
  }
  componentWillReceiveProps(newProp)
  {
    this.refreshData()
  }
  intervalID;
  componentDidMount() {

    console.log(" this.mapt", this.mapt)
    this.Markermap = new GMaps({
      scrollwheel: false,
      div: '#markers',
      zoom: 12,
      lat: 31.803166,
      lng: 35.212055,

    });

    this.refreshData()
    this.intervalID=setInterval(() => this.refreshData(), 10000)
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
                    {/* <MapContainer id='markers' name='Map Markers' /> */}
                    <div id='markers' style={{height: 600}}></div>
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
