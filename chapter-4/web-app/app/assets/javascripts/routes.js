'use strict';

import React from 'react';
import { Route, IndexRoute } from 'react-router';
import Layout from './components/Layout';
import IndexPage from './components/IndexPage';
import DashboardPage from './components/DashboardPage';
import NotFoundPage from './components/NotFoundPage';

const routes = (
  <Route path="/" component={Layout}>
    <IndexRoute component={IndexPage}/>
    <Route path="dashboard" component={DashboardPage}/>
    <Route path="*" component={NotFoundPage}/>
  </Route>
);

export default routes;
