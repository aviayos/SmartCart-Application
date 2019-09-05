import React from 'react';
import ReactDOM from 'react-dom';
import { Link, withRouter } from 'react-router';
import axios from 'axios';
import moment from 'moment'
import {
  Row,
  Col,
  Nav,
  Grid,
  Icon,
  Panel,
  Button,
  MenuItem,
  HelpBlock,
  PanelBody,
  FormGroup,
  InputGroup,
  SplitButton,
  PanelHeader,
  ButtonGroup,
  FormControl,
  PanelFooter,
  ControlLabel,
  DropdownButton,
  PanelContainer,
} from '@sketchpixy/rubix';

// @withRouter
export default class StatisticsComponent extends React.Component {

  constructor( props ) {
    super( props );
    this.state = {filter:props.Filter};
  }

  render() {
  console.log(this.state);
  return (
      <div>
        <Grid>
          <Row >
            <Col xs={12}>
              <div className='sidebar-header text-center' style={{marginBottom: 0}} ref={(c) => this._nav = c}>Data chooser</div>
              <hr style={{borderColor: 'rgba(255,255,255,0.1)', borderWidth: 2, marginTop: 12.5, marginBottom: 12.5, width: 200}} />
              <div>
                {/* <p>Stream type:  <Link to={this.props.path_to_filter}> {this.props.Filter.Streamtype}</Link> </p>
                <p>From date: <Link to={this.props.path_to_filter}> {moment(this.state.filter.FromDate).format('MMMM Do YYYY, h:mm:ss a')} </Link></p>
                <p>To date:<Link to={this.props.path_to_filter}> {moment(this.state.filter.ToDate).format('MMMM Do YYYY, h:mm:ss a')} </Link></p>
                <p>alerts type:<Link to={this.props.path_to_filter}> {this.state.filter.alertstype.toString().split(',').join(', ')}</Link></p>
                <p>FMS unit IDs: <Link to={this.props.path_to_filter}> {this.state.filter.FMSunitIDs.toString().split(',').join(', ')}</Link></p>
                <p>country:  <Link to={this.props.path_to_filter}> {this.props.Filter.country}</Link></p>*/}
                <center> 
                 <Link to ={this.props.path_to_filter}>Update Filter</Link>
                </center>
              </div>
            </Col>
          </Row> 
        </Grid>
      </div>
    );
  }
}
