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
import Vuex from 'vuex'
import axios from 'axios';
import BranchesStore from './branches';
import UsersStore from './users';
import InvoicesStore from './invoices';
import ProductsStore from './products';


Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        breadcrumb: {
            items: []
        },
        oidcUrl: '',
        env: ''
    },
    getters: {
        getItems(state) {
            return state.breadcrumb.items
        },
        getOidcUrl(state){
            return state.oidcUrl;
        },
        getEnv(state){
            return state.env
        }
    },
    mutations: {
        updateBreadCrumb(state, payload) {
            state.breadcrumb.items = payload
        },
        addConfig(state, config){
            state.oidcUrl = config.oidcUrl;
            state.env =  config.env;
        }
    },
    actions: {
        updateBreadCrumbAction({commit}, payload) {
            let pathArray = payload.path.split('/');
            if (pathArray.join("/").length == 1) {
                pathArray = [''];
            }
            const breadcrumbs = [];
            pathArray.forEach((value, index) => {
                const matched = payload.matched;
                breadcrumbs.push({
                    path: matched[index] ? matched[index] : '',
                    to: matched[index].path ? matched[index].path : '/',
                    text: matched[index].name
                })
            })
            commit('updateBreadCrumb', breadcrumbs)
        },
        getDefaultConfig({commit}){
            axios.get("/api/config").then((response) => {
                commit('addConfig', response.data.viewModel);
            })
        }
    },
    modules: {
        namespace: false,
        branches: BranchesStore,
        users: UsersStore,
        invoices: InvoicesStore,
        products: ProductsStore
    }

})
