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
import { mapGetters } from "vuex";

export default {
  name: "NavigationMenu",
  components: {
  },
  computed: {
    ...mapGetters(['getOidcUrl', 'getUsername']),
    getHostUri(){
      return window.location.protocol + "//" + window.location.hostname + (window.location.port === ''? "" :  ":" +  window.location.port )
    }
  }
}
</script>
<template>
  <div>
    <b-sidebar v-if="false" shadow id="sidebar-menu" title="Menú Principal">
      <nav class="mb-3">
        <b-nav vertical>
          <b-nav-item href="#">Dashboard</b-nav-item>
          <b-nav-item href="#">Sucursales</b-nav-item>
          <b-nav-item-dropdown boundary="" text="Facturación">
            <b-nav-item href="#/invoices">Facturas</b-nav-item>
            <b-dropdown-item href="#">Notas de débito</b-dropdown-item>
            <b-dropdown-item href="#">Notas de crédito</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-nav>
      </nav>
    </b-sidebar>
    <b-navbar toggleable="lg" type="dark" variant="primary">
      <b-button v-if="false" variant="primary" class="mr-2 text-white border-white" v-b-toggle.sidebar-menu>
        <b-icon icon="grid3x3-gap-fill"></b-icon>
      </b-button>
      <b-navbar-brand href="#/" class="brand-name">FortBiz</b-navbar-brand>
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav align="center">
      <b-navbar-nav menu-class="text-white">
          <b-nav-item href="#/">Dashboard</b-nav-item>
          <b-nav-item href="#/branches">Sucursales</b-nav-item>
          <b-nav-item-dropdown text="Facturación">
            <b-dropdown-item href="#/invoices">Facturas</b-dropdown-item>
            <b-dropdown-item href="#">Notas de débito</b-dropdown-item>
            <b-dropdown-item href="#">Notas de crédito</b-dropdown-item>
          </b-nav-item-dropdown>
      </b-navbar-nav>
        <b-navbar-nav class="ml-auto">
          <b-nav-item-dropdown class="" toggle-class="active" right menu-class="text-white">
            <template slot="button-content">
              <b-icon icon="person-circle"></b-icon>
              <span class="ml-1">{{ getUsername }}</span>
            </template>
            <b-dropdown-item :href="getOidcUrl + '/account'">Perfil</b-dropdown-item>
            <b-dropdown-item href="/fortbiz-web/#/credentials">Configuración de Hacienda</b-dropdown-item>
            <b-dropdown-item :href="getOidcUrl + '/protocol/openid-connect/logout?redirect_uri=' + getHostUri">Cerrar Sesión</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>
<style>
@import url("https://fonts.googleapis.com/css2?family=Fredoka+One&display=swap");

.brand-name {
  font-family: 'Fredoka One';
}
</style>
