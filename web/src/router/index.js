import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '@/views/pk/PkIndexView'
import RecordIndexView from '@/views/record/RecordIndexView'
import RecordContentView from '@/views/record/RecordContentView';
import RanklistIndexView from '@/views/ranklist/RanklistIndexView'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView'
import NotFound from '@/views/error/NotFound'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView'
import store from '@/store'

const routes = [
  {
    path: "/",
    name: "home",
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/record/:recordId/",
    name: "record_content",
    component: RecordContentView,
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView,
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requireAuth: false
    }
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requireAuth: false
    }
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.name === "record_content" && !store.state.record.is_record) {
    next({name: "record_index"});
  }
  if (to.meta.requireAuth && !store.state.user.is_login) {
    store.commit("updatePageBefore", to.name)
    next({name: "user_account_login"});
  } else {
    next();
  }
})

export default router
