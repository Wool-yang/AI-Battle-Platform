<template>
  <div class="matchground">
      <div class="row">
        <div class="col-4">
          <div class="user-photo">
            <img :src="$store.state.user.photo" alt="" />
          </div>
          <div class="user-username">
            {{ $store.state.user.username }}
          </div>
        </div>
        <div class="col-4">
          <div class="user-select-bot">
            <select v-if="selected_status" v-model="selected_bot" class="form-select" disabled>
              <option selected value="-1">Play in person</option>
              <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
            </select>

            <select v-else v-model="selected_bot" class="form-select">
              <option selected value="-1">Play in person</option>
              <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
            </select>
          </div> 
        </div>
        <div class="col-4">
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
  import $ from 'jquery';

  export default {
    name: "MatchGround",
    components: {  },
    setup() {
      let match_btn_info = ref("Start Matching");
      let match_btn_type = ref("success");
      let bots = ref([]);
      let selected_bot = ref("-1");

      let selected_status = ref(false);

      const click_match_btn = () => {
        if (match_btn_info.value === "Start Matching") {
          match_btn_info.value = "Stop";
          match_btn_type.value = "warning";

          // 向后端发送消息
          // 传递一个 域 来表示当前操作
          store.state.pk.socket.send(JSON.stringify({
            event: "start-matching",
            bot_id: selected_bot.value
          }));

          selected_status.value = true;
        } else {
          match_btn_info.value = "Start Matching";
          match_btn_type.value = "success";

          store.state.pk.socket.send(JSON.stringify({
            event: "stop-matching",
          }));

          selected_status.value = false;
        }
      };

      const refresh_bots = () => {
        $.ajax({
          url: "http://localhost:3000/user/bot/getall/",
          type: "GET",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success(resp) {
            bots.value = resp;
          }
        })
      };

      refresh_bots();
      
      return {
        match_btn_info,
        match_btn_type,
        click_match_btn,
        bots,
        selected_bot,
        selected_status
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
  div.user-select-bot {
    padding-top: 25vh;
  }
  div.user-select-bot > select {
    width: 70%;
    margin: 0 auto;
  }
</style>