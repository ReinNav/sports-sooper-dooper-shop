import {
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT,
} from "./AuthStatus";

import AuthService from "../services/AuthService";

export const signup = (username, email, password) => (dispatch) => {
  return AuthService.signup(username, email, password).then(
    (response) => {
      dispatch({
        type: REGISTER_SUCCESS,
      });

      return Promise.resolve(response);
    },
    (error) => {
      const message =
        (error.response &&
          error.response.data &&
          error.response.data.message) ||
        error.message ||
        error.toString();

      dispatch({
        type: REGISTER_FAIL,
      });

      return Promise.reject(message);
    }
  );
};

export const changePassword = (username, oldPassword, newPassword) => (dispatch) => {
  return AuthService.changePassword(username, oldPassword, newPassword).then(
    (data) => {
      dispatch({
        type: LOGIN_SUCCESS,
        payload: { user: data },
      });

      return Promise.resolve();
    },
    (error) => {
      const message =
        (error.response &&
          error.response.data &&
          error.response.data.message) ||
        error.message ||
        error.toString();

      dispatch({
        type: LOGIN_FAIL,
      });

      return Promise.reject(message);
    }
  );
};

export const login = (username, password) => (dispatch) => {
  return AuthService.login(username, password).then(
    (data) => {
      dispatch({
        type: LOGIN_SUCCESS,
        payload: { user: data },
      });

      return Promise.resolve();
    },
    (error) => {
      const message =
        (error.response &&
          error.response.data &&
          error.response.data.message) ||
        error.message ||
        error.toString();

      dispatch({
        type: LOGIN_FAIL,
      });

      return Promise.reject(message);
    }
  );
};

export const logout = () => (dispatch) => {
  AuthService.logout();

  dispatch({
    type: LOGOUT,
  });
};