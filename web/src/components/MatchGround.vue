<template>
  <div class="matchground">
      <div class="row">
        <div class="col-6">
          <div class="user-photo">
            <img :src="$store.state.user.photo" alt="" />
          </div>
          <div class="user-username">
            {{ $store.state.user.username }}
          </div>
        </div>
        <div class="col-6">
          <div class="user-photo">
            <img :src="$store.state.pk.opponent_photo" alt="" />
          </div>
          <div class="user-username">
            {{ $store.state.pk.opponent_username }}
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-12" style="text-align:center;">
          <button type="button" @click="click_match_btn" :class="'btn btn-' + match_btn_type + ' btn-lg'" style="margin-top:17vh;">{{ match_btn_info }}</button>
        </div>
      </div>
  </div>
</template>

<script>
  import store from '@/store';
  import { ref } from 'vue';

  export default {
    name: "MatchGround",
    components: {  },
    setup() {
      let match_btn_info = ref("Start Matching");
      let match_btn_type = ref("success");

      const click_match_btn = () => {
        if (match_btn_info.value === "Start Matching") {
          match_btn_info.value = "Stop";
          match_btn_type.value = "warning";

          // 向后端发送消息
          // 传递一个 域 来表示当前操作
          store.state.pk.socket.send(JSON.stringify({
            event: "start-matching",
          }));

        } else {
          match_btn_info.value = "Start Matching";
          match_btn_type.value = "success";

          store.state.pk.socket.send(JSON.stringify({
            event: "stop-matching",
          }));
        }
      }


      return {
        match_btn_info,
        match_btn_type,
        click_match_btn,
      }
    }
}
</script>

<style scoped>
  div.matchground {
    width: 70vw;
    height: 75vh;
    margin: 7vh auto;
    background-color: rgba(50, 50, 50, 0.5);
    border-radius: 5vh;
  }
  div.user-photo {
    text-align: center;
    padding-top: 10vh;
  }
  div.user-photo > img {
    border-radius: 50%;
    width: 30vh;
  }
  div.user-username {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: whitesmoke;
  }
</style>