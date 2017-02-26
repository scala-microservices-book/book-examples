'use strict';

import React from 'react';
import { Link } from 'react-router';
import NotFoundPage from './NotFoundPage';
import SearchBar from './SearchBar';
import Medal from './Medal';
import Flag from './Flag';
import athletes from '../data/athletes';
import { Loader} from 'semantic-ui-react';


export default class DashboardPage extends React.Component {
  constructor(){
    super();
    this.hostname = window.location.protocol + "//" + window.location.hostname + ":"+(window.location.port);
    this.state = {
      loggedIn : null
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
    // const id = this.props.params.id;
    // const athlete = athletes.filter((athlete) => athlete.id === id)[0];
    // if (!athlete) {
    //   return <NotFoundPage/>;
    // }
    // const headerStyle = { backgroundImage: `url(/img/${athlete.cover})` };
    let content;
    if(this.state.loggedIn == null){
        content = <Loader active inline='centered' />  
    } else if(this.state.loggedIn == true) {
          content = <SearchBar/>
    } else {
      window.location.href=this.hostname
    }
    return (      
      <div className="athlete-full" style={{textAlign:"center"}}>
        {content}       
      </div>
    );
  }
}
