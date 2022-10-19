import $ from 'jquery';

export default ({
  state: {
    id: "",
    username: "",
    photo: "",
    token: "",
    is_login: "",
    pulled_info: false,
    page_before: ""
  },
  getters: {
  },
  mutations: {
    updateUser(state, user) {
      state.id = user.id;
      state.username = user.username;
      state.photo = "/static/images/avatar/" + user.photo + ".png";
      state.is_login = user.is_login;
    },
    updateToken(state, token) {
      state.token = token;
    },
    updatePhoto(state, photo) {
      state.photo = photo;
    },
    logout(state) {
      state.id = "";
      state.username = "";
      state.photo = "";
      state.is_login = "";
      state.token = "";
      state.pulled_info = false;
      state.page_before = "";
    },
    updatePulledInfo(state, pulled_info) {
      state.pulled_info = pulled_info;
    },
    updatePageBefore(state, page_before) {
      state.page_before = page_before;
    }
  },
  actions: {
    login(context, data) {
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/account/token/",
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
        url: "http://127.0.0.1:3000/api/user/account/info/",
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