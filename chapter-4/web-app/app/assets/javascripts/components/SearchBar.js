'use strict';

import React from 'react';
import { Link } from 'react-router';
import {Input} from 'semantic-ui-react';
export default class SearchBar extends React.Component {
  render() {
    return (
    <Input
    size="massive"
    action={{ color: 'teal', labelPosition: 'right', icon: 'search', content: 'Search' }}
    defaultValue=''
  	/>
    );
  }
}
