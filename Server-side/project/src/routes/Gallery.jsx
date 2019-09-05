import React from 'react';
import axios from 'axios';
import config from '../../config/config';
import Cookie from 'universal-cookie';
import EditProductModal from './EditProductModal.jsx';
import DeleteProductModal from './DeleteProductModal.jsx';
import UpdatePhotoModal from './UpdatePhotoModal.jsx';

import {
  Row,
  Col,
  Icon,
  Grid,
  Panel,
  Image,
  Table,
  Button,
  PanelBody,
  PanelHeader,
  PanelContainer,
} from '@sketchpixy/rubix';

class GalleryItem extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      active: this.props.active || false,
      editModal: false,
      deleteModal: false,
      uploadModal: false
    };
  }

  changeModal = () => {
    this.setState({ editModal: false });
  };

  changeDeleteModal = () => {
    this.setState({ deleteModal: false });
  };

  changeUploadModal = () => {
    this.setState({ uploadModal: false });
  };

  render() {
    return (
      <div style={{outline: '20px black'}}>
      <PanelContainer style={{width: 250, height: 300}}>
        <Panel>
          <PanelHeader>
            <Grid className='gallery-item'>
              <Row>
                <Col xs={12} style={{padding: 12.5}}>
                    <Image src={`/image/${this.props.image}`} alt={this.props.title} width='225' height='230'/> 
                    <div className='black-wrapper text-center'>
                      <Table style={{height: '100%', width: '100%'}}>
                        <tbody>
                          <tr>
                            <td>
                            </td>
                          </tr>
                        </tbody>
                      </Table>
                    </div>
                  <div className='text-center'>
                    <h4 className='fg-darkgrayishblue75 hidden-xs' style={{textTransform: 'uppercase'}}>{this.props.title}</h4>
                    <h6 className='fg-darkgrayishblue75 visible-xs' style={{textTransform: 'uppercase'}}>{this.props.title}</h6>
                    <h5 className='fg-darkgrayishblue75 hidden-xs' style={{textTransform: 'uppercase'}}>{this.props.price}</h5>
                    <h5 className='fg-darkgray50 hidden-xs' style={{textTransform: 'uppercase'}}>{this.props.subtitle}</h5>
                    <h6 className='visible-xs' style={{textTransform: 'uppercase'}}><small className='fg-darkgray50'>{this.props.subtitle}</small></h6>
                    <Button id={this.props.image} outlined onlyOnHover bsStyle='red' className='fav-btn' active={false}>
                      <span className='counts'>{this.props.purch}</span>
                    </Button>
                    {"  "}
                    <Button outlined onlyOnHover bsStyle='green' className='fav-btn' active={true} onClick={() => this.setState({ editModal: true })}>
                      <Icon glyph='icon-fontello-edit' />
                    </Button>
                    {"  "}
                    <Button outlined onlyOnHover bsStyle='black' className='fav-btn' active={true} onClick={() => this.setState({ deleteModal: true })}>
                      <Icon glyph='icon-fontello-trash' />
                    </Button>
                    {"  "}
                    <Button outlined onlyOnHover bsStyle='blue' className='fav-btn' active={true} onClick={() => this.setState({ uploadModal: true })}>
                      <Icon glyph='icon-fontello-picture' />
                    </Button>
                    <UpdatePhotoModal refresh={this.props.forceRefresh} show={this.state.uploadModal} name={this.props.title} id={this.props.image} changeModal={this.changeUploadModal} title='Attach a photo to product'></UpdatePhotoModal>
                    <EditProductModal refresh={this.props.refresh} name={this.props.title} show={this.state.editModal} changeModal={this.changeModal} title='Edit Product Price' id={this.props.image} ></EditProductModal>
                    <DeleteProductModal refresh={this.props.refresh} id={this.props.image} name={this.props.title} show={this.state.deleteModal} changeModal={this.changeDeleteModal}> </DeleteProductModal>
                  </div>
                </Col>
              </Row>
            </Grid>
          </PanelHeader>
        </Panel>
      </PanelContainer>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
      </div>
    );
  }
}

export default class Gallery extends React.Component {

  constructor(props) {
    super(props);
    this.cookie = new Cookie();
    this.state = {
      token: this.cookie.get('token'),
      products: [],
      state: 0
    };

  }
  componentDidMount() {
    this.updateProducts();
  }

  updateProducts = async () => {
    const headers = { Authorization:  this.state.token };
    const products = await axios.get(config.WEB_GET_PRODUCTS, { headers : headers });
    this.setState({ products: products.data, state: this.state.state + 1 });
  };

  forceRefresh = () => {
    window.location.reload();
  };

  render() {
    return this.state.products.length == 0 ? <h2 className='text-center'> You don't have any products yet. please post <a href='/ltr/charts/chartjs'>here</a> </h2> : (
      <PanelContainer>
        <Panel style={{'backgroundColor': '#f1f1f1'}}>
          <Grid>
            <Row>
              {this.state.products.map(product => (
                <Col key={product.productid} xs={4} md={3} collapseRight>
                  <GalleryItem forceRefresh={this.forceRefresh} refresh={this.updateProducts} purch={product.numberOfPurchases} image={product.productid} price={'Price: ' + product.price} title={product.product_name} subtitle={product.createdon} />
                </Col>
              ))}
            </Row>
          </Grid>
        </Panel>
      </PanelContainer>
    );
  }
}