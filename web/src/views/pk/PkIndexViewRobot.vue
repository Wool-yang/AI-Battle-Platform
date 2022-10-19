<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'" />
  <MatchGroundRobot v-if="$store.state.pk.status === 'matching'" />
  <ResultBoardRobot v-if="$store.state.pk.winner !== 'none' && $store.state.pk.status === 'playing'" />
  <a href id="last"></a>
</template>

<script>
  import PlayGround from '@/components/PlayGround.vue';
  import MatchGroundRobot from '@/components/MatchGroundRobot.vue'
  import ResultBoardRobot from '@/components/ResultBoardRobot.vue';

  import { onMounted, onUnmounted } from 'vue';
  import { useStore } from 'vuex';

  export default {
    name: "PkIndexRobot",
    components: {
      PlayGround,
      MatchGroundRobot,
      ResultBoardRobot
    },
    setup() {
      const store = useStore();
      store.commit("updateWinner", "none");
      store.commit("updateIsRecord", false);

      let target = null;

      // 组件挂载完成之后建立 WebSocket 连接
      onMounted(() => {
        target = document.querySelector('#last');
        store.commit("updateOpponent", {
          username: "Tachikoma",
          photo: "/static/images/avatar/10.png"
        });
      });

      // 组件卸载完成后断开连接
      onUnmounted(() => {
        store.commit("updateWinner", "none");
        store.commit("updateStatus", "matching");
      });

      setInterval(() => {
        if (store.state.pk.winner === 'none' && store.state.pk.status === 'playing') {
          target.scrollIntoView();
        }
      }, 200);
    }
  };
</script>

<style scoped>

</style>
