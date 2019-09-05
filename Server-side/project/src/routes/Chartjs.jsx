import React from 'react';
import SuccessMessage from './SuccessMessage.jsx';
import FailureMessage from './FailureMessage.jsx';
import { Panel, Form, Button, PanelContainer, InputGroup, FormGroup, FormControl, Icon, Grid, Row, Col, PanelBody } from "@sketchpixy/rubix";
import Axios from 'axios';
import * as config from '../../config/config';
import Cookie from 'universal-cookie';

export default class Chartjs extends React.Component {

  constructor(props) {
    super(props);
    this.cookie = new Cookie();
    this.categoryMap = {
      'Fruits & Veggies': 'FRUITS AND VEGS',
      'Meat': 'MEAT',
      'Dairy': 'DAIRY',
      'Other': 'OTHER'
    };
    this.state = {
      token: this.cookie.get('token'),
      product_name: null,
      price: null,
      category: null,
      success: null,
      failureMsg: null,
      file: null,
      lastProductId: null,
      catEnum: ['Choose category...', 'Fruits & Veggies', 'Meat', 'Dairy', 'Other']
    };
  }

  onFormValChange = (fieldName, event) => {
    switch (fieldName) {
      case 'product_name':
        this.setState({ product_name: event.target.value })
        break;

      case 'price':
        this.setState({ price: event.target.value })
        break;

      case 'category':
        this.setState({ category: event.target.value })
        break;

      case 'file':
        this.setState({ file: event.target.files[0] });
        break;

      default:
        break;
    }
  };

  async postFormToBackend(e) {
    e.preventDefault();
    e.stopPropagation();

    const body = {
      product_name: this.state.product_name,
      category: this.categoryMap[this.state.category],
      price: this.state.price
    };

    let myHeaders = {
      'Content-Type': 'application/json',
      'Authorization': this.state.token
    };

    try {
      console.log(JSON.stringify(body));
      const response = await Axios.post(config.default.WEB_ADD_PRODUCT, body, { headers: myHeaders });
      this.setState({ lastProductId: response.data.id, success: true }, () => {
        this.addPhotoToProduct();
        setTimeout(() => {
          this.setState({ success: null });
        }, 5000);
      });
    }
    catch (err) {
      let errors = err.response.data.map(err => (err.msg + "\n"));
      let msg = "";
      errors.forEach(errorr => { msg += errorr });
      this.setState({ success: false, failureMsg: msg });
    }
  }

  addPhotoToProduct = async () => {

    let myHeaders = {
      'Authorization': this.state.token,
      'Content-Type': 'multipart/form-data'
    };
    let formData = new FormData();
    formData.append('avatar', this.state.file)
    Axios.post(`${config.default.WEB_UPLOAD_IMAGE}/${this.state.lastProductId}`, formData, { headers: myHeaders });
  };

  dropChanged = (value) => {
    this.setState({ category: value })
  };

  render() {
    return (
      <PanelContainer>
        <Panel>
          <h1 className='center center-block'> Add A New Product </h1>
          <br />
          <br />
          <Grid>
            <Row>
              <Col sm={4} smOffset={4} xs={10} xsOffset={1} collapseLeft collapseRight>
                <PanelContainer controls={false}>
                  <Panel>
                    <PanelBody style={{ padding: 0 }}>
                      <div className='text-center bg-info fg-white'>
                        <h3 style={{ margin: 0, padding: 25 }}>Add A Product</h3>
                      </div>
                      <div>
                        <div style={{ padding: 25, paddingTop: 0, paddingBottom: 0, margin: 'auto', marginBottom: 25, marginTop: 25 }}>
                          <Form onSubmit={::this.postFormToBackend}>
                          <FormGroup controlId='product_name'>
                            <InputGroup bsSize='large'>
                              <InputGroup.Addon>
                                <Icon glyph='icon-fontello-basket-alt' />
                              </InputGroup.Addon>
                              <FormControl autoFocus type='text' onChange={(event) => this.onFormValChange('product_name', event)} className='border-focus-green' placeholder='Product Name' />
                            </InputGroup>
                          </FormGroup>
                          <FormGroup controlId='price'>
                            <InputGroup bsSize='large'>
                              <InputGroup.Addon>
                                <Icon glyph='icon-fontello-dollar' />
                              </InputGroup.Addon>
                              <FormControl autoFocus type='text' onChange={(event) => this.onFormValChange('price', event)} className='border-focus-green' placeholder='Price' />
                            </InputGroup>
                          </FormGroup>
                          <FormGroup controlId="formControlsSelect">
                            <InputGroup bsSize='large'>
                              <InputGroup.Addon>
                                <Icon glyph='icon-fontello-tag' />
                              </InputGroup.Addon>
                              <FormControl componentClass='select' placeholder='Choose Category...' className='border-focus-green' onChange={(event) => this.dropChanged(event.target.value)}>
                                {this.state.catEnum.map(e => (
                                  <option key={e} value={e}>{e}</option>
                                ))}
                              </FormControl>
                            </InputGroup>
                          </FormGroup>
                          <FormGroup controlId='image'>
                            <InputGroup bsSize='large'>
                              <InputGroup.Addon>
                                <Icon glyph='icon-fontello-folder-open' />
                              </InputGroup.Addon>
                              <FormControl autoFocus type='file' onChange={(event) => this.onFormValChange('file', event)} className='border-focus-green' placeholder='Category' />
                            </InputGroup>
                          </FormGroup>
                          <FormGroup>
                            <Grid>
                              <Row>
                                <Col xs={12} collapseLeft collapseRight>
                                  <Button type='submit' outlined lg bsStyle='blue' block onClick={::this.postFormToBackend}>Create A New Product</Button>
                                </Col>
                              </Row>
                            </Grid>
                          </FormGroup>
                        </Form>
                      </div>
                    </div>
                  <SuccessMessage show={this.state.success} msg={`Product "${this.state.product_name}" has been added with price of: ${this.state.price}`}></SuccessMessage>
                  <FailureMessage show={this.state.success} msg={this.state.failureMsg}></FailureMessage>
                  </PanelBody>
                </Panel>
              </PanelContainer>
            </Col>
          </Row>
        </Grid>
        </Panel >
        </PanelContainer >);
  }
};  