export default ({
  state: {
    status: "matching",     // matching 表示匹配界面，playing 表示对战界面
    socket: null,           // WebSocket 对象
    opponent_username: "",  //
    opponent_photo: "",
    
    a_id: 0,
    a_sx: 0,
    a_sy: 0,
    b_id: 0,
    b_sx: 0,
    b_sy: 0,
    game_map: null,

    game_map_object: null,

    winner: "none",       // none, all, A, B

    btn_value: "Start Matching",
    btn_type: "success"
  },
  getters: {
  },
  mutations: {
    updateSocket(state, socket) {
      state.socket = socket;
    },
    updateOpponent(state, opponent) {
      state.opponent_username = opponent.username;
      if (opponent.photo.length <= 3) {
        state.opponent_photo = "/static/images/avatar/" + opponent.photo + ".png";
        // console.log(state.opponent_photo);
      } else {
        state.opponent_photo = opponent.photo;
      }
    },
    updateStatus(state, status) {
      state.status = status;
    },
    updateGame(state, game) {
      state.a_id = game.a_id;
      state.a_sx = game.a_sx;
      state.a_sy = game.a_sy;
      state.b_id = game.b_id;
      state.b_sx = game.b_sx;
      state.b_sy = game.b_sy;
      state.game_map = game.map;
    },
    updateGameMapObject(state, object) {
      state.game_map_object = object;
    },
    updateWinner(state, winner) {
      state.winner = winner;
    },
    updateMatchingBtn(state, btn) {
      state.btn_value = btn.btn_value;
      state.btn_type = btn.btn_type;
    }
  },
  modules: {
  }
})