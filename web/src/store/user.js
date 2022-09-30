import $ from 'jquery';

export default ({
  state: {
    id: "",
    username: "",
    photo: "",
    token: "",
    is_login: "",
    pulled_info: false,
  },
  getters: {
  },
  mutations: {
    updateUser(state, user) {
      state.id = user.id;
      state.username = user.username;
      state.photo = user.photo;
      state.is_login = user.is_login;
    },
    updateToken(state, token) {
      state.token = token;
    },
    logout(state) {
      state.id = "";
      state.username = "";
      state.photo = "";
      state.is_login = "";
      state.token = "";
      state.pulled_info = false;
    },
    updatePulledInfo(state, pulled_info) {
      state.pulled_info = pulled_info;
    }
  },
  actions: {
    login(context, data) {
      $.ajax({
        url: "http://localhost:3000/user/account/token/",
        type: "POST",
        data: {
          username: data.username,
          password: data.password
        },
        success(resp) {
          if (resp.error_message === "success") {
            localStorage.setItem("jwt_token", resp.token);
            context.commit("updateToken", resp.token);
            data.success(resp);
          } else {
            data.error(resp);
          }
        },
        error(resp) {
          data.error(resp);
        }
      });
    },
    getInfo(context, data) {
      $.ajax({
        url: "http://localhost:3000/user/account/info/",
        type: "GET",
        headers: {
          Authorization: "Bearer " + context.state.token,
        },
        success(resp) {
          if (resp.error_message === 'success') {
            context.commit("updateUser", {
              ...resp,
              is_login: true
            });
            data.success(resp);
          } else {
            data.error(resp);
          }
        },
        error(resp) {
          data.error(resp);
        }
      });
    },
    logout(context) {
      localStorage.removeItem("jwt_token");
      context.commit("logout");
    }
  },
  modules: {
  }
})