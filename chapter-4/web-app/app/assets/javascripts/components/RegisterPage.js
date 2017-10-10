'use strict';

import React from 'react';
import {Form, Button, Input, Header, Message} from 'semantic-ui-react';
import queryString from 'query-string';

const loginForm = {
  width: '300px',
  margin: "auto",

};
export default class RegisterPage extends React.Component {
  constructor(){
    super();
    this.hostname = window.location.protocol + "//" + window.location.hostname + ":"+(window.location.port);
    this.state = {
      loading: false,
      loggedIn: false,
      email: null,
      password: null,
      flashMessage: ''
    }
  }

  componentDidMount(){
    let self = this;
    return fetch(this.hostname+'/api/status',{
      credentials:"same-origin"
    })
    .then(function(response) {
      if(response.status==200){
        return response.json()  
      } else {
        throw new Error(response.status)
      }
    }).then(function(body) {
      self.setState({
        loggedIn: true
      })
    }).catch(function(err){
      self.setState({
        loggedIn: false 
      })
    })
  }

  render() {
    if(this.state.loggedIn){
      window.location.href= this.hostname+'/dashboard'
    }
    let message = ""
    if( this.state.flashMessage != ''){
      message = <Message
          info
          header={this.state.flashMessage}
          content=""
        />
    }
    return (
      <div className="home">
        <div style={loginForm}>
        {message}
        <Header as='h2'> Register new Account </Header>
        <Header as='h4'> <a href="/">Old User? Login here.</a> </Header>
        <Form loading={this.state.loading}>
        <Form.Input label='Email' ref='email' onChange={this.updateEmailState.bind(this)} placeholder='test@test.com' />
        <br/>
        <Form.Field>
        <label>Password</label>
        <Input ref='password' type='password' onChange={this.updatePasswordState.bind(this)} placeholder='test'/>
        </Form.Field>       
        </Form>
        <br/>
        <Button onClick={this.sendRegisterRequest.bind(this)}>Submit</Button>
        </div>
      </div>
    );
  }

  updateEmailState(e){
    this.setState({email:e.target.value});
  }

  updatePasswordState(e){
    this.setState({password:e.target.value});
  }

  sendRegisterRequest(){
    let self = this;
    this.setState({
      loading: true
    });
    return fetch(this.hostname+"/api/register",{
      method:'POST',
      credentials:'same-origin',
      headers: {'Content-Type':'application/x-www-form-urlencoded'}, // this line is important, if this content-type is not set it wont work
      body: queryString.stringify({
        email:this.state.email,
        password:this.state.password
      })
    })
    .then(function(response) {
      if(response.status == 200){
        return response.json()  
      }
      self.setState({
        loggedIn: false,
        loading: false,
        flashMessage: "Registration failed with response code: "+response.status
      });
      throw new Error(response.status)
    }).then(function(responseObj){
      window.location.href=self.hostname+"/dashboard?tokenStr="+responseObj.message.tokenStr;
    })
  }
}
