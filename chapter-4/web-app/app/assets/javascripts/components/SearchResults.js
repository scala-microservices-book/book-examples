'use strict';

import React from 'react';
import { Link } from 'react-router';
import {List, Image} from 'semantic-ui-react';
export default class SearchResults extends React.Component {

	renderDefaultView(){
		return (
			<div><p>No results </p></div>
			)
	}
	/*
	soTag:
	{id: 1, name: "scala"}
	soUser:
	{id: 2, name: "Muhammad", soAccountId: 2, aboutMe: "Toy apps or cute things like qsort in haskell really give the wrong idea.", soLink: "#",... }
	*/
	renderRows(results){
		let content = []
		results.forEach( row =>{
			content.push(
			    <List.Item>
			      <Image avatar src='https://react.semantic-ui.com//assets/images/avatar/small/rachel.png' />
			      <List.Content>
			        <List.Header as='h4'>{row.soUser.name}</List.Header>
			        <List.Description>Lives in <a><b>{row.soUser.location}</b></a></List.Description>
			      </List.Content>
			    </List.Item>
			)
		})
		return content
	}

  	render() {
  		debugger
  		// return this.renderDefaultView()
  		if(Array.isArray(this.props.results) && this.props.results.length > 0){
  			return(
  				<List> {this.renderRows(this.props.results)} </List>
  			)
  		} else {
  			return (
	    	this.renderDefaultView()
    		);	
  		}
    	
  }
}
