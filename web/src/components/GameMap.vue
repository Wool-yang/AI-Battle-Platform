<template>
  <div ref="parent" class="gamemap">
    <div class="noticeSnakeR" style="color: #00B2EE">
      {{ rightNotice }}
    </div>
    <canvas ref="canvas" tabindex="0"></canvas>
    <div class="noticeSnakeL" style="color: #FF3030">
      {{ leftNotice }}
    </div>
  </div>
</template>

<script>
  import { GameMap } from '@/assets/scripts/game_map/GameMap';
  // onMounted 组件挂载结束后的操作
  import { ref, onMounted } from 'vue';
  import { useStore } from 'vuex';

  export default {
    name: "GameMap",
    setup() {
      const store = useStore();
      // 响应式数据，当数据发生改变时，Vue会自动更新UI
      let parent = ref(null);
      let canvas = ref(null);

      let leftNotice = ref("");
      let rightNotice = ref("");

      // 元素挂载结束之后再获取元素
      onMounted(() => {
        /* 创建 GameMap 对象，并存储到 store 中
        WebSocket 连接接收到信息后可以直接修改 GameMap 对象中的蛇对象等属性 */
        store.commit("updateGameMapObject", 
        new GameMap(canvas.value.getContext('2d'), parent.value, store));

        if (store.state.pk.a_id === parseInt(store.state.user.id)) {
          leftNotice.value = "You";
          rightNotice.value = "Rival";
        } else if (store.state.pk.b_id === parseInt(store.state.user.id)) {
          leftNotice.value = "Rival";
          rightNotice.value = "You";
        }

      });

      return {
        parent,
        leftNotice,
        rightNotice,
        canvas
      }
    }
  }
</script>

<style scoped>
  .gamemap {
    height: 100%;
    width: 100%;

    /* canvas居中 */
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  div.noticeSnakeR {
    margin-top: 5%;
    margin-right: 9%;
    font-size: 2.2em;
    font-weight: 700;
    -webkit-text-stroke: 0.02em aliceblue;
    align-self: flex-end;
  }
  div.noticeSnakeL {
    margin-left: 9%;
    font-size: 2.2em;
    font-weight: 700;
    -webkit-text-stroke: 0.02em aliceblue;
    align-self: flex-start;
  }
</style>