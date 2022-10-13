<template>
    <PlayGround/>
  </template>
  
  <script>
    import PlayGround from '@/components/PlayGround.vue';
    import { useStore } from 'vuex';
    import { onUnmounted } from 'vue';
    
    export default {
      name: "RecordContent",
      components: {
      PlayGround,
      
      },
      setup() {
        const store = useStore();
        onUnmounted(() => {
            store.commit("updateIsRecord", false);
        });
      }
    };
  </script>
  
  <style scoped>
  
  </style>
  