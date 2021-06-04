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
        branches: [],
    },
    mutations: {
        setBranches(state, data){
            state.branches = data;
        },
        addNewBranch(state, data){
            state.branches.push(data);
        },
        setUpdatedBranch(state, data) {
            let foundBranch = state.branches.find( (aBranch) => aBranch.id === data.id);
            Object.assign(foundBranch, data);
        },
        removeBranchById(state, branchId) {
             state.branches.find( (aBranch, index) => {
                 if(aBranch.id === branchId){
                     state.branches.splice(index, 1);
                     return;
                 }
             });
        }
    },
    getters: {
        getUserBranches(state){
            return state.branches;
        }
    },
    actions: {
        addBranch(store, branch){
           return axios.post('/fortbiz-web/api/branches/v1', branch).then( response => {
                store.commit("addNewBranch", response.data);
            })
        },
        updateBranch(store, branch){
            return axios.put('/fortbiz-web/api/branches/v1', branch).then( response => {
                store.commit("setUpdatedBranch", response.data);
            })
        },
        fetchBranchesByUserId(store){
            
            axios.get(`/fortbiz-web/api/users/${store.rootState.users.accessTokenParsed.sub}/branches`).then( response => {
                store.commit("setBranches", response.data);
            })
        },
        fetchUserBranches(store){
            return axios.get(`/fortbiz-web/api/users/${store.rootState.users.accessTokenParsed.sub}/branches`).then( response => {
                store.commit("setBranches", response.data);
            })
        },
        deleteBranchById(store, branchId){
            
            return axios.delete(`/fortbiz-web/api/branches/v1/${branchId}`).then(() => {
                store.commit("removeBranchById", branchId);
            });
        }
    }

}
