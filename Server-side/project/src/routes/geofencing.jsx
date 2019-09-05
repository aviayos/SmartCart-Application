import React from 'react';
import axios from 'axios';
const uuidv4 = require('uuid/v4');
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
  DropdownButton,
  MenuItem,
  PanelHeader,
  PanelContainer,
  ButtonGroup,
  Table,
  Icon
} from '@sketchpixy/rubix';

class MapContainer extends React.Component {
  render() {
    return (
      <PanelContainer>
        <Panel>
          <PanelBody style={{padding: 25}}>
            <div id='color-palette'></div>
            <div>
                <button id='delete-button'>Delete Selected Shape</button>
            </div>
            <h4 className='text-center' style={{marginTop: 0}}>{this.props.name}</h4>
            {this.props.children}
            <div id={this.props.id} style={{height: 300}}></div>
          </PanelBody>
        </Panel>
      </PanelContainer>
    );
  }
}

export default class Geofencing extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        vioList: []
     };

    }
  markersArray = [];
  dragstart=false;
  isCleaning=false;
  countID=1;
  shapes = [];
  drawingManager;
  selectedShape;
  colors = ['#1E90FF', '#FF1493', '#32CD32', '#FF8C00', '#4B0082'];
  selectedColor;
  colorButtons = {};
  polyOptions = {
      strokeWeight: 0,
      fillOpacity: 0.45,
      editable: true,
      draggable: true
  };
    convertToGEOJson=(GoogleLatLanArry , cb)=>{
        var newArr=[]
        GoogleLatLanArry.forEach(element => {
            newArr.push([element.lng(),element.lat()]);
        });
        newArr.push(newArr[0])//Input geometries must be closed LINESTRINGS
        cb(newArr);
    }
    convertFromGEOJson=(DBArry , cb)=>{
        var newArr=[]
        DBArry.coordinates[0].forEach(element => {
            newArr.push({lng:element[0],lat:element[1]});
        });
        console.log(newArr);
         // close polygon.
        cb(newArr);
    }
    updateDB=(polygonObj)=>{
        this.convertToGEOJson(polygonObj.shape,(GEOJsonArry)=>{
            console.log("writing data to server")
            polygonObj.shape=GEOJsonArry;
            axios.post('/managementLayer/managePolygon',{ 
            "polygon":polygonObj
            }).then((res)=>{
                console.log(res)
            }).catch((err)=>{console.log(err)})
        })
    }
    QueryDB=()=>{
        console.log("QueryDB")
            axios.post('/managementLayer/managePolygon',{ 
            "polygon":{
                "getPolygonsFroMfmsEntity":true
            }
            }).then((res)=>{
                console.log(res)            
                res.data.map((polygon)=>{
                    this.convertFromGEOJson(polygon.shape,(GoogleLatLonPolygon)=>{
                        var NewPolygon=new google.maps.Polygon({
                            paths: GoogleLatLonPolygon,
                            strokeWeight: this.polyOptions.strokeWeight,
                            fillOpacity: this.polyOptions.fillOpacity,
                            editable: this.polyOptions.editable,
                            draggable: this.polyOptions.draggable,   
                            fillColor:this.selectedColor
                        })
                        console.log(`NewPolygon is ${NewPolygon}`)
                        NewPolygon.id=polygon.polygonfmsEntityid
                        NewPolygon.type="polygon";
                        console.log("setUpFunction")
                        this.setUpFunction(NewPolygon);
                        NewPolygon.setMap(this.map) 
                    })

                })
            }).catch((err)=>{console.log(err)})
        
    }
    
    setUpFunction=(newShape)=>{
        if (newShape.type !== google.maps.drawing.OverlayType.MARKER) {
            // Switch back to non-drawing mode after drawing a shape.
            this.shapes.push(newShape);
            this.drawingManager.setDrawingMode(null);

            // Add an event listener that selects the newly-drawn shape when the user
            // mouses down on it.
            google.maps.event.addListener(newShape, 'click',(e)=> {
                if (e.vertex !== undefined) {
                    if (newShape.type === google.maps.drawing.OverlayType.POLYGON) {
                        var path = newShape.getPaths().getAt(e.path);
                        path.removeAt(e.vertex);
                        if (path.length < 3) {
                            newShape.setMap(null);
                        }
                    }
                    if (newShape.type === google.maps.drawing.OverlayType.POLYLINE) {
                        var path = newShape.getPath();
                        path.removeAt(e.vertex);
                        if (path.length < 2) {
                            newShape.setMap(null);
                        }
                    }
                }
                this.setSelection(newShape);
            });
            this.setSelection(newShape);
        }
        else {
            google.maps.event.addListener(newShape, 'click',(e)=> {
                this.setSelection(newShape);
            });
            this.setSelection(newShape);
        }

        google.maps.event.addListener(newShape.getPath(), 'insert_at', ()=>{
            console.log("New point");
            this.updateDB({
                shape:newShape.getPath(),
                id:newShape.id,
                type:newShape.type,
                kind:newShape.kind
            })
        });
        
        google.maps.event.addListener(newShape.getPath(), 'remove_at', ()=>{
            // Point was removed

            console.log("Point was removed");
            this.updateDB({
                shape:newShape.getPath(),
                id:newShape.id,
                type:newShape.type,
                kind:newShape.kind
            })
        
        });
        
        google.maps.event.addListener(newShape.getPath(), 'set_at',()=>{
            // Point was moved
            if(!this.dragstart){
                console.log("Point was moved");
                console.log("the id is" ,newShape.id)
                this.updateDB({
                    shape:newShape.getPath(),
                    id:newShape.id,
                    type:newShape.type,
                    kind:newShape.kind
                })
            }
        });
        google.maps.event.addListener(newShape, 'dragend', ()=>{
            // Polygon was dragged
            this.dragstart=false;
            console.log("Point was moved");
            console.log("the id is" ,newShape.id)
            this.updateDB({
                shape:newShape.getPath().getArray(),
                id:newShape.id,
                type:newShape.type,
                kind:newShape.kind
            })
          });
        google.maps.event.addListener(newShape, 'dragstart', ()=>{

            // Polygon was dragged
            this.dragstart=true;
         });

    }

    deleteAllShape=(cb)=> {
        this.isCleaning=true;
        console.log("deleteAllShapes")
        if(this.shapes.length>0){
        var i=0
        for (; i < this.shapes.length; i++)
        {
            this.shapes[i].setMap(null);
            
        }
        console.log (`i: ${i}, length: ${this.shapes.length} `)
        if(i===this.shapes.length){
            this.shapes = [];
            console.log(`call to query from cleaning`)
            this.isCleaning=false;
            cb()     
        }}
        else{
            cb()
        }
        

    }
    componentWillReceiveProps(newProp){
        console.log(`componentWillReceiveProps with prop ${newProp}`)
        this.deleteAllShape(this.QueryDB);
        this.refreshData()
    }
    componentWillUnmount(){
        clearInterval( this.intervalID);
      }

      refreshData=()=>{

        axios.post('/elastic/get_All_Last_Location')
        .then((response)=>{
          console.log("this is a get_All_Last_Location success");
          console.log(response);
          this.addPositionToMap(response.data);
    
        })
        .catch(err => {
          console.log("this is an error");
          console.log(err);
        });


        axios.post('/managementLayer/violation_of_polygon')
        .then((response)=>{
          console.log(`this is a success array length is ${response.data.length}`);
          console.log(response);

          if(response.data.length >0 || (this.state && this.state.vioList.length >0))
          {
            console.log("setstate");
            if(this.state.vioList)
            {
                let vioList = Object.assign({}, this.state.vioList);
                
                vioList = response.data;
                this.setState({vioList});
            }
            else{
                this.setState({vioList:response.data})
            }
            
          }
    
        })
        .catch(err => {
          console.log("this is an error");
          console.log(err);
        });
        
      }  
      addToList(){

      }
      contains = (fmsuid)=>
      {
    if(this.state.vioList)
    {
      for (var i =0;i<this.state.vioList.length;i++)
      {
          
          console.log(`${i} and ${fmsuid}`)
          console.log(`state is ${JSON.stringify(this.state)}`)
        if (this.state.vioList[i].lp === fmsuid) return true;
      }
    }
      return false;
      }

      removeMarkers(){
        for (var i = 0; i < this.markersArray.length; i++ ) {
            this.markersArray[i].setMap(null);
          }
          this.markersArray.length = 0;
      }
      addPositionToMap =(data)=>
      {

        console.log(`addPositionToMap ${data}`)
        if(this.markersArray &&this.markersArray.length>0)
        {
          this.removeMarkers();
        }

        var splitedLocation;
        //var curData=data;
        data.forEach((element)=> {
        if(element.max_date.hits.hits[0]){
            var itemLocation = element.max_date.hits.hits[0]._source.location;
            splitedLocation= itemLocation.split(",");

            var markerobj={
                position:{
                lat: parseFloat(splitedLocation[0]),
                lng: parseFloat(splitedLocation[1]),
                },
                map: this.map,
                title: element.max_date.hits.hits[0]._source.fmsuid || element.max_date.hits.hits[0]._source.uid,   
            }
            if (this.contains(element.max_date.hits.hits[0]._source.fmsuid))
            {
                markerobj.icon= '/imgs/app/avatars/ldw.png';
            }

            var marker = new google.maps.Marker(markerobj);
            this.markersArray.push(marker)
            //this.map.addMarker(marker);          
        }

        });
    
        
      };
    
    componentDidMount(){
        this.refreshData()
        this.intervalID=setInterval(() => this.refreshData(), 15000)
        
        this.deleteButton=document.getElementById('delete-button');
        this.colorPalette = document.getElementById('color-palette');
        this.toolbar=document.getElementById('toolbar');
        this.map = new google.maps.Map(document.getElementById('map'), {
            zoom: 15,
            center: {lat: 31.803166, lng: 35.212055},
            mapTypeId: 'roadmap'
        });
        // Creates a drawing manager attached to the map that allows the user to draw
        // markers, lines, and shapes.

        this.drawingManager = new google.maps.drawing.DrawingManager({
            drawingMode: google.maps.drawing.OverlayType.POLYGON,
            drawingControl: false,
            markerOptions: {
                draggable: true
            },
            polylineOptions: {
                editable: true,
                draggable: true
            },
            rectangleOptions: this.polyOptions,
            circleOptions: this.polyOptions,
            polygonOptions: this.polyOptions,
            map: this.map
        });

        google.maps.event.addListener(this.drawingManager, 'overlaycomplete', (e)=> {
            console.log('overlaycomplete')
            var newShape = e.overlay;
            
            newShape.type = e.type;
            newShape.id=uuidv4();
            newShape.kind="enter"
            
            this.setUpFunction(newShape)

            this.updateDB({
                shape:newShape.getPath().getArray(),
                id:newShape.id,
                type:newShape.type,
                kind:newShape.kind
            })
        });
        google.maps.event.addListener(this.drawingManager, 'drawingmode_changed', this.clearSelection);
        google.maps.event.addListener(this.map, 'click', this.clearSelection);
        google.maps.event.addDomListener(this.deleteButton, 'click', this.deleteSelectedShape);
        this.map.controls[google.maps.ControlPosition.TOP_CENTER].push(this.toolbar);
        this.selectColor(this.colors[0])
        this.QueryDB();
        //this.buildColorPalette();
        
    }

    clearSelection= ()=> {
        if (this.selectedShape) {
            if (this.selectedShape.type !== 'marker') {
                this.selectedShape.setEditable(false);
            }
            
            this.selectedShape = null;
        }
    }
    setSelection= (shape)=> {
        if (shape.type !== 'marker') {
            this.clearSelection();
            shape.setEditable(true);
            this.selectColor(shape.get('fillColor') || shape.get('strokeColor'));
        }
        
        this.selectedShape = shape;
    }
    deleteSelectedShape= ()=> {

        console.log("deleteSelectedShape")
        if (this.selectedShape) {
            this.selectedShape.removed=true;
            this.updateDB({
                shape:this.selectedShape.getPath(),
                id:this.selectedShape.id,
                removed:this.selectedShape.removed
            })
            this.selectedShape.setMap(null);
        }
    }

    buildColorPalette= ()=> {
        
        // for (var i = 0; i < this.colors.length; ++i) {
        //     var currColor = this.colors[i];
        //     var colorButton = this.makeColorButton(currColor);
        //     this.colorPalette.appendChild(colorButton);
        //     this.colorButtons[currColor] = colorButton;
        // }
        // this.selectColor(this.colors[0]);
    }
    makeColorButton =(color)=> {
        var button = document.createElement('span');
        button.className = 'color-button';
        button.style.backgroundColor = color;
        google.maps.event.addDomListener(button, 'click', ()=> {
            this.selectColor(color);
            this.setSelectedShapeColor(color);
        });

        return button;
    }
    setSelectedShapeColor= (color)=> {
        if (this.selectedShape) {
            if (this.selectedShape.type == google.maps.drawing.OverlayType.POLYLINE) {
                this.selectedShape.set('strokeColor', color);
            } else {
                this.selectedShape.set('fillColor', color);
            }
        }
    }
    centerMap(location){
        console.log("center map")
        //var splitedLocation= location.split(",");
        if(this.map)
        {
        this.map.setCenter({lat: location[1],
                            lng: location[0]})
        }
    }


    selectColor= (color)=> {
        this.selectedColor = color;
        // for (var i = 0; i < this.colors.length; ++i) {
        //     var currColor = this.colors[i];
        //     this.colorButtons[currColor].style.border = currColor == color ? '2px solid #789' : '2px solid #fff';
        // }

        // Retrieves the current options from the drawing manager and replaces the
        // stroke or fill color as appropriate.
        var polylineOptions = this.drawingManager.get('polylineOptions');
        polylineOptions.strokeColor = color;
        this.drawingManager.set('polylineOptions', polylineOptions);

        var rectangleOptions = this.drawingManager.get('rectangleOptions');
        rectangleOptions.fillColor = color;
        this.drawingManager.set('rectangleOptions', rectangleOptions);

        var circleOptions = this.drawingManager.get('circleOptions');
        circleOptions.fillColor = color;
        this.drawingManager.set('circleOptions', circleOptions);

        var polygonOptions = this.drawingManager.get('polygonOptions');
        polygonOptions.fillColor = color;
        this.drawingManager.set('polygonOptions', polygonOptions);
    }
    // CaenterMapLine = [['0,0'],['1,1'],['2,2']].map(function(name){
    //     return   
                  
    //   })



    render() {
               console.log(`render is now!`)  
    this.namesList = this.state.vioList.map((obj,index)=>{
        return(
            // className={index==1?'warning':'danger'}
            <tr >
            <td>                     
            <Button bsStyle='paleblue' data-toggle="tooltip" data-placement="bottom" title="show on map"
                onClick={()=>{this.centerMap(obj.location.coordinates)}}>
            <Icon glyph="icon-simple-line-icons-graph" />
            </Button>
            </td>
            <td>Naor Levi</td>
            <td>{obj.lp}</td>
            <td>{new Date(obj.time).toLocaleDateString()}</td>
          </tr>  );
    })
               
        return (
        <div>
        <Row>
          <Col sm={8} collapseRight>
          <Row>
            <Col xs={12}>
                <PanelContainer>
                <Panel>
                    <PanelBody >
                    {/* <FontAwesomeIcon icon="coffee" /> */}
                    {/* <div id='color-palette'></div> */}
                    <div id ="toolbar" style={{marginTop:10}}>
                    <ButtonGroup >
                      <Button id='delete-button' bsStyle='paleblue' data-toggle="tooltip" data-placement="bottom" title="delete selected polygon" 
                     onClick={this.deleteSelectedShape}>
                      <Icon glyph='icon-feather-cross' />
                      </Button>
                      <Button id='create-square-button' bsStyle='paleblue' data-toggle="tooltip" data-placement="bottom" title="create square fance"
                      onClick={()=>{this.drawingManager.setDrawingMode(google.maps.drawing.OverlayType.RECTANGLE)}}>
                      <Icon glyph='icon-feather-stop' />
                      </Button>
                      <Button id='create-circle-button' bsStyle='paleblue' data-toggle="tooltip" data-placement="bottom" title="create circle fance"
                       onClick={()=>{this.drawingManager.setDrawingMode(google.maps.drawing.OverlayType.CIRCLE)}}>
                      <Icon glyph="icon-feather-record" />
                      </Button>
                      <Button id='create-polygon-button' bsStyle='paleblue' data-toggle="tooltip" data-placement="bottom" title="create polygon fance"
                       onClick={()=>{this.drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYGON)}}>
                      <Icon glyph="icon-simple-line-icons-graph" />
                      </Button>
                      <Button id='move-button' bsStyle='paleblue' data-toggle="tooltip" data-placement="bottom" title="move map"
                      onClick={()=>{this.drawingManager.setDrawingMode(null)}}>
                      <Icon glyph="icon-simple-line-icons-cursor-move" />
                      </Button>
                      <DropdownButton id="bg-nested-dropdown" bsStyle='paleblue' title="shape mode">
                        <MenuItem  eventKey="1" onClick={()=>{this.selectColor(this.colors[0])}} >Enter</MenuItem>
                        <MenuItem active eventKey="2" onClick={()=>{this.selectColor(this.colors[1])}} >Exit</MenuItem>
                      </DropdownButton>
                    </ButtonGroup>
                  </div>
                    <Grid>
                        <Row style={{padding: 25}}>
                        <div id='map' style={{height: 600}}></div>
                        </Row>
                    </Grid>
                    </PanelBody>
                </Panel>
                </PanelContainer>
            </Col>
            </Row>

          </Col>
          <Col sm={4} collapseRight>
 
          <PanelContainer>
              <Panel>
                <PanelBody>
                  <Grid>
                    <Row>
                      <Col xs={12}>
                        <h4 style={{marginTop: 0}}>violation of polygon constraints</h4>
                        <Table striped>
                          <thead>
                            <tr>
                              <th>#</th>
                              <th>Driver Name</th>
                              <th>Lisence plate</th>
                              <th>violation time</th>
                            </tr>
                          </thead>
                          <tbody>
                          
                            { this.namesList }
                        
                          </tbody>
                        </Table>
                      </Col>
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
