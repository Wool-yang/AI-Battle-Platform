<template>
  <ContentField>my bot</ContentField>
</template>
  
<style>

</style>

<script scoped>
  import ContentField from '@/components/ContentField.vue';
  import $ from 'jquery';
  import { useStore } from 'vuex';

  export default {
    name: "UserBotIndex",
    components: { ContentField },
    setup() {
      const store = useStore();

      // $.ajax({
      //   url: "http://localhost:3000/user/bot/add/",
      //   type: "POST",
      //   headers: {
      //     Authorization: "Bearer " + store.state.user.token,
      //   },
      //   data: {
      //     title: "bot标题",
      //     description: "bot描述",
      //     content: "bot代码"
      //   },
      //   success(resp) {
      //     console.log(resp);
      //   },
      //   error(resp) {
      //     console.log(resp);
      //   }
      // })

      //   $.ajax({
      //     url: "http://localhost:3000/user/bot/remove/",
      //     type: "DELETE",
      //     headers: {
      //       Authorization: "Bearer " + store.state.user.token,
      //     },
      //     data: {
      //       bot_id: "6",
      //     },
      //     success(resp) {
      //       console.log(resp);
      //     },
      //     error(resp) {
      //       console.log(resp);
      //     }
      //   })
      // }
      
      //   $.ajax({
      //     url: "http://localhost:3000/user/bot/update/",
      //     type: "PUT",
      //     headers: {
      //       Authorization: "Bearer " + store.state.user.token,
      //     },
      //     data: {
      //       bot_id: "3",
      //       title: "7777",
      //       description: "123",
      //       content: "笑嘻了",
      //     },
      //     success(resp) {
      //       console.log(resp);
      //     },
      //     error(resp) {
      //       console.log(resp);
      //     }
      //   })

        $.ajax({
          url: "http://localhost:3000/user/bot/getall/",
          type: "GET",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success(resp) {
            console.log(resp);
          },
          error(resp) {
            console.log(resp);
          }
        })
      }
  };
</script>
  