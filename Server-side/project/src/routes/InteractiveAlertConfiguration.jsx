
//import 'css/index.scss';

//import 'rc-tooltip/assets/bootstrap.css';
//import 'rc-slider/assets/index.css';

import React from 'react';
import ReactDOM from 'react-dom';
import Slider from 'rc-slider';
import axios from 'axios';


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

import dataObject from './dataObject.json';

class AlertType extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      invited:false ,
      //this.props.invited ? true : false,
      invitedText: this.props.invited ? 'configure' : 'configured',
    };

  }
  shouldUpdate =true;
  style = { width: '100%' };
  arrayOfAttr=[];
  values=[];
  volumeMarks = {
    0:'0',
    20: '1',
    40: '2',
    60: '3',
    80: '4',
    100: '5',
  };
  
  cb= (values, valueForProps)=>{
    console.log('before listToupdate',values)
    if(!this.listToupdate){
    this.listToupdate=values
    }
    if(this.state.invited==false){
      this.setState({
        invited: true,
        invitedText: 'configure'
      });
    }
    else{
//TODO dvir
      console.log ('exist list to update ',this.listToupdate.setting[0].val)
      values.setting[0].val.forEach((element, i) => {
        
        var picked =  this.listToupdate.setting[0].val.findIndex((o) => o.key === element.key);
        if(picked>0)
        {
          console.log ('picked',picked)
          //console .log(`found ${o}`)
          console.log ('new' ,element)
          console.log('old', this.listToupdate.setting[0].val[picked])
          this.listToupdate.setting[0].val[picked]=element;
        }
        else{
          this.listToupdate.setting[0].val.push(element);
        }
        if(values.setting[0].val.length-1==i )
        {
          console.log('this.listToupdate',this.listToupdate)
          if(this.state.invited==false){
            this.setState({
              invited: true,
              invitedText: 'configure'
            });
          }
        }
     });
    }

   }
  responseTamplate={
    header: {
      prot: {
        ver: "Naor",
        name: "Levi"
      }
    },
    body: {
      transactionid: 1,
      fmsid: "",
      fleetid: "",
      group: {
        name: "DEV_group",
        type: 1,
        setting:""
      }
    }
  }

  handleClick(e) {
    e.preventDefault();
    e.stopPropagation();
    //////////
    //axios/// 
    //////////
    if(this.listToupdate)
    {

      this.responseTamplate.body.group.setting=this.listToupdate.setting;
      axios.post('/managementLayer/wsSetGroupSettings.php',{
        "JSONrequest":JSON.stringify(this.responseTamplate)
      })
      .then((response)=>{
        console.log("the group settings  is ", response.data.body)
        console.log("ths this now is",this )
        //self.setState({filter : response.data});
        console.log("**************update server *****************")


      })
      .catch(err => {
        console.log("there is groupsettings update response  error");
        console.log(err);
      });
    }
    this.shouldUpdate=false;
    this.setState({
      invited: !this.state.invited,
      invitedText: (this.state.invited) ? 'configured' :'configure'  ,

    });
  }

  componentDidMount=()=> {

   if (this.props.attrs && Array.isArray(this.props.attrs))
   {
     this.arrayOfAttr=this.props.attrs;
     this.arrayOfAttr.map(obj=>{obj.cb=this.cb})
     this.setState({
      invited: this.state.invited,
      invitedText: (this.state.invited) ? 'configure' : 'configured',
    });
   }
   console.log("arrayOfAttr",this.arrayOfAttr)
  }
  render() {
    return (
      <tr>
        <td style={{verticalAlign: 'middle', borderTop: this.props.noBorder ? 'none': null ,width:'15%'} }>
          <img src={`/imgs/app/avatars/${this.props.avatar}.png`} />
           <span class="caption"> {this.props.name}</span>
        </td>
        <td style={{width:'70%' } }>
         {this.arrayOfAttr.map(obj=>{return <SenSlider attr={obj} shouldUpdate={this.shouldUpdate} />})}         
        </td>
        <td style={{verticalAlign: 'middle', borderTop: this.props.noBorder ? 'none': null ,width:'15%'} } className='text-right'>
          <Button onlyOnHover bsStyle='orange' active={this.state.invited} onClick={::this.handleClick}>
            {this.state.invitedText}            
          </Button>
        </td>
      </tr>
    );
  }
}

 class SenSlider extends React.Component {
  constructor(props) {

    super(props);
    this.state = {
      value: props.attr.defaultValue
    };
    this.marks=props.attr.marks;
    this.name=props.attr.name;
    this.cb=props.attr.cb;
    this.count = props.attr.count;
    this.included=props.attr.included;
    this.handleStyle=props.attr.handleStyle;
    this.fsField=props.attr.fsField;
    this.entriesList=Object.entries(props.attr.marks)
    this.featureName=props.attr.featureName
  }
  style = { width: '100%', margin:"40px 0 40px 0"};

  log=(value)=> {
    var ObjectToUpdate={
      setting:[{
        key:this.featureName,
        ts:0,
        unit:"",
        val:[
        ]
        }
      ]
    }
    for(var i=0;i<value.length; i++)
    {
      this.entriesList.map((o, index)=>{if (Number(o[0])===value[i]){
        ObjectToUpdate.setting[0].val.push({
           key:this.fsField[i],
           ts:0,
           unit:"",
           val:[
             {
              key:"Equal",
              ts:0,
              unit:"",
              val:index
             }
           ]
        })
        }     
      });  
    }
    this.cb(ObjectToUpdate,value);

    // this.formattedValue=[];
    // value.forEach(element => {

    //   this.formattedValue.push(this.marks[element])
    // });
    console.log(value)
    //this.cb(this.formattedValue,this.name);
    //console.log(this.formattedValue,this.name); //eslint-disable-line
  }
  onSliderChange = (value) => {
    this.log(value);
    console.log(value)
    this.setState({
      value:value
    });
  }
  onAfterChange = (value) => {
 //   console.log(value); //eslint-disable-line
  }


  componentDidMount(){
   
  }   
  componentWillReceiveProps(newProps) {
    if(newProps && newProps.shouldUpdate)
    {
    console.log("componentWillReceiveProps",newProps)
    console.log(this.props.attr.defaultValue)
      this.setState({value: this.props.attr.defaultValue});
    }
  }
  render(){
    return(
      <div style={this.style}>
        <div>
          {this.name}
        </div>
        <Slider.Range 
        //allowCross={false}
        value={this.state.value}
        count={this.count} 
        marks={this.marks}  
        step={null}  
        handleStyle={this.handleStyle  }
        included={this.included}
        onChange={this.onSliderChange} 
        />
       </div>
    );
  }
}
export default class InteractiveAlertConfiguration extends React.Component {
  constructor (){
    super();

  }
  state={
    settings:[]
  }; 

  sliAttrs=[{
    marks:{
      0:'0',
      16: '1',
      33: '2',
      50: '3',
      66: '4',
      82: '5',
      100: '6'
    },   
    fsField : [
      "sl_minvalue",
      "slmodelevelvalue",
      "sl_maxvalue",
    ],
    name: "sli activation",
    featureName: "sli",
    defaultValue :[0,66,82],
    count:3,
    handleStyle: [{ backgroundColor: 'transparent' }, { backgroundColor: '#FA824F' },{ backgroundColor: 'transparent' }]
  },
  ]
  
  brAttrs=[{
    marks:{
      0:'0',
      16: '1',
      33: '2',
      50: '3',
      66: '4',
      82: '5',
      100: '6'
    },
    name: "BR sensitivity",
    featureName: "blinker reminder",
    fsField : [
    "blink_rem_minvalue",
    "blinkremmodelevel",
    "blink_rem_maxvalue",
    ],
    defaultValue:[0,66,82],
    count:3,
    handleStyle: [{ backgroundColor: 'transparent' }, { backgroundColor: '#FA824F' },{ backgroundColor: 'transparent' }]
  },
  ]
  hmwAttrs= [
   { marks:{
    0:'0.1',
    4: '0.2',
    8: '0.3',
    12: '0.4',
    16: '0.5',
    20: '0.6',
    24: '0.7',
    28: '0.8',
    32: '0.9',
    36: '1',
    42: '1.2',
    48: '1.4',
    54: '1.6',
    60: '1.8',
    66: '2',
    100: '2.5',
            }, 
    fsField : [   
      "hmw_minvalue", 
    "hmwmodelevel",
    "hmw_maxvalue",],
    featureName: "hmw",
    name:  "HMW sensitivity",
    defaultValue:[6,42,94],
    count:3,
    handleStyle: [{ backgroundColor: 'transparent' }, { backgroundColor: '#FA824F' },{ backgroundColor: 'transparent' }]
  },
  {
    marks:{
          0:'0',
          20: '1',
          40: '2',
          60: '3',
          80: '4',
          100: '5'
          },
          name:"HMW Volume",
          featureName: "hmw",
          fsField : [
          "buzzerhmwminvolume",
          "hmwvolume",
          "buzzerhmwmaxvolume",
          ],
          defaultValue:[20,80,100],
          count:3,
          handleStyle: [{ backgroundColor: 'transparent' }, { backgroundColor: '#FA824F' },{ backgroundColor: 'transparent' }]   
             //cb:this.cb
    },
    {marks:{
      0:'0.1',
      4: '0.2',
      8: '0.3',
      12: '0.4',
      16: '0.5',
      20: '0.6',
      24: '0.7',
      28: '0.8',
      32: '0.9',
      36: '1',
      42: '1.2',
      48: '1.4',
      54: '1.6',
      60: '1.8',
      66: '2',
      100: '2.5',
          },     
            name: "HMW REP sensitivity",
            featureName: "hmw",
            fsField : [
            "hmw_rep_minvalue",
            "hwrepmodelevel",
            "hmw_rep_maxvalue",
            ],
            defaultValue:[6,42,94],
            count:3,
            handleStyle: [{ backgroundColor: 'transparent' }, { backgroundColor: '#FA824F' },{ backgroundColor: 'transparent' }]
// cb:this.cb
     }
]
ldwAttrs=[
  {marks:{
    0:'Off',
    50: 'low sensitivity',
    100: 'high sensitivity'
    },
    name:"LDW MODE",
    featureName: "ldw",
    fsField : [
    "ldw_minvalue",
    "ldwmodelevel",
    "ldw_maxvalue",
    ],
    //included :true,
    count:3,
    defaultValue:[0,50,100],
    handleStyle: [{ backgroundColor: 'transparent' }, { backgroundColor: '#FA824F' },{ backgroundColor: 'transparent' }]
    //cb:this.cb
  },
  {marks:{
    0:'50 km/h',
    50: '60 km/h ',
    100: '70 km/h'
    },
    fsField : [
    "ldw_speed",],
    included :false,
    name:"LDW speed",
    featureName: "ldw",
    count:1,
    defaultValue:[50],
    handleStyle: [{ backgroundColor: '#FA824F' }]
    //cb:this.cb
  },
  {marks:{
    0:'0',
    20: '1',
    40: '2',
    60:'3',
    80:'4',
    100:'5',
    },
    name:"LDW volume",
    fsField : [
    "buzzerldwminvolume",
    "buzzerldwmaxvolume",],
    featureName: "ldw",
    count:2,
    defaultValue:[0,60],
    handleStyle: [{ backgroundColor: 'transparent' },{ backgroundColor: 'transparent' }]
    //cb:this.cb
  }
]
  
//   //q=this.state.settings[sl_minvalue] || 0;
//   setUpSettings=function(){
//     if  (!this.state){
//       return;
//     }
//     console.log("**************dvir*****************88")
//     //this.slientries=
//     //console.log("this.sliAttrs",this.sliAttrs)
//     //console.log("stateVal",stateVal)
//     this.sliAttrs[0].defaultValue=[
//       Object.entries(this.sliAttrs[0].marks)[this.state.setting[this.sliAttrs[0].left]][0],
//       this.sliAttrs[0].defaultValue[1],
//       this.sliAttrs[0].defaultValue[2]
//     ]
//     //return true;
//   }
// temp=this.setUpSettings();
find_in_array (arr,val,cb)
{
  var a = arr.find(function (obj){
    if (obj.key === val )
      return true;
  
  })
  if(a)
    cb(a.val);
}
update=(settings,cb)=>{
  if (settings.length >0)
  {
    var list_to_update=[this.sliAttrs,this.brAttrs,this.hmwAttrs,this.ldwAttrs];
    for(var k=0;k<list_to_update.length;k++)
    {
      var temp = list_to_update[k];  
      var entriesList='';
      var fieldName='';
      
      var result =[];
      for(var i=0;i<temp.length;i++){
        result=temp[i].defaultValue.slice(0, temp[i].defaultValue.length);
        console.log(temp[i].marks)
        entriesList = Object.entries(temp[i].marks);
        var tempfsfiled= temp[i].fsField;
        for(var j=0;j<tempfsfiled.length;j++){
        var field = tempfsfiled[j];
          this.find_in_array(settings,field, (val)=>
          {    
            console.log('step:',val)
            console.log('entriesList',entriesList)
            if(entriesList[val]){
              console.log(`change value ${temp[i].defaultValue[j]} on place ${j} to ${entriesList[val][0]}`)
              result[j]=~~entriesList[val][0] ;
              console.log(result)
            }
          });
          if(tempfsfiled.length -1 ==j )
          {
            temp[i].defaultValue=JSON.parse(`[${result.toString()}]`);
            console.log(temp[i].defaultValue)
          }
        }
        console.log(temp.length-1==i && list_to_update.length -1==k)
        if(temp.length-1==i && list_to_update.length -1==k ){
          console.log('im setting state')
          this.setState({settings : settings});
        }
      }
    }
  }
}

  componentDidMount() {
    // get real values using axios (bypass login re-using func {applyConstraints})
    
    axios.post('/managementLayer/GetGroupSettings')
      .then((response)=>{
        console.log("the group settings  is ", response.data.body.setting)
        console.log("ths this now is",this )
        //self.setState({filter : response.data});
        console.log("**************setState- will take place*****************")

        this.update(response.data.body.setting) 
      })
      .catch(err => {
        console.log("there is groupsettings response  error");
        console.log(err);
      });
  

    var elems = Array.prototype.slice.call(document.querySelectorAll('.js-switch'));

    elems.forEach(function(html) {
      var switchery = new Switchery(html, { color: '#FA824F' });;
      switchery.ar
    });
  }

  render() {
    console.log("modified this.sliAttrs.defult",this.sliAttrs)
      return (
        <div>
          <Row>
            <Col sm={12}>
              <PanelContainer>
                <PanelHeader className='bg-orange65 fg-white'>
                  <Grid>
                    <Row>
                      <Col xs={12}>
                        <h3>Configure Smart ADAS Features</h3>
                      </Col>
                    </Row>
                  </Grid>
                </PanelHeader>
                <PanelBody>
                  <Grid>
                    <Row>
                      <Col xs={12}>
                      <div>       
                      <Grid>
                        <Row>
                          <Col xs={12} style={{padding: 25}}>
                            <Form>
                              <FormGroup>
                              <InputGroup>
                                  <FormControl type='text' placeholder='Search feature...' className='border-orange border-focus-darkorange'/>
                                  <InputGroup.Button>
                                    <Button bsStyle='orange'>
                                      <Icon glyph='icon-fontello-search'/>
                                    </Button>
                                  </InputGroup.Button>
                                </InputGroup>
                              </FormGroup>
                            </Form>
                            <div>
                              <Table collapsed>
                                <tbody>
                                  <AlertType name='HMW' avatar='avatar1' invited noBorder attrs = { this. hmwAttrs}/>
                                  <AlertType name='LDW' avatar='avatar1' invited attrs={this.ldwAttrs} />
                                  <AlertType name='BR' avatar='avatar1' invited  attrs={this.brAttrs} />
                                  <AlertType name='SLI' avatar='avatar1' invited  attrs={this.sliAttrs}/>
                                </tbody>
                              </Table>
                            </div>
                          </Col>
                        </Row>
                      </Grid>               
                      </div>
                      </Col>
                    </Row>
                  </Grid>
                </PanelBody>
              </PanelContainer>
            </Col>
          </Row>
        </div>
      );
  }
}
