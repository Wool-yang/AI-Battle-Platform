import { createApp } from 'vue'
import App from './views/App.vue'
import router from './router'
import store from './store'

createApp(App).use(store).use(router).use(router).mount('#app')
