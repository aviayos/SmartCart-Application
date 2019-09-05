import React from 'react';
import ReactDOM from 'react-dom';
import openSocket from 'socket.io-client';


import {
  Row,
  Col,
  Grid,
  Panel,
  PanelBody,
  PanelHeader,
  PanelContainer,
} from '@sketchpixy/rubix';
var socket = openSocket("localhost:9000");

class TrumbowygEditor extends React.Component {

  constructor(props) {
    super(props);

  }
  state = {
    data: []
  };
  componentDidMount() {
    console.log("subscribeToTimer")
    this.subscribeToTimer();
  }

  subscribeToTimer(cb) {
    socket.on('some event',(data)=>{
      var date= (new Date()).getTime();
      console.log(this.state.data)
      this.state.data.push((
          <p>
          <p>######################{date}##################</p>
          <pre>{data}</pre>
          <p>######################{date}##################</p>
          </p>
        ))
        
      this.setState({data:this.state.data})
    })
  }
  
  render() {
    console.log(this.state.data)
    return(
      <div style ={{'margin': '25px 50px 75px 100px',}}>
      {this.state.data}
      </div>
      )
  }
}

export default class Editor extends React.Component {
  render() {
    return (
      <div>
        <PanelContainer>
          <Panel>
            <PanelHeader>
              <Grid>
                <Row>
                  <Col xs={12}>
                    <h3 className='text-center'>SeeQ Tweets</h3>
                  </Col>
                </Row>
              </Grid>
            </PanelHeader>
            <PanelBody>
              <TrumbowygEditor />
            </PanelBody>
          </Panel>
        </PanelContainer>
      </div>
    );
  }
}
