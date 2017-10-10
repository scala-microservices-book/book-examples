'use strict';

import React from 'react';
import {Form, Button, Input, Header, Message} from 'semantic-ui-react';
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
			loggedIn:null,
			flashMessage: "",
			email:null,
			password:null
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
				<Header as='h2'> Log In </Header>
					<Header as='h4'> <a href="/register">New User? Register here.</a> </Header>
					<Form loading={this.state.loading}>
						<Form.Input label='Email' onChange={this.updateEmailState.bind(this)} placeholder='test@test.com' />
						<br/>
						<Form.Field>
							<label>Password</label>
							<Input type='password' onChange={this.updatePasswordState.bind(this)} placeholder='test'/>
						</Form.Field>
					</Form>
					<br/>
					<Button onClick={this.sendLoginRequest.bind(this)}>Submit</Button>
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

	sendLoginRequest(){
		let self = this;
		this.setState({
			loading:'loading'
		});
		return fetch(this.hostname+"/api/login",{
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
				loggedIn:false,
				loading: false,
				flashMessage: "Login failed with response code: "+response.status
			});
			throw new Error(response.status)
		}).then(function(responseObj){
			window.location.href=self.hostname+"/dashboard?tokenStr="+responseObj.message.tokenStr;
		})
	}
}
