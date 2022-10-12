<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'" />
  <MatchGround v-if="$store.state.pk.status === 'matching'" />
  <ResultBoard v-if="$store.state.pk.winner !== 'none' && $store.state.pk.status === 'playing'" />
</template>

<script>
  import PlayGround from '@/components/PlayGround.vue';
  import MatchGround from '@/components/MatchGround.vue'
  import ResultBoard from '@/components/ResultBoard.vue';

  import { onMounted, onUnmounted } from 'vue';
  import { useStore } from 'vuex';

  export default {
    name: "PkIndex",
    components: {
    PlayGround,
    MatchGround,
    ResultBoard
    },
    setup() {
      const store = useStore();
      store.commit("updateWinner", "none");

      const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}/`;
      let socket = null;

      // 组件挂载完成之后建立 WebSocket 连接
      onMounted(() => {
        store.commit("updateOpponent", {
          username: "Opponent",
          photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
        });


        socket = new WebSocket(socketUrl);

        socket.onopen = () => {
          console.log("connected!");
          store.commit("updateSocket", socket);
        }

        socket.onmessage = msg => {
          const data = JSON.parse(msg.data);
          if (data.event === 'start-matching'){   // 匹配成功
            store.commit("updateOpponent", {
              username: data.opponent_username,
              photo: data.opponent_photo,
            });
            
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
          }
        }

        socket.onclose = () => {
          console.log("disconnected");
        }
      });

      // 组件卸载完成后断开连接
      onUnmounted(() => {
        if (store.state.pk.status === "matching") {
          socket.send(JSON.stringify({
            event: "stop-matching",
          }));
        }
        socket.close();
        store.commit("updateWinner", "none");
        store.commit("updateStatus", "matching");
      });
    }
  };
</script>

<style scoped>

</style>
