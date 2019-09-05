import Modal from '@sketchpixy/rubix/lib/Modal';
import Button from '@sketchpixy/rubix/lib/Button';
import React from 'react';
import config from '../../config/config';
import Axios from 'axios';
import Cookies from 'universal-cookie';

export default class DeleteProductModal extends React.Component {

	constructor(props) {
            super(props);
            this.cookie = new Cookies();
		this.state =  {
			successMsg: null,
                  errMsg: null,
                  token: this.cookie.get('token'),
		};
	};

	deleteProduct = async () => {
		const options = {
			headers: {
			  "Content-Type": "application/json",
			  "Authorization": this.state.token
			},
			data: { productid: this.props.id },
		};


		try {
            const edit = await Axios.delete(config.WEB_DELETE_PROD, options);    
			this.setState({successMsg: 'Product deleted!'} ,() => setTimeout(() => {
				this.props.changeModal();
				this.props.refresh();
				this.setState({successMsg: null});
			}, 500));
		}
		catch(err) {
			this.setState({errMsg: err.response.data[0].msg.concat('\n')});
		}
		
      };
	
	closeModal = () => {
		this.props.changeModal();
		this.setState({errMsg: null, successMsg: null})
	}

	render() {
		if(!this.props.show) return null;
		return(
			<Modal.Dialog style={{paddingTop: 200}}>

				<Modal.Header>
					<Modal.Title><strong>{`Sure you want to delete ${this.props.name}?`}</strong></Modal.Title>
				</Modal.Header>
		
			
				<Modal.Footer style={{paddingRight: 210}}>
					<Button bsStyle='danger' onClick={this.closeModal}>Close</Button>
					<Button bsStyle='success' onClick={this.deleteProduct}>Delete</Button>
					<br/>
					<br/>
                    <strong className='text-center' style={{PaddingLeft: 100}}> {this.state.successMsg} </strong>
                    <br/>
                    <strong style={{paddingRight: 500, color: 'red'}}> {this.state.errMsg} </strong>
				</Modal.Footer>
		
			</Modal.Dialog>
		);
	}
};

String.prototype.replaceAt = function(index, replacement) {
	return this.substr(0, index) + replacement+ this.substr(index + replacement.length);
  }