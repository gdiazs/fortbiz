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
import {mapActions, mapGetters} from "vuex";
import ValidationUtils from "@/utils/ValidationUtils";

export default {
  name: "InvoicesHeader",

  data() {
    return {
      newInvoiceHeader: {
        paymentMethod: ['01'],
        currency: null,
        invoiceType: null,
        branchId: ''
      },
      validateErrors: [],
      show: null,
      variantValue: "success",
      alertMessage: "",
    }
  },
  methods: {
    ...mapActions(['addInvoiceHeader']),

    validateForm() {
      this.validateErrors = [];
      if (ValidationUtils.isNull(this.newInvoiceHeader.invoiceType)) {
        this.validateErrors.push({field: 'invoiceType'})
      }
      if (ValidationUtils.isNull(this.newInvoiceHeader.currency)) {
        this.validateErrors.push({field: 'currency'})
      }


    },
    findError(field) {
      let error = this.validateErrors.find(error => error.field === field);
      return error ? false : null;
    },

    saveInvoiceHeader() {
      this.validateForm();
      if (this.validateErrors.length > 0) {
        this.showAlertMessage('danger', true, 'Hay algunos errores, por favor corrígalos');
      } else {
        this.newInvoiceHeader.branchId = this.getActiveBranch;
        this.addInvoiceHeader(this.newInvoiceHeader)
            .then((response) => {
              this.$bvModal.hide('invoice-header')
              const invoiceNumber = response.data.invoiceNumber;
              this.$router.push({ path: `/invoices/${invoiceNumber}` })
            })
            .catch(() => {
              this.showAlertMessage('danger', true, 'Ha ocurrido un error, intente nuevamente');
        });
      }
    },
    showAlertMessage(variantValue, show, alertMessage) {
      this.variantValue = variantValue;
      this.show = show;
      this.alertMessage = alertMessage;
    }
  },

  computed: {
    ...mapGetters(['getActiveBranch']),
  }
}
</script>

<template>
  <div>
    <div class="row justify-content-md-center">
      <div class="col-12 col-md-10">

        <div class="row px-5">
          <b-alert class="col" :variant="variantValue" :show="show">{{ alertMessage }}</b-alert>
        </div>

        <div class="row px-5" v-if="!getActiveBranch">
          <b-alert show variant="info">Debe seleccionar una sucursal para emitir facturas, si no tiene sucursales puede crear una <a href="#/catalogs/branches">aquí</a>
            posteriormente seleccione la sucursal en la barra de selección.</b-alert>
        </div>

        <b-form>
          <fieldset class="border px-4 py2">
            <div class="m-auto col-lg-12">
              
              <b-form-group label="Tipo de factura">
                <b-form-select :state="findError('invoiceType')" v-model="newInvoiceHeader.invoiceType">
                  <template #first>
                    <b-form-select-option :value="null" disabled>Seleccione una opción</b-form-select-option>
                  </template>
                  <b-form-select-option value="01">Contado</b-form-select-option>
                </b-form-select>
                <b-form-invalid-feedback :state="findError('invoiceType')">
                  Seleccione el tipo de factura
                </b-form-invalid-feedback>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Forma de pago">
                <b-form-checkbox-group id="checkbox-group" v-model="newInvoiceHeader.paymentMethod">
                  <b-form-checkbox value="01">Efectivo</b-form-checkbox>
                  <b-form-checkbox value="02">Tarjeta</b-form-checkbox>
                  <b-form-checkbox value="04">Transferencia</b-form-checkbox>
                </b-form-checkbox-group>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Moneda">
                <b-form-select :state="findError('currency')" v-model="newInvoiceHeader.currency">
                  <template #first>
                    <b-form-select-option :value="null" disabled>Seleccione una opción</b-form-select-option>
                  </template>
                  <b-form-select-option value="CRC">CRC-Colón Costarricense</b-form-select-option>
                  <b-form-select-option value="USD">USD-Dólar Americano</b-form-select-option>
                </b-form-select>
                <b-form-invalid-feedback :state="findError('currency ')">
                  Seleccione el tipo de moneda
                </b-form-invalid-feedback>
              </b-form-group>

            </div>
          </fieldset>
        </b-form>

      </div>
    </div>

    <div class="text-lg-right mt-4">
      <b-button @click="saveInvoiceHeader"  class="col-md-4 col-12" variant="primary">
        <span>Guardar</span>
      </b-button>
    </div>

  </div>
</template>

<style scoped>

</style>