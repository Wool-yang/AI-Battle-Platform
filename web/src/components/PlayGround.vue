<template>
  <div class="playgroud">
    <GameMap></GameMap>
  </div>
</template>

<script>
  import GameMap from './GameMap.vue';
  export default {
    name: "PlayGround",
    components: { GameMap }
}
</script>

<style>
  .playgroud {
    width: 50vw;
    height: 75vh;
    /* background-color: lightblue; */
    margin: 5vh auto;
  }
</style>