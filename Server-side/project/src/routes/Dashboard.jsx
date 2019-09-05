
import React from 'react';
import {
  Row,
  Tab,
  Col,
  Icon,
  Grid,
  Table,
  Label,
  Panel,
  Button,
  PanelBody,
  LoremIpsum,
  PanelRight,
  PanelHeader,
  PanelContainer,
  PanelTabContainer,
} from '@sketchpixy/rubix';
import axios from 'axios';
import config from '../../config/config';
import Cookies from 'universal-cookie';
import MapsPanel from './MapsPanel.jsx';
import LogoPanel from './LogoPanel.jsx';

class Contact extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      invited: this.props.invited ? true : false,
      invitedText: this.props.invited ? 'configured' : 'configure'
    };
  }
  handleClick(e) {
    e.preventDefault();
    e.stopPropagation();
    this.setState({
      invited: !this.state.invited,
      invitedText: (!this.state.invited) ? 'configured' : 'configure'
    });
  }
  render() {
    return (
      <tr>
        <td style={{verticalAlign: 'middle', borderTop: this.props.noBorder ? 'none': null}}>
          <img src={`/imgs/app/avatars/${this.props.avatar}.png`} />
        </td>
        <td style={{verticalAlign: 'middle', borderTop: this.props.noBorder ? 'none': null}}>
          {this.props.name}
        </td>
        <td style={{verticalAlign: 'middle', borderTop: this.props.noBorder ? 'none': null}} className='text-right'>
          <Button onlyOnHover bsStyle='orange' active={this.state.invited} onClick={this.handleClick}>
            {this.state.invitedText}
          </Button>
        </td>
      </tr>
    );
  }
}

class MainChart extends React.Component {
  constructor(props) {
    super(props);
    this.cookie = new Cookies();
    this.state = {
      token: this.cookie.get('token'),
      userData: {
        first_name: null,
        last_name: null,
        business_name: null
      },
      imgSrc: 'https://checkout.advancedshippingmanager.com/wp-content/uploads/2015/10/Cart-Icon-PNG-Graphic-Cave-e1461785088730-300x300.png',
      backGrnd: 'https://gracehealthsystem.com/wp-content/uploads/2017/09/GC-090717-Fruit-Veggies-FB.jpg'
    };
 }
  async componentDidMount() { 
    const header = { 'Authorization': this.state.token };
    const user = await axios.get(`${config.WEB_GET_USER}/${this.state.token}`, { headers : header });
    this.setState({userData: user.data});
  };
  
  render() { 
    return ( 
      <div style={{backgroundImage: `url(${this.state.backGrnd})`}}>
      <PanelBody>
        <h1 className='center center-block' style={{opacity: 1, color: 'white', fontWeight: 'bold', textShadow: 'black -3px 3px' , backgroundColor: 'rgba(0, 0, 0, 0.3)', fontSize: '150px'}}> {this.state.userData.business_name} </h1>
        <h2 className='center center-block' style={{opacity: 1, color: 'white', fontWeight: 'bold', textShadow: 'black -3px 3px' , backgroundColor: 'rgba(0, 0, 0, 0.3)', fontSize: '60px'}}> Welcome to Smart Cart Dashboard! </h2>
        <div id='main-chart'></div>
      </PanelBody>
      </div>
    )
  }
}

class RadarChartPanel extends React.Component {
  componentDidMount() {
    var data = {
        labels: ['FRUITS', 'VEGTABLES', 'MEAT', 'DAIRY', 'OTHERS'],
      datasets: [{
        label: 'My First dataset',
        fillColor: 'rgba(220,220,220,0.2)',
        strokeColor: 'rgba(220,220,220,1)',
        pointColor: 'rgba(220,220,220,1)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(220,220,220,1)',
        data: [65, 59, 90, 81, 56]
      }, {
        label: 'My Second dataset',
        fillColor: 'rgba(234, 120, 130, 0.5)',
        strokeColor: 'rgba(234, 120, 130, 1)',
        pointColor: 'rgba(234, 120, 130, 1)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(151,187,205,1)',
        data: [28, 48, 40, 19, 96]
      }]
    };

    var ctx = document.getElementById('chartjs-1').getContext('2d');
    new Chart(ctx).Radar(data, {
      responsive: false,
      maintainAspectRatio: true
    });

    $('.line-EA7882').sparkline('html', { type: 'line', height: 25, lineColor: '#EA7882', fillColor: 'rgba(234, 120, 130, 0.5)', sparkBarColor: '#EA7882' });
    $('.line-2EB398').sparkline('html', { type: 'line', height: 25, lineColor: '#2EB398', fillColor: 'rgba(46, 179, 152, 0.5)', sparkBarColor: '#2EB398' });
    $('.line-79B0EC').sparkline('html', { type: 'line', height: 25, lineColor: '#79B0EC', fillColor: 'rgba(121, 176, 236, 0.5)', sparkBarColor: '#79B0EC' });
    $('.line-FFC497').sparkline('html', { type: 'line', height: 25, lineColor: '#FFC497', fillColor: 'rgba(255, 196, 151, 0.5)', sparkBarColor: '#FFC497' });
  }
  render() {
    return (
      <div>
        <h1 style={{color: '#4d5d53', fontStyle: 'oblique'}}>Last Month Distribution</h1>
        <canvas id='chartjs-1' height='350' width='350'></canvas>
        <Table striped collapsed>
          <tbody>
            <tr>
                        <td className='text-left'>OTHERS</td>
              <td className='text-center'>
                <Label className='bg-red fg-white'>+46%</Label>
              </td>
              <td className='text-right'>
                <div className='line-EA7882'>2,3,7,5,4,4,3,2,3,4,3,2,4,3,4,3,2,5</div>
              </td>
            </tr>
            <tr>
                        <td className='text-left'>MEAT</td>
              <td className='text-center'>
                <Label className='bg-darkgreen45 fg-white'>+23%</Label>
              </td>
              <td className='text-right'>
                <div className='line-2EB398'>7,7,7,7,7,7,6,7,4,7,7,7,7,5,7,7,7,9</div>
              </td>
            </tr>
            <tr>
                        <td className='text-left'>DAIRY</td>
              <td className='text-center'>
                <Label className='bg-blue fg-white'>43,000 (+50%)</Label>
              </td>
              <td className='text-right'>
                <div className='line-79B0EC'>4,6,7,7,4,3,2,1,4,9,3,2,3,5,2,4,3,1</div>
              </td>
            </tr>
            <tr>
              <td className='text-left'>VEGTABLES</td>
              <td className='text-center'>
                <Label className='bg-orange fg-white'>2000 (+75%)</Label>
              </td>
              <td className='text-right'>
                <div className='line-FFC497'>3,2,4,6,7,4,5,7,4,3,2,1,4,6,7,8,2,8</div>
              </td>
            </tr>
          </tbody>
        </Table>
      </div>
    );
  }
}


class WeatherPanel extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      Humidity: null,
      Max: null,
      Min: null,
      Wind: null,
      Text: null,
      Icon: null
    };
  }
  async componentDidMount() {
    $('#datetimepicker1-parent').datetimepicker({
      inline: true,
    });

    const response = await axios.get('https://api.apixu.com/v1/current.json?key=fd52eb276f4a47bba47124220191506&q=Jerusalem');
    this.setState({
      Max : response.data.current.temp_c + 5,
      Min : response.data.current.temp_c,
      Wind : response.data.current.wind_kph,
      Humidity : response.data.current.humidity,
      Text : response.data.current.condition.text,
      Icon : response.data.current.condition.icon,
    });

  }
  render() {
    return (
      <PanelContainer>
        <Panel horizontal className='force-collapse'>
          <PanelBody className='panel-sm-7' style={{padding: 0}}>
            <div id='datetimepicker1-parent' className='datetimepicker-inline'></div>
          </PanelBody>
          <PanelRight className='panel-sm-5 bg-brown50 fg-white' style={{verticalAlign: 'middle'}}>
            <Grid>
              <Row>
                <Col xs={12}>
                  <div className='text-center'>
                    <Icon glyph='climacon sun' style={{fontSize: '800%', lineHeight: 0}} />
                  </div>
                </Col>
              </Row>
              <Row>
                <Col xs={6} collapseRight>
                  <h4>Max: {this.state.Max || '31'}°</h4>
                </Col>
                <Col xs={6} collapseLeft className='text-right'>
                  <h4>Min: {this.state.Min || '23'}°</h4>
                </Col>
              </Row>
              <Row>
                <Col xs={12} className='text-center'>
                  <h5>{this.state.Text || 'Sunny'}</h5>
                  <h6>Wind: {this.state.Wind || '3'} km/h | Humidity: {this.state.Humidity || '67'}%</h6>
                </Col>
              </Row>
            </Grid>
          </PanelRight>
        </Panel>
      </PanelContainer>
    );
  }
}

export default class Dashboard extends React.Component {
  constructor(props) {
    super(props);
    this.cookie = new Cookies();
    this.state = {
      token: this.cookie.get('token'),
      userData: {
        address: null,
        latlng: {
          lat: null,
          lng: null
        },
      }
    };
  };

  async componentDidMount() {
    const headers = { 'Authorization': this.state.token };
    const response = await axios.get(config.WEB_GET_LOCATION, { headers: headers });
    this.setState({ userData: response.data });
  };

  render() {
    return (
      <div className='dashboard'>
        <Row>
          <Col sm={9}>
            <PanelTabContainer id='dashboard-main' defaultActiveKey="demographics">
              <Panel>
                  <Grid>
                    <Row>
                      <Col sm={13}>
                        <MainChart />
                      </Col>
                    </Row>
                  </Grid>
              </Panel>
            </PanelTabContainer>
          </Col>
          <Col sm={3}>
            <LogoPanel width='300' height='300' className='text-center center center-block'/>
          </Col>
        </Row>

        <Row>
          <Col sm={5} collapseRight>
            <PanelContainer>
              <Panel>
                <PanelBody style={{padding: 0}}>
                  <Grid>
                    <Row>
                      <Col xs={12} className='text-center' style={{padding: 25}}>
                        <RadarChartPanel />
                      </Col>
                    </Row>
                  </Grid>
                </PanelBody>
              </Panel>
            </PanelContainer>
          </Col>
          <Col sm={7}>
            <WeatherPanel />
          </Col>
        </Row>
        <MapsPanel secName={this.state.userData.address} center={[this.state.userData.latlng.lng, this.state.userData.latlng.lat]}/>
      </div>
    );
  }
}
