
import React from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import Cookies from 'universal-cookie';
import moment from 'moment';
import {
  Row,
  Tab,
  Col,
  Nav,
  Icon,
  Grid,
  Form,
  Table,
  Label,
  Panel,
  Button,
  NavItem,
  Checkbox,
  Progress,
  PanelBody,
  FormGroup,
  PanelLeft,
  isBrowser,
  InputGroup,
  LoremIpsum,
  PanelRight,
  PanelHeader,
  FormControl,
  PanelContainer,
  PanelTabContainer,
} from '@sketchpixy/rubix';


export default class RouteMaps extends React.Component {
    constructor(){
        super();
        this.state={troute:{
            origin: [31.802865, 35.212196],
            destination: [31.802938, 35.213108],
            
          travelMode: 'driving',
          step: (e)=>{
            this.list.push({
              instructions: e.instructions,
              lat: e.end_location.lat(),
              lng: e.end_location.lng(),
              path: e.path
            });
          },
          end: (e)=> {
            var lat, lng, path;
            var processList = (i)=> {
              if(this.list.length === i) return;
              lat = this.list[i].lat;
              lng = this.list[i].lng;
              path = this.list[i].path;
              this.map.drawPolyline({
                path: path,
                strokeColor: '#FF6FCF',
                strokeWeight: 8
              });
              processList(i+1);
            };
            processList(0);
          }
        }
    }
    }
    list = []
    createRoute=(data)=>{
        var cRoute=this.state.troute;
        var waypts = [];
        var splitedLocation;
        var i=0;
        this.each = parseInt(data.length/20);
        data.forEach((element)=> {
            if(element!=="0,0")
            {
                if(i++ == this.each){
                splitedLocation=element.split(",");
                waypts.push({
                    location: element,
                    stopover: false
                });
                i=0;
            }
             }
            }  ) 
        splitedLocation =data[0].split(",");
        cRoute.origin= data[0]//[splitedLocation[0],splitedLocation[1]];
        splitedLocation =data[data.length-1].split(",");
        cRoute.destination= data[data.length-1]//[splitedLocation[0],splitedLocation[1]];
        cRoute.waypoints=waypts;
        console.log(cRoute)
        console.log(this)
        splitedLocation =data[0].split(",");
        this.map.setCenter(splitedLocation[0],splitedLocation[1])
        this.map.travelRoute(this.state.troute);
        //this.setState(cRoute)
    }
    polylines=[]
    addPositionToMap2 =(data)=>
    {
      if( this.map)
      {
         this.map.removeMarkers();
      }
      //var curData=data;
      data.forEach(function(element) {
      var splitedLocation = element._source.location.split(",");
  if ( element._source.alert_type=='LDW'){
    this.map.addMarker({
        lat: splitedLocation[0],
        lng: splitedLocation[1],
        //label:element._source.alert_type,
        title: element._source.alert_type,
        icon:'/imgs/app/avatars/ldw.png',
        click: (e) => {
          alert(`alert type ${element._source.alert_type} time  ${element._source.gps_ts} on pos ${element._source.location}`);
        }
      });
    
  }
  else{
    this.map.addMarker({
        lat: splitedLocation[0],
        lng: splitedLocation[1],
        //label:element._source.alert_type,
        title: element._source.alert_type,
        click: (e) => {
          alert(`alert type ${element._source.alert_type} time  ${element._source.gps_ts} on pos ${element._source.location}`);
        }
      });
  }

      }, this);
      console.log("this.Markermap",this.map)
    };

      componentWillReceiveProps()
      {
        for (var i = 0; i < this.polylines.length; ++i) {
            this.polylines[i].setMap(null);
          }
          this.polylines = [];
        this.refreshData();
      }
    componentDidMount() {

       this.map = new GMaps({
            div: '#routingmap',
            lat: 31.802865,
            lng: 35.212196,
            scrollwheel: false,
            zoom: 14
          });
        this.refreshData();
        

    }
    runSnapToRoad =(data)=> {

        var pathValues = [];
        var i=0;
        var setcenter = false
        data.forEach((element)=> {
            if(element!=="0,0")
            {
                if(i++<100)
                {
                    // if(!setcenter)
                    // {
                    //     var splitedLocation =element[0].split(",");
                    //     this.map.setCenter(splitedLocation[0],splitedLocation[1])
                    //     setcenter=true;
                    // }

                pathValues.push(element);
                }
                else{
                    var PartOfPathValue=pathValues.slice(0, pathValues.length);
                    pathValues=[];
                    i=0;
                    $.get('https://roads.googleapis.com/v1/snapToRoads', {
                        interpolate: true,
                        key: 'AIzaSyB-aE9iJanVdWG6pwMqbADY2e2UtEV7Yek',
                        path: PartOfPathValue.join('|')
                      }, (response)=> {
                          //console.log("path",path);
                          console.log("response",response)
                        this.processSnapToRoadResponse(response);
                        this.drawSnappedPolyline();
                        //this.getAndDrawSpeedLimits();
                      });

                }
            }
             
         })       
        $.get('https://roads.googleapis.com/v1/snapToRoads', {
          interpolate: true,
          key: 'AIzaSyB-aE9iJanVdWG6pwMqbADY2e2UtEV7Yek',
          path: pathValues.join('|')
        }, (response)=> {
            //console.log("path",path);
            console.log("response",response)
          this.processSnapToRoadResponse(response);
          this.drawSnappedPolyline();
          //this.getAndDrawSpeedLimits();
        });
      }

// Draws the snapped polyline (after processing snap-to-road response).
 drawSnappedPolyline=()=> {
    var snappedPolyline = new google.maps.Polyline({
      path: this.snappedCoordinates,
      strokeColor: 'black',
      strokeWeight: 3,
      geodesic: true,
    });
  
    snappedPolyline.setMap(this.map.map);
    console.log(this.map)
    this.polylines.push(snappedPolyline);
  }

// Store snapped polyline returned by the snap-to-road service.
 processSnapToRoadResponse=(data)=> {
    this.snappedCoordinates = [];
    //this.placeIdArray = [];
    for (var i = 0; i < data.snappedPoints.length; i++) {
      var latlng = new google.maps.LatLng(
          data.snappedPoints[i].location.latitude,
          data.snappedPoints[i].location.longitude);
          this.snappedCoordinates.push(latlng);
          //this.placeIdArray.push(data.snappedPoints[i].placeId);
    }
  }

    render() {
      return (
        <PanelContainer collapseBottom>
          <Panel>
            <PanelHeader>
              <div style={{padding: 25}}>
                <div id='routingmap' style={{height: 600}}></div>
                <div className='fg-black50 text-center' style={{borderBottom: '1px solid #ccc'}}>
                  <h5 style={{padding: 12.5, margin: 0}}>DRIVING 0.3 KM - FOR 6 MINUTES</h5>
                </div>
              </div>
            </PanelHeader>
          </Panel>
        </PanelContainer>
      );
    }
  }