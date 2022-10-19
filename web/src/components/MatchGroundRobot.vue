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
            <div class="select-tag">You&emsp;</div>
            <select v-if="selected_status" v-model="selected_bot" class="form-select" disabled>
              <option selected value="-1">Play in person</option>
              <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
            </select>

            <select v-else v-model="selected_bot" class="form-select">
              <option selected value="-1">Play in person</option>
              <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
            </select>
          </div> 
          
          <div class="robot-select-bot">
            <div class="select-tag">Robot</div>
            <select v-if="selected_status" v-model="selected_tachi" class="form-select" disabled>
              <option selected value="1">Simple Robot</option>
            </select>

            <select v-else v-model="selected_tachi" class="form-select">
              <option selected value="1">Simple Robot</option>
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
          <button type="button" @click="click_match_btn" class="btn btn-success btn-lg" style="margin-top:7vh;">Start Game</button>
        </div>
      </div>
  </div>
</template>

<script>
  import store from '@/store';
  import { ref } from 'vue';
  import $ from 'jquery';

  export default {
    name: "MatchGroundRobot",
    components: {  },
    setup() {
      let bots = ref([]);
      let selected_bot = ref("-1");
      let selected_tachi = ref("1");
      let selected_status = ref(false);

      const click_match_btn = () => {
        store.state.pk.socket.send(JSON.stringify({
          event: "start-robot-game",
          bot_id: selected_bot.value,
          tachi_id: selected_tachi.value
        }));
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
        selected_bot,
        selected_status,
        selected_tachi
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
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }
  div.robot-select-bot {
    padding-top: 5vh;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }
  div.user-select-bot > select {
    width: 70%;
    margin: 0 auto;
  }
  div.robot-select-bot > select {
    width: 70%;
    margin: 0 auto;
  }
  .select-tag {
    color: aliceblue;
    font-size: large;
    font-weight: 600;
    text-align: right;
  }
</style>