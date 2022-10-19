<template>
    <ContentField>
      <div class="row justify-content-md-center">
        <div class="col-3">
          <!-- <form @submit.prevent="register"> -->
          <form  @submit.prevent>
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
            <button type="submit" class="btn btn-primary bt1"  data-bs-toggle="modal" data-bs-target="#avatar-modal">Register</button>

            <!-- avatar-Modal -->
            <div class="modal fade" id="avatar-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-lg">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Choose an avatar</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body my-modal-body">
                    <div class="container" >
                      <div class="row">
                        <div v-for="(num, index) in nums" :key=index class="col-4 my-modal-body">
                          <img @click="change_choice(index)" :class="'my-avatar' + (index === choice_index ? ' choicelist-avatar' : '')" :src=num>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" @click="register" class="btn btn-success" data-bs-dismiss="modal">OK</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">close</button>
                  </div>
                </div>
              </div>
            </div>
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

      let choice_index = ref(0);
      let nums = ref([]);

      const init_nums = () => {
        for (let i = 1; i <= 9; i ++ ) {
          nums.value.push("/static/images/avatar/" + i + ".png");
        }
      }

      init_nums();

      const change_choice = (index) => {
        choice_index.value = index;
      };

      const register = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/account/register/",
          type: "POST",
          data: {
            username: username.value,
            password: password.value,
            confirmedPassword: confirmedPassword.value,
            photo: choice_index.value + 1
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
        register,
        nums,
        change_choice,
        choice_index
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
  .my-avatar {
    object-fit: cover;
    width: 12vw;
    height: 12vw;
    border-radius: 50%;
    margin-bottom: 10px;
  }
  .my-modal-body {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  .choicelist-avatar {
    box-shadow: 0 0 7px 7px #66ccff;
  }
</style>