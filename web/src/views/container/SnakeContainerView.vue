<template>
  <div class="content">
    <div class="snake-title">
      <img src="@/assets/images/snake.jpg" alt="" >
      <div style="color: aliceblue;">&nbsp;Snake</div>
    </div>
    <ul class="nav nav-tabs nav-fill navtab">
      <li class="nav-item">
        <router-link :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link' + ' my-nav-item'" aria-current="page" :to="{name: 'pk_index'}">Battle</router-link>
      </li>
      <li class="nav-item">
        <router-link :class="route_name == 'pk_robot_index' ? 'nav-link active' : 'nav-link' + ' my-nav-item'" aria-current="page" :to="{name: 'pk_robot_index'}">Battle with Robots</router-link>
      </li>
      <li class="nav-item">
        <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link' + ' my-nav-item'" :to="{name: 'record_index'}">Battle List</router-link>
      </li>
      <li class="nav-item">
        <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link' + ' my-nav-item'" :to="{name: 'ranklist_index'}">Rank List</router-link>
      </li>
      <li class="nav-item">
        <router-link :class="route_name == 'user_bot_index' ? 'nav-link active' : 'nav-link' + ' my-nav-item'" :to="{name: 'user_bot_index'}">My Bots</router-link>
      </li>
    </ul>
    <router-view></router-view>
  </div>


  <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
    <div class="toast" style="width: 45vw" role="alert" aria-live="assertive" aria-atomic="true" ref="toastRef">
      <div class="toast-header">
        <img src="@/assets/images/snake.jpg" class="rounded me-2" alt="">
        <strong class="me-auto" style="font-size: medium;">Please wait a moment</strong>
        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
      <div class="toast-body" style="font-size: medium;">
        It is detected that your last game is not over or the game interval is too short, please wait at least 5 seconds before restarting
      </div>
    </div>
  </div>
</template>
  
<script>
  import { useRoute } from 'vue-router';
  import { computed } from '@vue/reactivity';

  import { onMounted, onUnmounted } from 'vue';
  import { useStore } from 'vuex';

  import { ref } from 'vue';
  import { Toast } from 'bootstrap/dist/js/bootstrap';

  export default {
    name: "SnakeContainer",
    components: {
    },
    setup() {
      const route = useRoute();
      let route_name = computed(() => route.name);
      const store = useStore();

      const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
      let socket = null;

      let toastRef = ref(null);
      let myToast = null;

      const options = {
        animation: true, // 开启过渡动画
        autohide: true, // 开启自动隐藏
        delay: 4500 // 3000ms后自动隐藏
      }

      onMounted(() => {
        myToast = new Toast(toastRef.value, options);
        socket = new WebSocket(socketUrl);

        socket.onopen = () => {
          // console.log("connected!");
          store.commit("updateSocket", socket);
        }

        socket.onmessage = msg => {
          const data = JSON.parse(msg.data);
          if (data.event === 'start-matching'){   // 匹配成功
                     
            setTimeout(() => {
              store.commit("updateStatus", "playing");
            }, 200);

            store.commit("updateGame", data.game);
            
          } else if (data.event === "move") {
            const game = store.state.pk.game_map_object;

            const [snake0, snake1] = game.snakes;
            snake0.set_direction(data.a_direction);
            snake1.set_direction(data.b_direction);

          } else if (data.event === "result") {          
            const game = store.state.pk.game_map_object;
            const [snake0, snake1] = game.snakes;

            if (data.winner === "all" || data.winner === "B") {
              snake0.status = "die";
            } 
            if (data.winner === "all" || data.winner === "A") {
              snake1.status = "die";
            }

            if (store.state.pk.status === 'playing')
              store.commit("updateWinner", data.winner);
          } else if (data.event === "error_start") {
            myToast.show();
          } else if (data.event === "error_matching") {
            myToast.show();
            store.commit("updateMatchingBtn", {
              btn_value: "Start Matching",
              btn_type: "success"
            })
          }
        }

        socket.onclose = () => {
          console.log("disconnected");
        }
      });

      onUnmounted(() => {
        if (store.state.pk.status === "matching") {
          store.state.pk.socket.send(JSON.stringify({
            event: "stop-matching",
          }));
        }
        socket.close();
      });

      return {
        route_name,
        toastRef,
        myToast
      }
    }
  };
</script>

<style scoped>
  .navtab {
    margin-top: 5px;
    background-color: rgba(50, 50, 50, 0.5);
    width: 80%;
    border-radius: 2vh;
  }
  .my-nav-item {
    color: #BDFCC9;
  }
  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .snake-title {
    display: flex;
    align-items: center;
    margin-top: 10px;
    align-self: flex-start;
    margin-left: 10%;
    font-size: xx-large;
  }
  img {
    width: 2em;
  }
</style>
  