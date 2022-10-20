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
            <select v-if="$store.state.pk.btn_value === 'Stop'" v-model="selected_bot" class="form-select" disabled>
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
          <button type="button" @click="click_match_btn" :class="'btn btn-' + $store.state.pk.btn_type + ' btn-lg'" style="margin-top:11vh;">{{ $store.state.pk.btn_value }}</button>
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
      let bots = ref([]);
      let selected_bot = ref("-1");

      const click_match_btn = () => {
        if (store.state.pk.btn_value === "Start Matching") {
          store.commit("updateMatchingBtn", {
            btn_value: "Stop",
            btn_type: "warning"
          })

          // 向后端发送消息
          // 传递一个 域 来表示当前操作
          store.state.pk.socket.send(JSON.stringify({
            event: "start-matching",
            bot_id: selected_bot.value
          }));
        } else {
          store.commit("updateMatchingBtn", {
            btn_value: "Start Matching",
            btn_type: "success"
          })

          store.state.pk.socket.send(JSON.stringify({
            event: "stop-matching",
          }));
        }
      };

      const refresh_bots = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/bot/getall/",
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
        click_match_btn,
        bots,
        selected_bot
      }
    }
}
</script>

<style scoped>
  div.matchground {
    width: 70vw;
    height: 75vh;
    margin: 3vh auto;
    background-color: rgba(255, 255, 255, 0.2);
    border-radius: 5vh;
  }
  div.user-photo {
    text-align: center;
    padding-top: 10vh;
  }
  div.user-photo > img {
    border-radius: 50%;
    width: 15vw;
    height: 15vw;
    object-fit: cover;
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