/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store/index'
import Home from '../components/pages/Home';
import CatalogsRoutes from '@/components/pages/catalogs/CatalogsRoutes'
import ConfigurationRoutes from "@/components/pages/configuration/ConfigurationRoutes";
import InvoicesRoutes from "@/components/pages/invoices/InvoicesRoutes";
import nprogress from "@/plugins/nprogress-vue";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Inicio',
        component: Home,
        children: [
            CatalogsRoutes,
            ConfigurationRoutes,
            InvoicesRoutes
        ]
    },

]

const router = new VueRouter({
    routes
})
router.beforeEach((to, from, next) => {
    if (to) {
        nprogress.done();
    }
    store.dispatch('updateBreadCrumbAction', to).then(() => {
        next()
    }).catch(() => {
        next()
    })
})


router.afterEach((to) => {
    if (to) {
        nprogress.start();
    }

})


export default router
