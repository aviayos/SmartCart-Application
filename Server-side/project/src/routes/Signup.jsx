import React from 'react';
import axios from 'axios';
import { Link, withRouter } from 'react-router';
import SuccessMessage from './SuccessMessage.jsx';
import FailureMessage from './FailureMessage.jsx';
import * as config  from '../../config/config';
import {
  Row,
  Col,
  Icon,
  Grid,
  Form,
  Badge,
  Panel,
  Button,
  PanelBody,
  FormGroup,
  LoremIpsum,
  InputGroup,
  FormControl,
  ButtonGroup,
  ButtonToolbar,
  PanelContainer,
} from '@sketchpixy/rubix';
import { nullLiteral } from 'babel-types';

@withRouter
export default class Signup extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      first_name: null,
      last_name: null,
      business_name: null,
      business_address: null,
      email: null,
      password: null,
      success: null,
      failureMsg: null,
      phone: null
    };
  }

  async postFormToBackend(e) {
    e.preventDefault();
    e.stopPropagation();

    const body = {
      first_name: this.state.first_name,
      last_name: this.state.last_name,
      business_name: this.state.business_name,
      business_address: this.state.business_address,
      email: this.state.email,
      password: this.state.password,
      phone: this.state.phone
    };

    const myHeader = { "Contet-Type": "application/json" };


    axios.post(config.default.WEB_SIGNUP, body, { headers: myHeader })
    .then(response => {
      this.setState({ success: true }, () => {
        setTimeout(() => {
          this.setState({ success: true });
          this.props.router.goBack();
        }, 5000);
      });
    })
    .catch(AxiosError => {
      let errors = AxiosError.response.data.map(error => (error.msg + "\n"));
      let msg = "";
      errors.forEach(err => { msg += err });
      this.setState({ success: false, failureMsg: msg });
    });
  };

  back(e) {
    e.preventDefault();
    e.stopPropagation();
    this.props.router.goBack();
  }

  componentDidMount() {
    $('html').addClass('authentication');
  }

  componentWillUnmount() {
    $('html').removeClass('authentication');
  }

  getPath(path) {
    var dir = this.props.location.pathname.search('rtl') !== -1 ? 'rtl' : 'ltr';
    path = `/${dir}/${path}`;
    return path;
  }

  onFormValChange = (fieldName, event) => {
    switch (fieldName) {
      case 'first_name':
        this.setState({first_name: event.target.value})
        break;

      case 'last_name':
        this.setState({last_name: event.target.value})
        break;

      case 'business_name':
        this.setState({business_name: event.target.value})
        break;
      
      case 'business-address':
        this.setState({business_address: event.target.value})
        break;
      
      case 'email':
        this.setState({email: event.target.value})
        break;

      case 'password':
        this.setState({password: event.target.value})
        break;
      
      case 'phone':
        this.setState({ phone: event.target.value })
        break;
    
      default:
        break;
    }
  };

  render() {
    return (
      <div id='auth-container' className='login'>
        <div id='auth-row'>
          <div id='auth-cell'>
            <Grid>
              <Row>
                <Col sm={4} smOffset={4} xs={10} xsOffset={1} collapseLeft collapseRight>
                  <PanelContainer controls={false}>
                    <Panel>
                      <PanelBody style={{padding: 0}}>
                        <div className='text-center bg-darkblue fg-white'>
                          <h3 style={{margin: 0, padding: 25}}>Sign up</h3>
                        </div>
                        <div>
                          <div style={{padding: 25, paddingTop: 0, paddingBottom: 0, margin: 'auto', marginBottom: 25, marginTop: 25}}>
                            <Form onSubmit={::this.postFormToBackend}>
                              <FormGroup controlId='firstname'>
                                <InputGroup bsSize='large'>
                                  <InputGroup.Addon>
                                    <Icon glyph='icon-fontello-user' />
                                  </InputGroup.Addon>
                                  <FormControl autoFocus onChange={(event) => this.onFormValChange('first_name', event)} type='text' className='border-focus-blue' placeholder='First Name' />
                                </InputGroup>
                              </FormGroup>
                              <FormGroup controlId='lastname'>
                                <InputGroup bsSize='large'>
                                  <InputGroup.Addon>
                                    <Icon glyph='icon-fontello-user' />
                                  </InputGroup.Addon>
                                  <FormControl autoFocus type='text' onChange={(event) => this.onFormValChange('last_name', event)} type='text' className='border-focus-blue' placeholder='Last Name' />
                                </InputGroup>
                              </FormGroup>
                              <FormGroup controlId='business-name'>
                                <InputGroup bsSize='large'>
                                  <InputGroup.Addon>
                                    <Icon glyph='icon-fontello-suitcase' />
                                  </InputGroup.Addon>
                                  <FormControl autoFocus type='text' onChange={(event) => this.onFormValChange('business_name', event)} className='border-focus-blue' placeholder='Business Name' />
                                </InputGroup>
                              </FormGroup>
                              <FormGroup controlId='business-address'>
                                <InputGroup bsSize='large'>
                                  <InputGroup.Addon>
                                    <Icon glyph='icon-fontello-basket' />
                                  </InputGroup.Addon>
                                  <FormControl autoFocus type='text' onChange={(event) => this.onFormValChange('business-address', event)} className='border-focus-blue' placeholder='Business Address' />
                                </InputGroup>
                              </FormGroup>
                              <FormGroup controlId='phone'>
                                <InputGroup bsSize='large'>
                                  <InputGroup.Addon>
                                    <Icon glyph='icon-fontello-phone-outline' />
                                  </InputGroup.Addon>
                                  <FormControl type='phone' onChange={(event) => this.onFormValChange('phone', event)} className='border-focus-blue' placeholder='Number Phone' />
                                </InputGroup>
                              </FormGroup>
                              <FormGroup controlId='emailaddress'>
                                <InputGroup bsSize='large'>
                                  <InputGroup.Addon>
                                    <Icon glyph='icon-fontello-mail' />
                                  </InputGroup.Addon>
                                  <FormControl type='email' onChange={(event) => this.onFormValChange('email', event)} className='border-focus-blue' placeholder='Valid Email' />
                                </InputGroup>
                              </FormGroup>
                              <FormGroup controlId='password'>
                                <InputGroup bsSize='large'>
                                  <InputGroup.Addon>
                                    <Icon glyph='icon-fontello-key' />
                                  </InputGroup.Addon>
                                  <FormControl type='password' onChange={(event) => this.onFormValChange('password', event)} className='border-focus-blue' placeholder='Password' />
                                </InputGroup>
                              </FormGroup>
                              <FormGroup>
                                <Grid>
                                  <Row>
                                    <Col xs={12} collapseLeft collapseRight>
                                      <Button type='submit' outlined lg bsStyle='blue' block onClick={::this.postFormToBackend}>Create account</Button>
                                    </Col>
                                  </Row>
                                </Grid>
                              </FormGroup>
                            </Form>
                          </div>
                          <div className='bg-hoverblue fg-black50 text-center' style={{padding: 25, paddingTop: 12.5}}>
                            <div style={{marginBottom: 12.5}}>SIGN UP WITH</div>
                            <Grid>
                              <Row>
                                <Col xs={12} sm={6} smCollapseRight>
                                  <Button block type='submit' id='facebook-btn' lg bsStyle='darkblue' onClick={::this.back}>
                                    <Icon glyph='icon-fontello-facebook' />
                                    <span>     Facebook</span>
                                  </Button>
                                  <br className='visible-xs' />
                                </Col>
                                <Col xs={12} sm={6}>
                                  <Button block type='submit' id='twitter-btn' lg bsStyle='darkblue' onClick={::this.back}>
                                    <Icon glyph='icon-fontello-twitter' />
                                    <span>     Twitter</span>
                                  </Button>
                                </Col>
                              </Row>
                            </Grid>
                            <div style={{marginTop: 25}}>
                              Already have an account? <Link to={::this.getPath('login')}>Login</Link>
                            </div>
                          </div>
                        </div>
                        <SuccessMessage show={this.state.success} msg={'Thanks for signing up, rediercting...'}></SuccessMessage>
                        <FailureMessage show={this.state.success} msg={this.state.failureMsg}></FailureMessage>
                      </PanelBody>
                    </Panel>
                  </PanelContainer>
                </Col>
              </Row>
            </Grid>
          </div>
        </div>
      </div>
    );
  }
}
