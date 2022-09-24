<template>
  <NavBar></NavBar>
  
  <router-view/>
</template>

<style>
  body {
    background-image: url("@/assets/images/background.png");
    background-size: cover;
  }
</style>

<script>
  import "bootstrap/dist/css/bootstrap.min.css"
  import "bootstrap/dist/js/bootstrap"
  import $ from 'jquery';
  import { ref } from 'vue';
  import NavBar from '@/components/NavBar.vue';

  export default {
    name: "App",
    components: { 
      NavBar 
    },
    setup: () => {
        let bot_name = ref("");
        let bot_rating = ref("");
        $.ajax({
            url: "http://localhost:3000/pk/getbotinfo",
            type: "get",
            success: resp => {
                bot_name.value = resp.name;
                bot_rating.value = resp.rating;
            }
        });
        return {
            bot_name,
            bot_rating
        };
    },
}
</script>
