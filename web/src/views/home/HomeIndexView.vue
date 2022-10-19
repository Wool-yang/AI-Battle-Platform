<template>
  <ContentField>
    <div class="row">
      <div class="col-12">
        <div class="card title1">
          欢迎来到 AI Battle Platform
        </div>
      </div>
    </div>
    <br/>
    <div class="row">
      <div class="col-7">
          <div class="card">
            <div class="card-header title2">
              这是什么？
            </div>
            <div class="card-body">
              这是一个在线程序设计游戏对战平台，玩家可以为不同的游戏编写不同的 AI 程序来进行对战并且同时支持<b>人人对战</b>、<b>人机对战</b>
            </div>
          </div>
          <br/>
          <div class="card">
            <div class="card-header title2">
              快速开始
            </div>
            <div class="card-body">
              1. <router-link class="'nav-link" :to="{name: 'user_account_register'}">注册</router-link>账户<br/>
              2. 查看游戏规则<br/>
              3. <router-link class="'nav-link" :to="{name: 'game_container'}">选择一款游戏</router-link>，进入相应的bot管理页面，创建自己的bot<br/>
              4. 进入对战页面，选择自己的bot程序或者选择真人游玩，开始匹配
            </div>
          </div>
      </div>
      <div class="col-5">
        <div class="card h-100">
          <div class="card-header title2">
            Wool
          </div>
          <div class="card-body">
            <div>
              仍在探索自己的道路<br/>
              上一个生日愿望是希望日积月累并不是谎言<br/>
              等一款次时代头显，可以让我躺在床上刷手机胳膊不会酸<br/>
              相当珍惜摆烂的时间，导致每次以996的姿态开摆<br/>
            </div>
            <br/>
            <br/>
            <div>
              <b>Email:</b> &nbsp;<a href="mailto://yangmaowool@gmail.com">yangmaowool@gmail.com</a><br/>
              <b>Blog(年久失修):</b> &nbsp;<a href="https://woolym.com">https://woolym.com</a><br/>
              <b>Battle.net:</b> &nbsp;Wool#5502 (彩笔打ow)
            </div>
          </div>
        </div>
      </div>
    </div>

  </ContentField>
</template>
  
<script>
  import ContentField from '@/components/ContentField.vue';
  import { useStore } from 'vuex';

  export default {
    name: "HomeIndex",
    components: {
      ContentField
    },
    setup() {
      const store = useStore();
      const jwt_token = localStorage.getItem("jwt_token");
      if (jwt_token) {
        store.commit("updateToken", jwt_token);
        store.dispatch("getInfo", {
          success() {
            store.commit("updatePulledInfo", true);
          },
          error() {
            store.commit("updatePulledInfo", true);
          }         
        });
      }
      else {
        store.commit("updatePulledInfo", true);
      }
    }
  };
</script>

<style scoped>
  .title1 {
    font-size: xx-large;
    font-weight: 700;
    align-items: center;
  }
  .title2 {
    font-size: larger;
    font-weight: 700;
    align-items: center;
  }
</style>
  