<template>
  <div ref="parent" class="gamemap">
    <canvas ref="canvas" tabindex="0"></canvas>
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

      // 元素挂载结束之后再获取元素
      onMounted(() => {
        // 创建 GameMap 对象
        new GameMap(canvas.value.getContext('2d'), parent.value, store)
      });

      return {
        parent,
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
    justify-content: center;
    align-items: center;
  }
</style>