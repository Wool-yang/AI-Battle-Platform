<template>
  <ContentField v-if="$store.state.user.pulled_info">
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input v-model="username" type="text" class="form-control" id="username" placeholder="Please enter your username">
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input v-model="password" type="password" class="form-control" id="password" placeholder="Please enter your password">
          </div>
          <div class="error-message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary bt1">Login</button>
        </form> 
      </div>
    </div>
  </ContentField>
</template>

<script >
  import ContentField from '@/components/ContentField.vue';
  import { useStore } from 'vuex';
  import { ref } from 'vue';
  import router from '@/router/index';

  export default {
    name: "UserAccountLogin",
    components: { ContentField },
    setup() {
      const store = useStore();
      let username = ref('');
      let password = ref('');
      let error_message = ref('');


      const jwt_token = localStorage.getItem("jwt_token");
      if (jwt_token) {
        store.commit("updateToken", jwt_token);
        store.dispatch("getInfo", {
          success() {
            store.commit("updatePulledInfo", true);
            if (store.state.user.page_before !== "") {
              router.push({name: store.state.user.page_before}); 
              store.commit("updatePageBefore", "");
            } else {
              router.push({name: 'home'});
            }
          },
          error() {
            store.commit("updatePulledInfo", true);
          }         
        });
      }
      else {
        store.commit("updatePulledInfo", true);
      }

      const login = () => {
        error_message.value = "";
        store.dispatch("login", {
          username: username.value,
          password: password.value,
          error_message: error_message.value,
          success() {
            store.dispatch("getInfo", {
              success() {
                if (store.state.user.page_before !== "") {
                  router.push({name: store.state.user.page_before}); 
                  store.commit("updatePageBefore", "");
                } else {
                  router.push({name: 'home'});
                }
                // console.log(store.state.user)
              },
              error() {
                error_message.value = "Wrong user name or password"
              }
            })
          },
          error() {
            error_message.value = "Wrong user name or password"
          }
        })
      }
      
      return {
        username,
        password,
        error_message,
        login,
      }
    }
  }
</script>

<style scoped>
  .bt1 {
    width: 100%;
  }
  .error-message {
    color: red;
  }
</style>
        