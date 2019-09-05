import React from 'react';
import DatetimeRangePicker from 'react-bootstrap-datetimerangepicker';
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
  import Cookies from 'universal-cookie';
  import PropTypes from 'prop-types'
  import { Link, withRouter } from 'react-router';
  @withRouter
class PredefinedRanges extends React.Component {
    static propTypes = {
        //match: PropTypes.object.isRequired,
        location: PropTypes.object.isRequired,
        //history: PropTypes.object.isRequired
      }
    
  constructor(props) {
    super(props);

    this.state = {
        startDate: moment().subtract(29, 'days'),
        endDate: moment(),
        ranges: {
          'Today': [moment(), moment()],
          'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
          'Last 7 Days': [moment().subtract(6, 'days'), moment()],
          'Last 30 Days': [moment().subtract(29, 'days'), moment()],
          'This Month': [moment().startOf('month'), moment().endOf('month')],
          'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
        },
      };

    const cookies = new Cookies();
    var state =cookies.get('OnlineFilterTimeRange');
    if(state)
    {
        console.log(state.start);
        this.state.startDate= moment(state.start);
        this.state.endDate= moment(state.end);
    }
  



    this.handleEvent = this.handleEvent.bind(this);


  }

  handleEvent(event, picker) {

    const cookies = new Cookies();
    cookies.remove('OnlineFilterTimeRange', { path: '/' });
    console.log(new Date (moment().endOf('day').valueOf()))
    cookies.set('OnlineFilterTimeRange',{tableName:"time",start:picker.startDate,end:picker.endDate}, { path: '/',expires:new Date (moment().endOf('day').valueOf())});
    this.setState({
      startDate: picker.startDate,
      endDate: picker.endDate,
    });
    this.props.router.push(this.props.router.location.pathname);
  }

  render() {

    let start = this.state.startDate.format('MMMM D, YYYY');
    let end = this.state.endDate.format('MMMM D, YYYY');
  
    let label = start + ' - ' + end;
    if (start === end) {
      label = start;
    }

    return (
      <div style={this.buttonStyle}  className="form-group">
          <DatetimeRangePicker
            startDate={this.state.startDate}
            endDate={this.state.endDate}
            ranges={this.state.ranges}
            onEvent={this.handleEvent}
            timePicker= {true}
          >
          
         <div className="Select-control"> 
            <span className="Select-multi-value-wrapper" >
                <div className="Select-placeholder">{label}</div>
            </span>
            <span className="Select-arrow-zone">
                <span className="Select-arrow" ></span>
            </span>
         </div>
            {/* <Button bsStyle='primary' className="Select-control" style={buttonStyle}></Button> */}
          </DatetimeRangePicker>
        </div>

    );
  }

}

export default PredefinedRanges;