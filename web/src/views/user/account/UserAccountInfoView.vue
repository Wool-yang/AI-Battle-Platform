<template>
  <div class="container"> 
    <div class="row">
      <div class="col-3">
        <div class="card mt-4">
          <div class="card-body" style="display: flex; flex-direction: column; justify-content:center; align-items: center;">
            <img :src="$store.state.user.photo" alt="" class="user-photo"/>
            <button type="button" class="btn btn-primary btn-sm mt-2" style="white-space: normal;" data-bs-toggle="modal" data-bs-target="#avatar-modal">change avatar</button>
            
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
                    <div class="error-message">{{ photo_error }}</div>
                    <button type="button" class="btn btn-success" @click="change_photo">OK</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">close</button>
                  </div>
                </div>
              </div>
            </div>
        
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card mt-4">
          <div class="card-body">
            <table class="table table-hover" style="font-size: 1.2em;">
              <thead>
                <tr>
                  <th style="width: 30%"></th>
                  <th style="width: 70%"></th>
                </tr>
              </thead>
              <tbody sy>
                <tr class="profile-tr">
                  <td>Username: </td>
                  <td>{{ $store.state.user.username }}</td>
                </tr>
                <tr class="profile-tr">
                  <td>Rating: </td>
                  <td>{{ info.rating }}</td>
                </tr>
                <tr class="profile-tr">
                  <td>Last Game: </td>
                  <td>{{ info.last_game }}</td>
                </tr>
                <tr class="profile-tr">
                  <td>Email: </td>
                  <td>{{ info.email }}</td>
                </tr>
                <tr class="profile-tr">
                  <td>Phone: </td>
                  <td>{{ info.phone }}</td>
                </tr>
                <tr class="profile-tr">
                  <td>Some thing else: </td>
                  <td><div style="word-break: break-all; word-wrap: break-word">{{ info.other }}</div></td>
                </tr>
              </tbody>
            </table>

            <!-- Update-Profile-Modal -->
            <div class="modal fade" id="update-profile" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-lg">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <form>
                      <div class="mb-3">
                        <label for="my-username" class="col-form-label">Username</label>
                        <input v-model="update_info.username" type="text" class="form-control" id="my-username">
                      </div>
                      <div class="mb-3">
                        <label for="my-email" class="col-form-label">Email</label>
                        <input v-model="update_info.email" type="email" class="form-control" id="my-email">
                      </div>
                      <div class="mb-3">
                        <label for="my-phone" class="col-form-label">Phone</label>
                        <input v-model="update_info.phone" type="tel" class="form-control" id="my-phone">
                      </div>
                      <div class="mb-3">
                        <label for="my-other" class="col-form-label">Some thing else</label>
                        <textarea v-model="update_info.other" class="form-control" id="my-other"></textarea>
                      </div>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <div class="error-message">{{ update_info.error_message }}</div>
                    <button type="button" class="btn btn-primary" @click="updateDetailInfo()">save</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">close</button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Update-Password--Modal -->
            <div class="modal fade" id="update-password" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-lg">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Change password</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <form>
                      <div class="mb-3">
                        <label for="oldpasswd" class="col-form-label">Old Password</label>
                        <input v-model="user_password.old" type="password" class="form-control" id="oldpasswd">
                      </div>
                      <div class="mb-3">
                        <label for="newpasswd" class="col-form-label">New Password</label>
                        <input v-model="user_password.new" type="password" class="form-control" id="newpasswd">
                      </div>
                      <div class="mb-3">
                        <label for="cnewpasswd" class="col-form-label">New Confirmed Password</label>
                        <input v-model="user_password.cnew" type="password" class="form-control" id="cnewpasswd">
                      </div>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <div class="error-message">{{ user_password.error_message }}</div>
                    <button type="button" class="btn btn-primary" @click="updatePassword()">update</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">close</button>
                  </div>
                </div>
              </div>
            </div>
            <button type="button" class="btn btn-success btn mybtn" data-bs-toggle="modal" data-bs-target="#update-profile" @click="set_update_info()">Edit profile</button>
            <button type="button" class="btn btn-warning btn mybtn mt-2" data-bs-toggle="modal" data-bs-target="#update-password" @click="reset_password()">Change password</button>
          </div>
        </div>
      </div>
    </div>
  </div> 
</template>

<script>
  import $ from 'jquery';
  import { useStore } from 'vuex';
  import { reactive, ref } from 'vue';
  import { Modal } from 'bootstrap/dist/js/bootstrap';
  export default {
    name: "UserAccountInfoView",
    components: {

    },
    setup() {
      const store = useStore();
      const info = reactive({
        rating: "",
        last_game: "",
        email: "",
        phone: "",
        other: ""
      })

      const update_info = reactive({
        username: "",
        email: "",
        phone: "",
        other: "",
        error_message: ""
      })

      const user_password = reactive({
        old: "",
        new: "",
        cnew: "",
        error_message: ""
      })

      let choice_index = ref(parseInt(store.state.user.photo.split(".")[0].split("/")[4]) - 1);
      let nums = ref([]);
      let photo_error = ref("");

      const init_nums = () => {
        for (let i = 1; i <= 9; i ++ ) {
          nums.value.push("/static/images/avatar/" + i + ".png");
        }
      }

      init_nums();

      const change_choice = (index) => {
        choice_index.value = index;
      };

      const change_photo = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/account/changephoto/",
          type: "PUT",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          data: {
            photo: choice_index.value + 1
          },
          success(resp) {
            if (resp.error_message === "success") {
              store.commit("updatePhoto", nums.value[choice_index.value]);
              Modal.getInstance('#avatar-modal').hide();
            }
          },
          error(resp) {
            photo_error.value = resp.error_message;
          }
        });
      }

      const getDetailInfo = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/account/detailinfo/",
          type: "GET",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success(resp) {
            info.rating = resp.rating;
            if (resp.last_game === "1970-01-01 00:00:00") {
              info.last_game = "No Record";
            } else {
              info.last_game = resp.last_game;
            }
            if (resp.email == null || resp.email === "") {
              info.email = "No Record";
            } else {
              info.email = resp.email;
            }
            if (resp.phone == null || resp.phone === "") {
              info.phone = "No Record";
            } else {
              info.phone = resp.phone;
            }
            if (resp.other == null || resp.other === "") {
              info.other = "No Record";
            } else {
              info.other = resp.other;
            }
          }
        })
      };

      getDetailInfo();

      const updateDetailInfo = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/account/updatedetailinfo/",
          type: "PUT",
          data: {
            username: update_info.username,
            email: update_info.email,
            phone: update_info.phone,
            other: update_info.other
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success(resp) {
            if (resp.error_message === "success") {
              getDetailInfo();
              store.commit("updateUsername", update_info.username);
              Modal.getInstance('#update-profile').hide();
            } else {
              update_info.error_message = resp.error_message;
            }
          },
          error() {
            update_info.error_message = "please wait a moment and try again";
          }
          
        })
      };

      const updatePassword = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/account/changepasswd/",
          type: "PUT",
          data: {
            old: user_password.old,
            new: user_password.new,
            cnew: user_password.cnew
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success(resp) {
            if (resp.error_message === "success") {
              Modal.getInstance('#update-password').hide();
              reset_password();
              store.commit("logout");
              store.dispatch("logout");
              location.reload();
            } else {
              user_password.error_message = resp.error_message;
            }
          },
          error() {
            user_password.error_message = "please wait a moment and try again";
          }
        })
      };

      const set_update_info = () => {
        update_info.username = store.state.user.username;
        update_info.email = info.email;
        update_info.phone = info.phone;
        update_info.other = info.other;
        update_info.error_message = "";
      }

      const reset_password = () => {
        user_password.old = "";
        user_password.new = "";
        user_password.cnew = "";
        user_password.error_message = "";
      }

      return {
        info,
        updateDetailInfo,
        update_info,
        set_update_info,
        updatePassword,
        user_password,
        reset_password,
        change_choice,
        change_photo,
        nums,
        photo_error,
        choice_index
      }
    }
  };
</script>

<style scoped>
  .user-photo {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  .profile-tr {
    vertical-align: middle;
  }
  .mybtn {
    display: flex;
    justify-content: center;
    white-space: nowrap; 
    width: 12em; 
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