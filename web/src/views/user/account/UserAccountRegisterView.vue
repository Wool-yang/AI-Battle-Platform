<template>
    <ContentField>
      <div class="row justify-content-md-center">
        <div class="col-3">
          <form @submit.prevent="register">
            <div class="mb-3">
              <label for="username" class="form-label">Username</label>
              <input v-model="username" type="text" class="form-control" id="username" placeholder="Please enter your username">
            </div>
            <div class="mb-3">
              <label for="password" class="form-label">Password</label>
              <input v-model="password" type="password" class="form-control" id="password" placeholder="Please enter your password">
            </div>
            <div class="mb-3">
              <label for="confirmedPassword" class="form-label">Confirmed Password</label>
              <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="Please confirm the password">
            </div>
            <div class="error-message">{{ error_message }}</div>
            <button type="submit" class="btn btn-primary bt1">Register</button>
          </form> 
        </div>
      </div>
    </ContentField>
</template>

<script >
  import ContentField from '@/components/ContentField.vue';
  import { ref } from "vue";
  import router from '@/router/index';
  import $ from 'jquery';

  export default {
    name: "UserAccountRegister",
    components: { ContentField },
    setup() {
      let username = ref("");
      let password = ref("");
      let confirmedPassword = ref("");
      let error_message = ref("");

      const register = () => {
        $.ajax({
          url: "http://localhost:3000/user/account/register/",
          type: "POST",
          data: {
            username: username.value,
            password: password.value,
            confirmedPassword: confirmedPassword.value
          },
          success(resp) {
            if (resp.error_message === 'success') {
              router.push({name: "user_account_login"});
            } else {
              error_message.value = resp.error_message;
            }
          },
          error(resp) {
            error_message.value = resp.error_message;
          }
        });
      }

      return {
        username,
        password,
        confirmedPassword,
        error_message,
        register
      }
    }
  };  
</script>

<style scoped>
  .bt1 {
    width: 100%;
  }
  .error-message {
    color: red;
  }
</style>