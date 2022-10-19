import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '@/views/pk/PkIndexView'
import PkIndexViewRobot from '@/views/pk/PkIndexViewRobot'
import RecordIndexView from '@/views/record/RecordIndexView'
import RecordContentView from '@/views/record/RecordContentView';
import RanklistIndexView from '@/views/ranklist/RanklistIndexView'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView'
import NotFound from '@/views/error/NotFound'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView'
import UserAccountInfoView from '@/views/user/account/UserAccountInfoView'
import HomeIndexView from '@/views/home/HomeIndexView'
import SnakeContainerView from '@/views/container/SnakeContainerView'
import GamesContainerView from '@/views/container/GamesContainerView'
import RuleIndexView from '@/views/rules/RuleIndexView'
import store from '@/store'

const routes = [
  {
    path: "/",
    name: "home_index",
    component: HomeIndexView,
    meta: {
      requireAuth: false,
      title: "AI Battle Platform"
    }
  },
  {
    path: "/games/",
    name: "game_container",
    component: GamesContainerView,
    meta: {
      requireAuth: true,
      title: "Games"
    }
  },
  {
    path: "/snake/",
    name: "snakeIndex",
    component: SnakeContainerView,
    meta: {
      requireAuth: true,
      title: "Snake"
    },
    children: [
      {
        path: "/snake/pk/",
        name: "pk_index",
        component: PkIndexView,
        meta: {
          requireAuth: true,
          title: "Snake Battle"
        }
      },
      {
        path: "/snake/pkrobot/",
        name: "pk_robot_index",
        component: PkIndexViewRobot,
        meta: {
          requireAuth: true,
          title: "Snake Battle"
        }
      },
      {
        path: "/snake/record/",
        name: "record_index",
        component: RecordIndexView,
        meta: {
          requireAuth: true,
          title: "Snake Record List"
        }
      },
      {
        path: "/snake/record/:recordId/",
        name: "record_content",
        component: RecordContentView,
        meta: {
          requireAuth: true,
          title: "Snake Record Play"
        }
      },
      {
        path: "/snake/ranklist/",
        name: "ranklist_index",
        component: RanklistIndexView,
        meta: {
          requireAuth: true,
          title: "Snake Rank List"
        }
      },
      {
        path: "/snake/user/bot/",
        name: "user_bot_index",
        component: UserBotIndexView,
        meta: {
          requireAuth: true,
          title: "Snake Bots"
        }
      }
    ]
  },
  {
    path: "/rules/",
    name: "rule_index",
    component: RuleIndexView,
    meta: {
      requireAuth: true,
      title: "Rules"
    }
  },
  {
    path: "/user/account/info/",
    name: "user_info",
    component: UserAccountInfoView,
    meta: {
      requireAuth: true,
      title: "User Info"
    }
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
    meta: {
      requireAuth: false,
      title: "404"
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requireAuth: false,
      title: "Login"
    }
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requireAuth: false,
      title: "Register"
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  }

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
