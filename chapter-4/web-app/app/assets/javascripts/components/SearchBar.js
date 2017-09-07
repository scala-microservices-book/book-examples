'use strict';

import React from 'react';
import { Link } from 'react-router';
import {Input, Button} from 'semantic-ui-react';
export default class SearchBar extends React.Component {
  render() {
    return (
      <div>
        <Input
        size="massive"
        placeholder='location(hyderabad)...'
      	/>
        <Input
        size="massive"
        placeholder='tag(scala)...'
        />
        <Button secondary onClick={this.props.onClick}>Search</Button>
      </div>
    );
  }
}
