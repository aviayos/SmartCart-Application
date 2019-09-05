import Modal from '@sketchpixy/rubix/lib/Modal';
import Button from '@sketchpixy/rubix/lib/Button';
import React from 'react';
import config from '../../config/config';
import axios from 'axios';
import Cookies from 'universal-cookie';
import { FormControl, Form } from '@sketchpixy/rubix';

export default class DeleteProductModal extends React.Component {

	constructor(props) {
            super(props);
            this.cookie = new Cookies();
		this.state =  {
			successMsg: null,
            errMsg: null,
            token: this.cookie.get('token'),
            file: null
		};
	};

	addPhotoToProduct = async () => {
		let myHeaders = { 
            'Authorization': this.state.token,
            'Content-Type': 'multipart/form-data'
        };

        let formData = new FormData();
        formData.append('avatar', this.state.file)

        try {
            const newPhoto = await axios.post(`${config.WEB_UPLOAD_IMAGE}/${this.props.id}`, formData, { headers: myHeaders });
            this.setState({successMsg: 'Picture added successfully!'} ,() => setTimeout(() => {
				this.props.changeModal();
				this.props.refresh();
				this.setState({successMsg: null});

			}, 3000));
        }
        catch(err) {
            console.error(err.data);
            this.setState({errMsg: 'Something went wrong!'});
        }
          		
    };
	
	closeModal = () => {
		this.props.changeModal();
		this.setState({errMsg: null, successMsg: null})
	};

	render() {
		if(!this.props.show) return null;
		return(
			<Modal.Dialog style={{paddingTop: 200}}>

				<Modal.Header>
					<Modal.Title><strong>{`Add a photo to ${this.props.name}?`}</strong></Modal.Title>
				</Modal.Header>
		
			
				<Modal.Footer style={{paddingRight: 210}}>
                    <Form>
                        <FormControl type='file' onChange={(event) => this.setState({ file: event.target.files[0] })}></FormControl>
                    </Form>
                    <Button bsStyle='danger' onClick={this.closeModal}>Close</Button>
                    <Button bsStyle='info' onClick={this.addPhotoToProduct}>Upload</Button>
					<br/>
					<br/>
                    <strong className='text-center' style={{PaddingLeft: 100, color: 'green'}}> {this.state.successMsg} </strong>
                    <br/>
                    <strong style={{paddingRight: 500, color: 'red'}}> {this.state.errMsg} </strong>
				</Modal.Footer>
			</Modal.Dialog>
		);
	}
};