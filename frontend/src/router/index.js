import { route } from 'quasar/wrappers'
import { createRouter, createMemoryHistory, createWebHistory, createWebHashHistory } from 'vue-router'
import routes from './routes'

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */

export default route(function (/* { store, ssrContext } */) {
  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : (process.env.VUE_ROUTER_MODE === 'history' ? createWebHistory : createWebHashHistory)

  const Router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    routes,

    // Leave this as is and make changes in quasar.conf.js instead!
    // quasar.conf.js -> build -> vueRouterMode
    // quasar.conf.js -> build -> publicPath
    history: createHistory(process.env.MODE === 'ssr' ? void 0 : process.env.VUE_ROUTER_BASE)
  })

  function checkUserAuthentication() {
    // 동기적으로 localStorage의 값을 확인
    const userToken = localStorage.getItem('authStore');

    // 사용자 토큰이 존재하면 로그인된 것으로 간주
    return !(userToken.length <= 2)
  }

  Router.beforeEach((to, from, next) => {
    // Todo... 로그인 확인
    const isAuthenticated = checkUserAuthentication()

    // 로그인이 안 되어 있는 상태에서만 리디렉션을 시도
    if (!isAuthenticated && to.path !== '/sign-in') {
      console.log("!@#!@#@!#", from.path);
      next('/sign-in');
    }
    else if(isAuthenticated && to.path === '/') {
      next('/property/map')
    }
    else {
      // 그 외의 경우에는 정상적으로 진행
      next();
    }
  })

  return Router
})
