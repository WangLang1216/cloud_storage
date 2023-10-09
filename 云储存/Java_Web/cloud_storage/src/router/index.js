import { createRouter, createWebHashHistory } from "vue-router";

import Login from "../components/login/Login.vue";
import Home from "../components/home/Home.vue";

const routes = [
  {
    path: '/',
    components: {
      default: Login
    }
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/home',
    name: 'home',
    component: Home
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  mode: History,
  routes
});


router.afterEach((to, from, next) => {
  // 解决页面/路由跳转后，滚动条消失，页面无法滚动
  document.querySelector("body").setAttribute("style", "overflow: auto !important;")
});

export default router;

