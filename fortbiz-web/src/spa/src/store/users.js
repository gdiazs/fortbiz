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
import axios from 'axios';

export default {
    state: {
        accessToken: null,
        accessTokenParsed: null,
        userExits: null,
        userId: null,
        activeBranch: localStorage.getItem('Kdkv7wc6ED6VeC7v'),
        credentials: [],
        provinces: [],
        cantons: [],
        districts: [],
        userActivities: [],
    },
    mutations: {
        addNewCredential(state, data){
            state.credentials.push(data);
        },
        setUser(state, data) {
            state.userExits = data.exists;
            state.userId = data.userId;
        },
        setCredentials(state, data){
            state.credentials = data;
        },
        removeCredentialById(state, credentialId) {
            state.credentials.find( (aCredential, index) => {
                if(aCredential.id === credentialId){
                    state.credentials.splice(index, 1);
                    return;
                }
            });
        },
        setUpdatedCredential(state, data) {
            let foundCredential = state.credentials.find( (aCredential) => aCredential.id === data.id);
            foundCredential.password = null;
            foundCredential.pin = null;

        },
        setActiveBranch(state, data){
            state.activeBranch = data;
        },
        setListProvinces(state, data){
            state.provinces = data;
        },
        setListCantons(state, data){
            state.cantons = data;
            state.cantons.push({id:"null", name:"Seleccione una opción"})
        },
        setListDistricts(state, data){
            state.districts = data;
            state.districts.push({id:"null", name:"Seleccione una opción"})
        },
        setUserActivities(state, data){
            state.userActivities = [];
            state.userActivities.push({codigo: null, descripcion: "Seleccione una opción" });
            state.userActivities.push(...data.actividades);

        }
    },
    getters: {
        getUserId(state){
            return state.accessTokenParsed['sub'];
        },
        getUsername(state){
            return state.accessTokenParsed['preferred_username'];
        },
        getCredentials(state){
            return state.credentials;
        },
        getActiveBranch(state){
            return state.activeBranch ;
        },
        getListProvinces(state){
            return state.provinces.map( (local) => (
                { value: local.id, text: local.name})
            );
        },
        getListCantons(state){
            return state.cantons.map( (local) => (
                { value: local.id, text: local.name})
            );
        },
        getListDistricts(state){
            return state.districts.map( (local) => (
                { value: local.id, text: local.name})
            );
        },
        getUserActivities(state){
            return state.userActivities.map( (activity) => (
                { value: activity.codigo, text: activity.descripcion})
            );
        }
    },
    actions: {
        addCredentials(store, credentials){
            const formData = new FormData();
            formData.append('identificationNumber', credentials.identificationNumber.replaceAll("-", ""));
            formData.append("username", credentials.username);
            formData.append('password', credentials.password);
            formData.append('keystore', credentials.keystore);
            formData.append('pin', credentials.pin);
            formData.append('tradeName', credentials.tradeName);
            formData.append('identificationType', credentials.identificationType);
            formData.append('economicActivity', credentials.economicActivity);
            formData.append('activityCode', credentials.activityCode);
            formData.append('localization', `${credentials.province}/${credentials.canton}/${credentials.district}` )
            formData.append("brandImage", credentials.brandImage)

            return axios.post('/fortbiz-web/api/credentials', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(response => {store.commit("addNewCredential", response.data)});

        },
        fetchCredentialsByUserId(store){
            axios.get(`/fortbiz-web/api/users/${store.state.accessTokenParsed.sub}/credentials`).then( response => {
                store.commit("setCredentials", response.data);
            })
        },
        deleteCredentialById(store, userId){
            return axios.delete(`/fortbiz-web/api/credentials/${userId}`).then(() => {
                store.commit("removeCredentialById", userId);
            })
        },
        updateCredential(store, credentials){
            const formData = new FormData();
            formData.append('id', credentials.id);
            formData.append('identificationNumber', credentials.identificationNumber.replaceAll("-", ""));
            formData.append("username", credentials.username);
            formData.append('password', credentials.password);
            formData.append('keystore', credentials.keystore);
            formData.append('pin', credentials.pin);
            formData.append('tradeName', credentials.tradeName);
            formData.append('identificationType', credentials.identificationType);
            formData.append('economicActivity', credentials.economicActivity);
            formData.append('activityCode', credentials.activityCode);
            formData.append('localization', `${credentials.province}/${credentials.canton}/${credentials.district}` )
            formData.append("brandImage", credentials.brandImage)

            return axios.put('/fortbiz-web/api/credentials', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(response => {
                store.commit("setUpdatedCredential", response.data)
            });
        },

        updateActiveBranch(store, activeBranch){
            localStorage.setItem('Kdkv7wc6ED6VeC7v', activeBranch);
            store.commit("setActiveBranch", activeBranch)

        },
        fetchProvinces(store){
            return axios.get('/fortbiz-web/api/addresses/provinces').then(response => {
                store.commit('setListProvinces' , response.data);
            })
        },
        findCantontsByProvinceId(store, idProvince){
            return axios.get(`/fortbiz-web/api/addresses/${idProvince}/cantons`).then(response => {
                store.commit('setListCantons' , response.data);
            })
        },
        findDistrictsByCantonId(store, data){
            return axios.get(`/fortbiz-web/api/addresses/${data.idProvince}/cantons/${data.idCanton}/disctrics`).then(response => {
                store.commit('setListDistricts' , response.data);
            })
        },
        verifyUser(store){
            axios.post('/fortbiz-web/api/users/verify').then( response => {
                store.commit("setUser", response.data);
            })
        },
        validateCredentials(store, credentialId){
            return axios.get(`/fortbiz-web/api/credentials/${credentialId}/validate`);
        },
        findHaciendaActivityByIdentification(store, identificationNumber){
            return axios.get(`/fortbiz-web/api/credentials/hacienda-actvities/${identificationNumber}`).then((response) => {
                store.commit("setUserActivities", response.data)
            });
        }
    }
}
