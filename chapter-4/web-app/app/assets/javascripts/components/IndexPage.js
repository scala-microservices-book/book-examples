'use strict';

import React from 'react';
import {Form, Button, Input} from 'semantic-ui-react';
import queryString from 'query-string';

const loginForm = {
	width: '300px',
	margin: "auto",

};
export default class IndexPage extends React.Component {
  constructor(){
  	super();
  	this.hostname = window.location.protocol + "//" + window.location.hostname + ":"+(window.location.port);
  	this.state = {
  		loading : '',
      loggedIn:null
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
        loggedIn : true
      })
    }).catch(function(err){
      self.setState({
        loggedIn :false 
      })
    })
  }

  render() {
    if(this.state.loggedIn){
      window.location.href= this.hostname+'/dashboard'
    }
    return (
      <div className="home">
        <div style={loginForm}>
          <Form loading={this.state.loading}>
		    <Form.Input label='Email' ref='email' placeholder='test@test.com' />
		    <br/>
          	<Form.Field>
			  <label>Password</label>
			  <Input ref='password' type='password' placeholder='test'/>
			</Form.Field>		    
		  </Form>
      <br/>
		  <Button onClick={this.sendLoginRequest.bind(this)}>Submit</Button>
        </div>
      </div>
    );
  }

  sendLoginRequest(){
      let self = this;
  	this.setState({
  		loading:'loading'
  	});
  	return fetch(this.hostname+"/api/register",{
  		method:'POST',
      credentials:'same-origin',
  		headers: {'Content-Type':'application/x-www-form-urlencoded'}, // this line is important, if this content-type is not set it wont work
    	body: queryString.stringify({
  			email:'test@test.com',
  			password:'testing'
  		})
  	})
  	.then(function(response) {
  		console.log(response);
  		return response.json()
  	}).then(function(){
  		window.location.href=self.hostname+"/dashboard";
  	})
  }
}
