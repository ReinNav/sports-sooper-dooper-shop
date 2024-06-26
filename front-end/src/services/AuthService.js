import axios from "axios";

const API_URL = "http://localhost:8080/api/auth/";

class AuthService {
  login(username, password) {
    return axios
      .post(API_URL + "login", { username, password })
      .then((response) => {
        if (response.data.jwt) {
          localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  logout() {
    localStorage.removeItem("user");
  }

  signup(username, email, password) {
    return axios.post(API_URL + "signup", {
      username,
      email,
      password,
    });
  }

  changePassword(username, oldPassword, newPassword) {
    return axios.post(API_URL + "change-password", {
      username,
      oldPassword,
      newPassword,
    });
  }
}

export default new AuthService();
