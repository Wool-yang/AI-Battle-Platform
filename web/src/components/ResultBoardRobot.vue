<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.winner === 'all'">
      Draw
    </div>

    <div class="result-board-text" v-else-if="$store.state.pk.winner === 'A' && $store.state.pk.a_id === parseInt($store.state.user.id)">
      Win
    </div>

    <div class="result-board-text" v-else-if="$store.state.pk.winner === 'B' && $store.state.pk.b_id === parseInt($store.state.user.id)">
      Win
    </div>

    <div class="result-board-text" v-else>
      Lose
    </div>

    <div class="result-board-btn">
      <button @click="restart()" type="submit" class="btn btn-primary btn-lg bt1"> rematch </button>
    </div>
  </div>
</template>

<script>
  import { useStore } from 'vuex'; 
  export default {
    name: "ResultBoardRobot",
    components: {  },
    setup() {
      const store = useStore();
      
      const restart = () => {
        store.commit("updateStatus", "matching");
        store.commit("updateWinner", "none");
        store.commit("updateOpponent", {
          username: "Tachikoma",
          photo: "/static/images/avatar/10.png"
        })
      }

      return {
        restart
      }
    }
}
</script>

<style scoped>
  div.result-board {
    height: 45vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.5);
    position: absolute;
    top: 45vh;
    left: 34.5vw;
    border-radius: 5vh;
  }
  div.result-board-text {
    text-align: center;
    color: white;
    font-size: 70px;
    font-weight: 700;
    font-style: italic;
    padding-top: 9vh;
  }
  div.result-board-btn {
    text-align: center;
    padding-top: 5vh;
  }
</style>