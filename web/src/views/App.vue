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
  import NavBar from '@/components/NavBar.vue';

  export default {
    name: "App",
    components: { 
      NavBar 
    },
    setup: () => {
    },
}
</script>
