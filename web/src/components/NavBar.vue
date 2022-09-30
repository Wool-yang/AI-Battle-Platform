<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <router-link class="navbar-brand" :to="{name: 'home'}">AI Battle Platform</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'" aria-current="page" :to="{name: 'pk_index'}">Battle</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index'}">Battle List</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'ranklist_index'}">Rank List</router-link>
        </li>
      </ul>
      <ul class="navbar-nav drop-field" v-if="$store.state.user.is_login">
        <li class="nav-item dropdown">
          <router-link class="nav-link dropdown-toggle" :to="{name: ''}" id="navbarDarkDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ $store.state.user.username }}
          </router-link>
          <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="navbarDarkDropdownMenuLink">
            <li><router-link class="dropdown-item" :to="{name: 'user_bot_index'}">My Bots</router-link></li>
            <li><a class="dropdown-item" @click="logout">Logout</a></li>
          </ul>
        </li>
      </ul>
      <ul class="navbar-nav" v-else-if="$store.state.user.pulled_info">
        <li><router-link :class="route_name == 'user_account_login' ? 'nav-link active' : 'nav-link'" :to="{name: 'user_account_login'}">Login</router-link></li>
        <li><router-link :class="route_name == 'user_account_register' ? 'nav-link active' : 'nav-link'" :to="{name: 'user_account_register'}">Register</router-link></li>
      </ul>
    </div>
  </div>
</nav>
</template>
  
<script>
  import { useRoute } from 'vue-router';
  import { computed } from '@vue/reactivity';
  import { useStore } from 'vuex';
  import router from '@/router';

  export default {
    name: "NavBar",
    
    setup() {
      const route = useRoute();
      const store = useStore();
      let route_name = computed(() => route.name);

      const logout = () => {
        store.dispatch("logout");
        router.push({name: "user_account_login"});
      }

      return {
        route_name,
        logout,
      }
    }
  }
</script>
  
<style scoped>
  .drop-field {
    margin-right: 7%;
  }
</style>