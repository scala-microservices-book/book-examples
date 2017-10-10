'use strict';

import React from 'react';
import { Link } from 'react-router';
import {List, Image, Message, Icon} from 'semantic-ui-react';
export default class SearchResults extends React.Component {

	renderNotFoundView(){
			return (
				<Message icon>
					<Icon name='ban'/>
					<Message.Content>
						<Message.Header>No results found.</Message.Header>
						Try searching 'scala developers in singapore'
					</Message.Content>
				</Message>
			)

	}

	renderLoadingView(){
		return (
				<Message icon>
					<Icon name='circle notched' loading />
					<Message.Content>
						<Message.Header>Just one second</Message.Header>
			  			Loading results....
					</Message.Content>
				</Message>
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
		// Loading View
		if( this.props.loading) {
			return this.renderLoadingView()
		}
		if(Array.isArray(this.props.results) && this.props.results.length > 0){
			return(
				<List> {this.renderRows(this.props.results)} </List>
			)
		}

		// No results found view.
		if(Array.isArray(this.props.results) && this.props.results.length == 0){
			return this.renderNotFoundView()
		}

		// Default View.
		return this.renderNotFoundView()
	}
}
