
const routes = [
  {
    path: '/',
    component: null,
    children: [
      { path: '/my-page', component: () => import('pages/MyPage.vue')}
    ]
  },
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '/test', component: () => import('pages/TestPage.vue')}
    ]
  },
  {
    path: '/',
    component: null,
    children: [
      { path: `/chat/room/enter/:roomId`, component: () => import('pages/chat/ChatRoomDetail.vue')}
    ]
  },
  {
    path: '/',
    component: null,
    children: [
      { path: '/chat/room', component: () => import('pages/chat/ChatRoomList.vue')}
    ]
  },
  {
    path: '/',
    component: () => import('layouts/AuthLayout.vue'),
    children: [
      { path: '/sign-up', component: () => import('pages/sign-up/SignUpPage.vue')},
      { path: '/sign-in', component: () => import('pages/sign-in/SignInPage.vue')}
    ]
  },
  {
    path: '',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '/test', component: () => import('pages/TestPage.vue')}
    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes;
