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
import '@babel/polyfill'
import 'mutationobserver-shim'
import Vue from 'vue'
import './plugins/bootstrap-vue'
import App from './App.vue'
import router from './router'
import store from './store'
import nprogress from './plugins/nprogress-vue';
import Keycloak from 'keycloak-js';
import VueTheMask from 'vue-the-mask'
import VueLogger from 'vuejs-logger';

Vue.config.productionTip = false

const isProduction = process.env.NODE_ENV === 'production';

const options = {
    isEnabled: true,
    logLevel: isProduction ? 'error' : 'debug',
    stringifyArguments: false,
    showLogLevel: true,
    showMethodName: true,
    separator: '|',
    showConsoleColors: true
};

Vue.use(VueTheMask)
Vue.use(VueLogger, options);

let initOptions = {
    url: isProduction? 'https://auth.aedeatech.com/auth' : 'http://localhost:8082/auth', realm: isProduction ? 'fortbiz-prod' : 'fortbiz', clientId: 'fortbiz-client', onLoad: 'login-required'
}

let keycloak = Keycloak(initOptions)

keycloak.init({onLoad: initOptions.onLoad}).then((auth) => {
    if (!auth) {
        window.location.reload();
    } else {

        store.state.users.accessToken = keycloak.token;
        store.state.users.accessTokenParsed = keycloak.tokenParsed;

        store.dispatch("getDefaultConfig");
        store.dispatch("verifyUser");

        new Vue({
            router,
            store,
            nprogress,
            render: h => h(App)
        }).$mount('#app')
    }

    setInterval(() => {
        keycloak.updateToken(70).then((refreshed) => {
            if (refreshed) {
                Vue.$log.info('Token refreshed' + refreshed);
                store.state.users.accessToken = keycloak.token;
            } else {
                Vue.$log.warn('Token not refreshed, valid for '
                        + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds');
            }
        }).catch(() => {
            Vue.$log.error('Failed to refresh token');
        });
    }, 6000)

}).catch(() => {
    Vue.$log.error("Authenticated Failed");
});

