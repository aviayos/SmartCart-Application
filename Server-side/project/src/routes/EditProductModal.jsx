import Modal from '@sketchpixy/rubix/lib/Modal';
import Button from '@sketchpixy/rubix/lib/Button';
import React from 'react';
import config from '../../config/config';
import FormControl from '@sketchpixy/rubix/lib/FormControl';;
import Axios from 'axios';
import Cookies from 'universal-cookie';

export default class EditProductModal extends React.Component {

	constructor(props) {
            super(props);
            this.cookie = new Cookies();
		this.state =  {
			newPrice: null,
			successMsg: null,
                  errMsg: null,
                  token: this.cookie.get('token'),
		};
	};

	changePrice = async () => {
            const body = {
                  productid: this.props.id,
                  newPrice: this.state.newPrice
            };
            const headers = { Authorization: this.state.token }

		try {
                  const edit = await Axios.put(config.WEB_UPDATE_PROD, body, { headers: headers });
                  
			this.setState({successMsg: 'Product price changed!'} ,() => setTimeout(() => {
				this.props.changeModal();
				this.props.refresh();
				this.setState({successMsg});
			}, 5000));
		}
		catch(err) {
			this.setState({errMsg: err.response.data[0].msg.concat('\n')});
		}
		
      };
      

	render() {
		if(!this.props.show) return null;
		return(
			<Modal.Dialog style={{paddingTop: 200}}>

				<Modal.Header>
					<Modal.Title><strong>{`${this.props.title}: ${this.props.name}`}</strong></Modal.Title>
				</Modal.Header>
			
				<Modal.Body>
					<FormControl type='text' placeholder='Enter new price...' onChange={(event) => this.setState({newPrice: event.target.value})}></FormControl>
				</Modal.Body>
			
				<Modal.Footer>
                              <strong style={{paddingRight: 200}}> {this.state.successMsg} </strong>
                              <br/>
                              <strong style={{paddingRight: 500, color: 'red'}}> {this.state.errMsg} </strong>
                              <br/>
					<Button bsStyle='danger' onClick={this.props.changeModal}>Close</Button>
					<Button bsStyle='success' onClick={this.changePrice}>Change</Button>
				</Modal.Footer>
		
			</Modal.Dialog>
		);
	}
};

String.prototype.replaceAt = function(index, replacement) {
	return this.substr(0, index) + replacement+ this.substr(index + replacement.length);
  }