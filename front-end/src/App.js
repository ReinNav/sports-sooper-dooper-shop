import React from 'react';
import AppRoutes from './routes';
import { connect } from "react-redux";
import 'bootstrap/dist/css/bootstrap.min.css';


const App = () => (
        <AppRoutes />
);

function mapStateToProps(state) {
    const { user } = state.auth;
    return {
      user,
    };
  }

  export default connect(mapStateToProps)(App);
