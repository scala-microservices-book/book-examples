'use strict';

import React from 'react';
import { Link } from 'react-router';
import { Grid, Column, Sidebar, Segment, Button, Menu, Image, Icon, Header } from 'semantic-ui-react'
// import SidebarLeftSlideAlong from './SidebarLeftSlideAlong';
const headerStyle = {
  textAlign : 'center'
}
export default class Layout extends React.Component {

  constructor(){
    super();
    this.hostname = window.location.protocol + "//" + window.location.hostname + ":"+(window.location.port);
    this.state = { visible: false }  
  }
  

  toggleVisibility(){
    this.setState({ visible: !this.state.visible })
  }

  logoutRequest(){
    let self = this
    return fetch(this.hostname+"/api/logout",{
      method:'GET',
      credentials:'same-origin'
    })
    .then(function(response) {
      return response.json()
    }).then(function(){
      window.location.href= self.hostname;
    })
  }

  render() {
    const { visible } = this.state;
    return (
      <div>
        <Sidebar.Pushable as={Segment}>
          <Sidebar as={Menu} animation='scale down' width='thin' direction='right' visible={visible} icon='labeled' vertical inverted>
            <Menu.Item name='welcome'>
              Welcome {this.state.email}
            </Menu.Item>
            <Menu.Item onClick={this.logoutRequest.bind(this)} name='sign out'>
              <Icon name='sign out' />
              Sign Out
            </Menu.Item>
          </Sidebar>
          <Sidebar.Pusher>
            <Segment basic>
              <div className="app-container">
                <header style={headerStyle}>
                  <Grid columns='equal'>
                    <Grid.Row>
                      <Grid.Column>
                          <Link to="/">
                          <Header as='h2'>
                            <Image shape='circular' src='http://semantic-ui.com/images/avatar2/large/patrick.png' />
                            {' '} Seeker2
                          </Header>
                          </Link>
                      </Grid.Column>
                      <Grid.Column>
                      </Grid.Column>
                      <Grid.Column>
                      </Grid.Column>
                      <Grid.Column>
                          <Button onClick={this.toggleVisibility.bind(this)}><Icon name='setting' size='large' /></Button>
                      </Grid.Column>
                    </Grid.Row>
                  </Grid>          
                </header>
                <div className="app-content">{this.props.children}</div>
                <footer>
                  <p>
                    This is a demo app to showcase  example from <strong>scala microservices</strong> book.
                  </p>
                  </footer>
              </div>
            </Segment>
          </Sidebar.Pusher>
        </Sidebar.Pushable>
      </div>
    )
  }

  render2() {
    return (
      <div className="app-container">
        <header style={headerStyle}>
          <Grid columns='equal'>
            <Grid.Row>
              <Grid.Column>
                  <Link to="/">
                  <Header as='h2'>
                    <Image shape='circular' src='http://semantic-ui.com/images/avatar2/large/patrick.png' />
                    {' '} Seeker
                  </Header>
                  </Link>
              </Grid.Column>
              <Grid.Column>
              </Grid.Column>
              <Grid.Column>
              </Grid.Column>
              <Grid.Column>
                  <SidebarLeftSlideAlong />
              </Grid.Column>
            </Grid.Row>
          </Grid>          
        </header>
        <div className="app-content">{this.props.children}</div>
        <footer>
          <p>
            This is a demo app to showcase  example from <strong>scala microservices</strong> book.
          </p>
          </footer>
      </div>
    );
  }
}
