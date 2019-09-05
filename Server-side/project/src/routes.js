import React from 'react';
import classNames from 'classnames';
import { IndexRoute, Route } from 'react-router';

import { Grid, Row, Col, MainContainer } from '@sketchpixy/rubix';

/* Common Components */

import Sidebar from './common/sidebar.jsx';
import Header from './common/header.jsx';
import Footer from './common/footer.jsx';

/* Pages */

import Homepage from './routes/Homepage.jsx';

import Dashboard from './routes/Dashboard.jsx';

import Inbox from './routes/Inbox.jsx';
import Mail from './routes/Mail.jsx';
import Compose from './routes/Compose.jsx';

import Gallery from './routes/Gallery.jsx';

import Social from './routes/Social.jsx';

import Posts from './routes/Posts.jsx';
import Post from './routes/Post.jsx';

import Panels from './routes/Panels.jsx';

import LineSeries from './routes/LineSeries.jsx';
import AreaSeries from './routes/AreaSeries.jsx';
import BarColSeries from './routes/BarColSeries.jsx';
import MixedSeries from './routes/MixedSeries.jsx';
import PieDonutSeries from './routes/PieDonutSeries.jsx';

import Chartjs from './routes/Chartjs.jsx';
import C3js from './routes/C3js.jsx';
import Morrisjs from './routes/Morrisjs.jsx';

import StaticTimeline from './routes/StaticTimeline.jsx';
import InteractiveAlertConfiguration from './routes/InteractiveAlertConfiguration.jsx';

import Codemirrorjs from './routes/Codemirrorjs.jsx';
import Maps from './routes/Maps.jsx';
import RealTimeMaps from './routes/RealTimeMaps.jsx';
import Editor from './routes/Editor.jsx';

import Buttons from './routes/Buttons.jsx';
import Dropdowns from './routes/Dropdowns.jsx';
import TabsAndNavs from './routes/TabsAndNavs.jsx';
import Sliders from './routes/Sliders.jsx';
import Knobs from './routes/Knobs.jsx';
import Modals from './routes/Modals.jsx';
import Messengerjs from './routes/Messengerjs.jsx';

import Controls from './routes/Controls.jsx';
import XEditable from './routes/XEditable.jsx';
import Wizard from './routes/Wizard.jsx';

import Tables from './routes/Tables.jsx';
import Datatablesjs from './routes/Datatablesjs.jsx';
import Tablesawjs from './routes/Tablesawjs.jsx';

import Grids from './routes/Grids.jsx';
import Calendar from './routes/Calendar.jsx';

import Dropzonejs from './routes/Dropzonejs.jsx';
import Cropjs from './routes/Cropjs.jsx';

import Fonts from './routes/Fonts.jsx';

import Login from './routes/Login.jsx';
import Signup from './routes/Signup.jsx';
import Invoice from './routes/Invoice.jsx';
import Pricing from './routes/Pricing.jsx';
import RouteMaps from './routes/RouteMaps.jsx';
import Lock from './routes/Lock.jsx';
import Geofencing from './routes/geofencing.jsx'
// set the initial filter.


class XeditProxy extends React.Component {
  constructor( props ) {
    super( props );
    console.log(props.kaki)
    this.state = {};
  }
  filter={
    Streamtype:"Sadas",
    
    Fromdate:"",
    
    Todate:"",
    
    alertstype:"",
    
    FMSunitIDs:"",
    
    country:"Israel"
  }
  render() {
    console.log("the kaki is ", this.dvir)
    return (
      <XEditable kaki={this.props.kaki}/>
    );
  }
}

class App extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {};
  }

  shouldUpdate=true;
  remotlyrender=(onlineFilterObjects)=>{
    this.shouldUpdate=false;
    console.log("global state has been placed");
    //re-render other component
    this.setState({onlineFilterObjects});
  }
  render() {
    return (
      <MainContainer {...this.props}>

        <Sidebar Filter={this.filter}/>
           {/*  remote ={this.remotlyrender} shouldUpdate ={this.shouldUpdate}/> */}
           <Header/>
        <div id='body'>
        <Grid>
          <Row>
            <Col xs={12} >
            {this.props.children}
            {/* {this.renderChildren(this.props)}  */}
            </Col>
          </Row>
        </Grid>
      </div>
          
        <Footer />
 
      </MainContainer>
    );
  }
}

/**
 * Includes Sidebar, Header and Footer.
 */
const routes = (
    <Route component={App}>
      <Route path='dashboard' component={Dashboard} />
      <Route path='RealTimeMaps' component={RealTimeMaps} />     
      <Route path='mailbox/inbox' component={Inbox} />
      <Route path='mailbox/mail' component={Mail} />
      <Route path='mailbox/compose' component={Compose} />
      <Route path='gallery' component={Gallery} />
      <Route path='social' component={Social} />
      <Route path='blog/posts' component={Posts} />
      <Route path='blog/post' component={Post} />
      <Route path='panels' component={Panels} />
      <Route path='RouteMaps' component={RouteMaps} />  
      <Route path='charts/rubix/line' component={LineSeries} />
      <Route path='charts/rubix/area' component={AreaSeries} />
      <Route path='charts/rubix/barcol' component={BarColSeries} />
      <Route path='charts/rubix/mixed' component={MixedSeries} />
      <Route path='charts/rubix/piedonut' component={PieDonutSeries} />
      <Route path='charts/chartjs' component={Chartjs} />
      <Route path='charts/c3js' component={C3js} />
      <Route path='charts/morrisjs' component={Morrisjs} />
      <Route path='timeline' component={StaticTimeline} />
      <Route path='interactive-alerts-configuration' component={InteractiveAlertConfiguration} />
      <Route path='codemirror' component={Codemirrorjs} />
      <Route path='maps' component={Maps} />
      <Route path='editor' component={Editor} />
      <Route path='ui-elements/buttons' component={Buttons} />
      <Route path='ui-elements/dropdowns' component={Dropdowns} />
      <Route path='ui-elements/tabs-and-navs' component={TabsAndNavs} />
      <Route path='ui-elements/sliders' component={Sliders} />
      <Route path='ui-elements/knobs' component={Knobs} />
      <Route path='ui-elements/modals' component={Modals} />
      <Route path='ui-elements/messenger' component={Messengerjs} />
      <Route path='forms/controls' component={Controls}/>
      <Route path='forms/wizard' component={Wizard} />
      <Route path='forms/x-editable' component={XEditable} />
      <Route path='tables/bootstrap-tables' component={Tables} />
      <Route path='tables/datatables' component={Datatablesjs} />
      <Route path='tables/tablesaw' component={Tablesawjs} />
      <Route path='grid' component={Grids} />
      <Route path='calendar' component={Calendar} />
      <Route path='file-utilities/dropzone' component={Dropzonejs} />
      <Route path='file-utilities/crop' component={Cropjs} />
      <Route path='fonts' component={Fonts} />
      <Route path='invoice' component={Invoice} />
      <Route path='pricing' component={Pricing} />
      <Route path='Geofencing' component={Geofencing}/>
  </Route>
);

/**
 * No Sidebar, Header or Footer. Only the Body is rendered.
 */
const basicRoutes = (
  <Route>
    <Route path='lock' component={Lock} />
    <Route path='login' component={Login} />
    <Route path='signup' component={Signup} />
  </Route>
);

const combinedRoutes = (
  <Route>
    <Route>
      {routes}
    </Route>
    <Route>
      {basicRoutes}
    </Route>
  </Route>
);



export default (  
  <Route>
    <Route path='/' component={Login} />
    <Route path='/ltr'>
      {combinedRoutes}
    </Route>
    <Route path='/rtl'>
      {combinedRoutes}
    </Route>
  </Route>

);
