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
import BFooter from "@/components/molecules/BFooter";
import NavigationMenu from "@/components/molecules/NavigationMenu";
import NprogressContainer from 'vue-nprogress/src/NprogressContainer'
import { mapGetters } from 'vuex'
export default {
  name: "default-layout",
  components: {
    BFooter,
    NavigationMenu,
    NprogressContainer,
  },
  computed: {
    ...mapGetters(['getItems'])
  },
  beforeCreate() {
  }
}
</script>
<template>
  <div>
    <nprogress-container />
    <navigation-menu />
    <b-breadcrumb class="bg-white" :items="getItems" />
    <div class="container p-lg-4">
      <slot/>
      <router-view></router-view>
    </div>
    <div class="mb-5"></div>
    <b-footer  />
  </div>
</template>
<style>
</style>
