const routes = [
  {
    path: '',
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
      { path: '/test', component: () => import('pages/TestPage.vue')},
      { path: `/chat/room/enter/:roomId`, component: () => import('pages/chat/ChatRoomDetail.vue')},
      { path: '/chat/room', component: () => import('pages/chat/ChatRoomList.vue')},
      { path: '/property/map', component: () => import('pages/property/PropertyMap.vue')},
      { path: '/property/register', component: () => import('pages/property/RegisterPropertyPage')},
      { path: '/property/list', component: () => import('pages/property/PropertyListPage.vue')},
      { path: '/property/:propertyId', component: () => import('pages/property/PropertyDetailPage')},
      { path: '/my-page', component: () => import('pages/user-info/MyPage.vue')}
    ]
  },
  {
    path: '/property/main',
    component: () => import('layouts/PropertyMainLayout.vue'),
    children: [
      { path: '/property/main', component: () => import('pages/property/PropertyMainPage.vue')},
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
