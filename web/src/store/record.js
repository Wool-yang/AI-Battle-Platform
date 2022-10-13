export default ({
  state: {
    is_record: false,
    a_username: "",
    b_username: "",
    a_steps: "",
    b_steps: "",
    record_winner: "",
  },
  getters: {
  },
  mutations: {
    updateIsRecord(state, is_record) {
      state.is_record = is_record;
    },
    updateSteps(state, data) {
      state.a_steps = data.a_steps,
      state.b_steps = data.b_steps
    },
    updateRecordWinner(state, winner) {
      state.record_winner = winner;
    },
    updateRecordUsernames(state, data) {
      state.a_username = data.a_username,
      state.b_username = data.b_username
    }
  },
  modules: {
  }
})