import Modal from '@sketchpixy/rubix/lib/Modal';
import Button from '@sketchpixy/rubix/lib/Button';
import React from 'react';
import config from '../../config/config';
import FormControl from '@sketchpixy/rubix/lib/FormControl';;
import Axios from 'axios';

export default class ForgotPasswordModal extends React.Component {

	constructor(props) {
		super(props);
		this.state =  {
			email: null,
			successMsg1: null,
			successMsg2: null,
			errMsg: null
		};
	};

	recoverPassword = async () => {
		try {
			const forgot = await Axios.post(config.WEB_FORGOT_PASS, { email: this.state.email });
			let final;
			let msg = forgot.data.msg;
			final = msg.substring(0, msg.indexOf('Email'));
			final.replaceAt(final.length-1, '\n');
			let final2 = msg.substring(msg.indexOf('Email'), msg.length);
			this.setState({successMsg1: final, successMsg2: final2}, () => setTimeout(() => {
				this.props.changeModal();
				this.setState({successMsg1: null, successMsg2: null});
			}, 10000));
		}
		catch(err) {
			this.setState({errMsg: err.response.data[0].msg.concat('\n')}, () => setTimeout(() => {
				this.props.changeModal();
				this.setState({errMsg: null});
			}, 10000));
		}
		
	};

	render() {
		if(!this.props.show) return null;
		return(
			<Modal.Dialog>

				<Modal.Header>
					<Modal.Title><strong>{this.props.title}</strong></Modal.Title>
				</Modal.Header>
			
				<Modal.Body>
					<FormControl type='text' placeholder='Enter your email...' onChange={(event) => this.setState({email: event.target.value})}></FormControl>
				</Modal.Body>
			
				<Modal.Footer>
				<strong style={{paddingRight: 295}}> {this.state.successMsg1} </strong>
				<br/>
				<strong style={{paddingRight: 500, color: 'red'}}> {this.state.errMsg} </strong>
				<br/>
				<strong style={{paddingRight: 100}}> {this.state.successMsg2} </strong>
					<Button onClick={this.props.changeModal}>Close</Button>
					<Button bsStyle='primary' onClick={this.recoverPassword}>Recover</Button>
				</Modal.Footer>
		
			</Modal.Dialog>
		);
	}
};

String.prototype.replaceAt = function(index, replacement) {
	return this.substr(0, index) + replacement+ this.substr(index + replacement.length);
  }