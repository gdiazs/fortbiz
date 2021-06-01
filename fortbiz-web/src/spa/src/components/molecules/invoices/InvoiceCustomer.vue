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
import StringFormatter from "@/utils/StringFormatter";


export default {
  name: "InvoiceCustomer",
  props: {
    invoiceNumber: {default: ''},
    editableCustomer: {type: Object, default: null}
  },
  watch:{
    customerIdNumber: function (value) {
      this.newInvoiceCustomer.identificationNumber = value;
      this.fillCustomerInfo(value)
    } 
  },
  data() {
    return {

      newInvoiceCustomer: {
        customerId:'',
        identificationType:null,
        identificationNumber: '',
        customerName: '',
        firstLastName: '',
        secondLastName: '',
        address: '',
        phoneNumber: '',
        invoiceNumber: this.invoiceNumber,
      },
      validateErrors: [],
      show: null,
      variantValue: "success",
      alertMessage: "",
      customerIdNumber: "",
      cedMask: ''
    }
  },
  computed: {
    ...mapGetters(['getCustomer'])
  },
  methods: {
    ...mapActions(['saveInvoiceCustomer', 'findInvoice','updateInvoiceCustomer','findCustomer']),

    saveCustomer() {
      this.validateForm();
      if (this.validateErrors.length > 0) {
        this.showAlertMessage('danger', true, 'Complete los campos correctamente')
      } else {
        if(this.editableCustomer){
          this.updateInvoiceCustomer(this.newInvoiceCustomer).then(() => {
            this.$bvModal.hide('invoice-customer')
            this.findInvoice(this.invoiceNumber);
          }).catch(() => {
            this.showAlertMessage('danger', true, 'Problemas al ingresar los datos, intente nuevamente')
          })
        }else{
          this.saveInvoiceCustomer(this.newInvoiceCustomer).then(() => {
            this.$bvModal.hide('invoice-customer')
            this.showAlertMessage('success', true, 'Credenciales validados y guardados satisfactoriamente')
            this.findInvoice(this.invoiceNumber);
          }).catch(() => {
            this.showAlertMessage('danger', true, 'Problemas al ingresar los datos, intente nuevamente')
          })
        }
      }
    },
    formatPhone(value){
      return StringFormatter.formatCrPhone(value);
    },
    showAlertMessage(variantValue, show, alertMessage) {
      this.variantValue = variantValue;
      this.show = show;
      this.alertMessage = alertMessage;
    },
    findError(field) {
      let error = this.validateErrors.find(error => error.field === field);
      return error ? false : null;
    },
    validateForm() {
      this.validateErrors = [];
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceCustomer.identificationNumber)) {
        this.validateErrors.push({field: 'identificationNumber'})
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceCustomer.address)) {
        this.validateErrors.push({field: 'address'})
      }else {
        if (!StringFormatter.validateEmail(this.newInvoiceCustomer.address)) {
          this.validateErrors.push({field: 'address'})
        }
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceCustomer.firstLastName)) {
        this.validateErrors.push({field: 'firstLastName'})
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceCustomer.customerName)) {
        this.validateErrors.push({field: 'customerName'})
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceCustomer.phoneNumber)) {
        this.validateErrors.push({field: 'phoneNumber'})
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceCustomer.secondLastName)) {
        this.validateErrors.push({field: 'secondLastName'})
      }
      if (ValidationUtils.isNull(this.newInvoiceCustomer.identificationType)) {
        this.validateErrors.push({field: 'identificationType'})
      }
    },
    fillCustomerInfo(identificationNumber){
      this.findCustomer(identificationNumber).then(() => {
        if(this.getCustomer){
            this.newInvoiceCustomer = this.getCustomer? this.getCustomer : {};
            this.newInvoiceCustomer.invoiceNumber = this.invoiceNumber
            this.validateErrors = [];
        } else {
          this.newInvoiceCustomer.identificationNumber = identificationNumber;
        }
      });
      
    },
    onIdentificationTypeChange(event){
      if(event){
        this.customerIdNumber = '';
      }
      if (this.newInvoiceCustomer.identificationType === '01'){
        this.cedMask = '0#-####-####';
      }
      if (this.newInvoiceCustomer.identificationType === '02'){
        this.cedMask = '3-###-######';
      }
    },
  },
  created() {
    if(this.editableCustomer){
      this.newInvoiceCustomer = this.editableCustomer;
      this.onIdentificationTypeChange();
      this.customerIdNumber = this.editableCustomer.identificationNumber;
     
      
    }
  }
}
</script>

<template>
  <div>
    <div class="row justify-content-md-center">
      <div class="col-12 col-md-12">

        <div class="row px-3">
          <b-alert class="col" :variant="variantValue" :show="show">{{ alertMessage }}</b-alert>
        </div>

        <b-form>
          <fieldset class="border py-2">
            <input v-model="newInvoiceCustomer.customerId" type="hidden"/>
            <div class="col-lg-12 m-auto">
              <b-form-group label="Tipo de identificación">
                <b-form-select @change="onIdentificationTypeChange" :state="findError('identificationType')" v-model="newInvoiceCustomer.identificationType">
                  <template #first>
                    <b-form-select-option :value="null" disabled>Seleccione una opción</b-form-select-option>
                  </template>
                  <b-form-select-option value="01">Cédula física</b-form-select-option>
                  <b-form-select-option value="02">Cédula jurídica</b-form-select-option>
                </b-form-select>
                <b-form-invalid-feedback :state="findError('identificationType')">
                  Elija una opción
                </b-form-invalid-feedback>
              </b-form-group>

              <b-form-group> 
                <label class="ml-2">
                  Identificación
                  <span class="ml-1">
                     <span id="disabled-wrapper" class="d-inline-block" tabindex="0">
                      <b-icon-info-circle/>
                    </span>
                    <b-tooltip target="disabled-wrapper">Al llenar este campo se cargará la información del cliente si el mismo ya existe dentro del sistema</b-tooltip>
                  </span>
                </label>

                <b-form-input v-mask="cedMask" :state="findError('identificationNumber')" placeholder="Número de identificación"
                              v-model="customerIdNumber"
                              debounce="500"
                ></b-form-input>
                <b-form-invalid-feedback :state="findError('identificationNumber')">
                  Escriba una identificación 
                </b-form-invalid-feedback>
              </b-form-group>

              <b-form-group label="Nombre">
                <b-form-input :state="findError('customerName')" placeholder="Ingrese el nombre"
                              v-model="newInvoiceCustomer.customerName"></b-form-input>
                <b-form-invalid-feedback :state="findError('customerName')">
                  Escriba un nombre
                </b-form-invalid-feedback>
              </b-form-group>

              <b-form-group label="Primer apellido">
                <b-form-input :state="findError('firstLastName')" placeholder="Ingrese el primer apellido"
                              v-model="newInvoiceCustomer.firstLastName"></b-form-input>
                <b-form-invalid-feedback :state="findError('firstLastName')">
                  Escriba el primer apellido
                </b-form-invalid-feedback>
              </b-form-group>

              <b-form-group label="Segundo apellido">
                <b-form-input :state="findError('secondLastName')" placeholder="Ingrese el segundo apellido"
                              v-model="newInvoiceCustomer.secondLastName"></b-form-input>
                <b-form-invalid-feedback :state="findError('secondLastName')">
                  Escriba el segundo apellido
                </b-form-invalid-feedback>
              </b-form-group>

              <b-form-group label="Correo electrónico">
                <b-form-input :state="findError('address')" placeholder="Ingrese el correo electrónico"
                              v-model="newInvoiceCustomer.address"></b-form-input>
                <b-form-invalid-feedback :state="findError('address')">
                  Escriba el correo electrónico válido
                </b-form-invalid-feedback>
              </b-form-group>

              <b-form-group label="Teléfono">
                <b-form-input :formatter="formatPhone" :state="findError('phoneNumber')" placeholder="Ingrese el teléfono"
                              v-model="newInvoiceCustomer.phoneNumber"></b-form-input>
                <b-form-invalid-feedback :state="findError('phoneNumber')">
                  Escriba el número de teléfono
                </b-form-invalid-feedback>
              </b-form-group>

            </div>
          </fieldset>
        </b-form>

        <div class="text-lg-right mt-4">
          <b-button @click="saveCustomer" class="col-12 col-lg-4" variant="primary">
            <span>Guardar</span>
          </b-button>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped>

</style>