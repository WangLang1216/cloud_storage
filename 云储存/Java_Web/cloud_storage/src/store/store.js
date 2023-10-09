import { createStore } from 'vuex'

export default createStore({
  state: {
    token: ''
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
      localStorage.setItem('token', token);
    },
    removeToken(state) {
      state.token = '',
      localStorage.removeItem('token')
    }
  },
  actions: {
    
  },
  getters: {
    
  }
})
