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
import InputSearch from "../molecules/InputSearch.vue";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "CabysFinder",
  components: {
    InputSearch,
  },
  data() {
    return {
      items: [],
      result: null,
    };
  },
  computed: {
    ...mapGetters(["getProducts"]),
  },
  methods: {
    ...mapActions(["findProductByDescription"]),
    onOptionClicked(selectedValue) {
      this.result = selectedValue;
      this.$emit("selected", this.result);
    },
    onUserTyping(value) {
      this.findProductByDescription(value).then(() => {
        this.filterListProducts(value);
      });
    },
    filterListProducts(value) {
      this.items = [];
      this.items = this.getProducts.filter((item) => {
        return item.description.toLowerCase().includes(value.toLowerCase()) 
      });
    },
  }
};
</script>
<template>
  <div>
    <div>
      <b-card
        title="Buscador de código Cabys"
        sub-title="Resultado de selección"
        class="mb-3"
      >
        <b-card-text v-if="result">
          <b-alert show variant="success">
            <div>
              <span class="font-weight-bold">Código:</span> {{ result.code }}
            </div>
            <div>
              <span class="font-weight-bold">Descripción:</span>
              {{ result.description }}
            </div>
            <div>
              <span class="font-weight-bold">Impuesto:</span> {{ result.tax }}
            </div>
          </b-alert>
        </b-card-text>
      </b-card>
    </div>
    <input-search
      label="Escriba una descripción"
      :options="items"
      @typing="onUserTyping"
      @optionClicked="onOptionClicked"
      placeholder="Buscar por descripción"
    >
      <template v-slot:item="{ data }">
        <b-list-group-item
          v-if="data"
          class="border-0 border-bottom rounded-bottom rounded-lg bg-transparent"
        >
          {{ data.description }} {{ data.code }} -
          <span class="font-weight-bold text-blue">({{ data.tax }})</span>
        </b-list-group-item>
      </template>
    </input-search>
  </div>
</template>
