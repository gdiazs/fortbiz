<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->
<script>
import {mapActions, mapGetters} from 'vuex';

export default {
  name: "BranchSwitcher",
  data() {
    return {
      selectedActiveBranch: localStorage.getItem('Kdkv7wc6ED6VeC7v'),
    }
  },
  methods: {
    ...mapActions(['updateActiveBranch', 'fetchUserBranches', 'fetchInvoices']),
    onChangeBranch() {
      this.updateActiveBranch(this.selectedActiveBranch);
      this.fetchInvoices(this.getActiveBranch)
    },
    shouldShowSwitcher(){
      return this.getUserBranches.length > 0;
    }
  },
  computed: {
    ...mapGetters(['getActiveBranch', 'getUserBranches'])
  },
  created() {
    this.fetchUserBranches().then(() => {
      if(this.getUserBranches.length === 1){
        this.selectedActiveBranch = this.getUserBranches[0].id;
        this.onChangeBranch();
      }
    });
    this.fetchInvoices(this.getActiveBranch)
  }
}
</script>

<template>
  <div class="w-100">
    <b-select @change="onChangeBranch"
              v-model="selectedActiveBranch"
              class="rounded"
              label-field="s"
    >
      <b-select-option :value="null" >
        Seleccione una sucursal
      </b-select-option>
      <b-select-option :key="branch.name" v-for="branch in getUserBranches" class="bg-white text-primary" :value="branch.id">
        {{ branch.name }}
      </b-select-option>
    </b-select>
  </div>
</template>

<style scoped>
</style>
