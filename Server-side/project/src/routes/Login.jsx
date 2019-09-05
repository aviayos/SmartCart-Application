import React from 'react';
import * as config  from '../../config/config';
import { Link, withRouter } from 'react-router';
import SuccessMessage from './SuccessMessage.jsx';
import FailureMessage from './FailureMessage.jsx';
import axios from 'axios';
import Cookies from 'universal-cookie';
import ForgotPasswordModal from './ForgotPasswordModal.jsx';
import LogoPanel from './LogoPanel.jsx';

import {
  Row,
  Col,
  Icon,
  Grid,
  Form,
  Panel,
  Button,
  PanelBody,
  FormGroup,
  InputGroup,
  FormControl,
  PanelContainer,
} from '@sketchpixy/rubix';

@withRouter
export default class Login extends React.Component {

  constructor(props) {
    super(props);
    this.cookie = new Cookies();
    this.state = {
      email: null,
      password: null,
      success: null,
      failureMsg: null,
      modal: false
    };
  }
  back(e) {
    e.preventDefault();
    e.stopPropagation();
    this.props.router.goBack();
  }

  componentDidMount() {
    $('html').addClass('authentication');
    this.deleteCookie('token');
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
      
      case 'email':
        this.setState({email: event.target.value})
        break;

      case 'password':
        this.setState({password: event.target.value})
        break;
    
      default:
        break;
    }
  };

  async postFormToBackend(e) {
    e.preventDefault();
    e.stopPropagation();

    this.deleteCookie('token');
    const body = {
      email: this.state.email,
      password: this.state.password
    };

    const myHeader = { "Content-Type": "application/json" };


    axios.post(config.default.WEB_LOGIN, body, { headers: myHeader })
    .then(response => {
      this.cookie.set('token', response.data.token, { maxAge: 7200000, sameSite: false });

      this.setState({ success: true }, () => {
        setTimeout(() => {
          window.location.href = this.getPath('dashboard');
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

  changeModal = () => {
    this.setState({modal: false})
  };

  /**
   * Delete a cookie.
   * Assign a cookie to a expired date.
   * @param name The cookie's name to delete.
   */
  deleteCookie = async (name) => {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    const response = await axios.delete(config.default.WEB_DEETE_COOKIE);
  };

  render() {
    return (
      <div id='auth-container' className='login'>
        <div id='auth-row'>
          <div id='auth-cell'>
            <Grid>
            <div className='center text-center center-block'>
            <LogoPanel src='/imgs/app/logo_small.png' width='300' height='300'></LogoPanel>
            </div>
              <Row>
                <Col sm={4} smOffset={4} xs={10} xsOffset={1} collapseLeft collapseRight>
                  <PanelContainer>
                    <Panel>
                      <PanelBody style={{padding: 0}}>
                        <div className='text-center bg-darkgreen fg-white'>
                          <h1 style={{margin: 0, padding: 25}}>Sign in to Smart Cart</h1>
                        </div>
                        <div className='bg-hoverblue fg-black50 text-center' style={{padding: 12.5}}>
                          <h3>You need to sign in for those awesome features</h3>
                          <div style={{marginTop: 12.5, marginBottom: 12.5}}>
                          </div>
                          <div>
                          </div>
                        </div>
                        <div>
                          <div className='text-center' style={{padding: 12.5}}>
                            <h2>Use your smart cart account</h2>
                          </div>
                          <div style={{padding: 25, paddingTop: 0, paddingBottom: 0, margin: 'auto', marginBottom: 25, marginTop: 25}}>
                            <Form onSubmit={() => { }}>
                              <FormGroup controlId='emailaddress'>
                                <InputGroup bsSize='large'>
                                  <InputGroup.Addon>
                                    <Icon glyph='icon-fontello-mail' />
                                  </InputGroup.Addon>
                                  <FormControl autoFocus type='email' onChange={(event) => this.onFormValChange('email', event)} className='border-focus-blue' placeholder='Email' />
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
                                    <Col xs={6} collapseLeft collapseRight style={{paddingTop: 10}}>
                                      <Link to={this.getPath('signup')}>Create a Smart Cart account</Link>
                                    </Col>
                                    <Col xs={6} collapseLeft collapseRight className='text-right'>
                                      <Button outlined lg type='submit' bsStyle='blue' onClick={::this.postFormToBackend}>Login</Button>
                                    </Col>
                                  </Row>
                                  <Row>
                                    <Col xs={6} collapseLeft collapseRight style={{paddingLeft: 200}} className='center-block text-center center'>
                                      <Button outlined lg type='button' bsStyle='red' onClick={() => this.setState({modal: true})}>Forgot Pasword?</Button>
                                    </Col>
                                  </Row>
                                </Grid>
                              </FormGroup>
                            </Form>
                            <SuccessMessage show={this.state.success} msg={'Thanks for signing in, rediercting...'}></SuccessMessage>
                            <FailureMessage show={this.state.success} msg={this.state.failureMsg}></FailureMessage>
                            <ForgotPasswordModal changeModal={this.changeModal} show={this.state.modal} title={'Recover your password'}></ForgotPasswordModal>
                          </div>
                        </div>
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
