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
import { mapActions, mapGetters } from "vuex";
import ValidationUtils from "../../../utils/ValidationUtils";
import CabysFinder from "../../organisms/CabysFinder";
export default {
  name: "invoiceDetail",
  props: {
    invoiceId: null,
    invoiceNumber: { default: "" },
    editableProduct: { type: Object, default: null },
  },
  components: {
    CabysFinder,
  },
  data() {
    return {
      selectedCabys: null,
      newInvoiceDetail: {
        measure: null,
        tax: "",
        cabys: "",
        description: "",
        quantity: "",
        price: "",
        invoiceId: this.invoiceId,
      },
      optionsMeasure: [],
      labelTax: null,
      validateErrors: [],
      show: null,
      variantValue: "success",
      alertMessage: "",
    };
  },
  methods: {
    ...mapActions([
      "addInvoiceDetail",
      "findInvoiceDetail",
      "updateInvoiceDetail",
    ]),

    saveInvoiceDetail() {
      this.validateForm();
      if (this.validateErrors.length > 0) {
        this.showAlertMessage(
          "danger",
          true,
          "Complete los campos correctamente"
        );
      } else {
        if (this.editableProduct) {
          this.updateInvoiceDetail(this.newInvoiceDetail).then(() => {
            this.$bvModal.hide("invoice-detail");
            this.findInvoiceDetail(this.invoiceNumber);
          });
        } else {
          this.addInvoiceDetail(this.newInvoiceDetail)
            .then(() => {
              this.$bvModal.hide("invoice-detail");
              this.findInvoiceDetail(this.invoiceNumber);
            })
            .catch();
        }
      }
    },
    onCabysSelection(selectedCabys) {
      this.selectedCabys = selectedCabys;
    },
    onCabysSelectedOk() {
      this.newInvoiceDetail.cabys = this.selectedCabys.code;
      this.newInvoiceDetail.description = this.selectedCabys.description;
      this.newInvoiceDetail.tax = this.selectedCabys.tax;
    },

    validateForm() {
      this.validateErrors = [];
      if (ValidationUtils.isNull(this.newInvoiceDetail.measure)) {
        this.validateErrors.push({ field: "measure" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceDetail.tax)) {
        this.validateErrors.push({ field: "tax" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceDetail.cabys)) {
        this.validateErrors.push({ field: "cabys" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceDetail.description)) {
        this.validateErrors.push({ field: "description" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceDetail.quantity)) {
        this.validateErrors.push({ field: "quantity" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newInvoiceDetail.price)) {
        this.validateErrors.push({ field: "price" });
      }
    },
    findError(field) {
      let error = this.validateErrors.find((error) => error.field === field);
      return error ? false : null;
    },
    showAlertMessage(variantValue, show, alertMessage) {
      this.variantValue = variantValue;
      this.show = show;
      this.alertMessage = alertMessage;
    },
    showModalSearchCabys() {
      this.$bvModal.show("search-cabys");
    }
  },
  created() {
    this.optionsMeasure = this.getInvoice.units;
    if (this.editableProduct) {
      this.newInvoiceDetail = this.editableProduct;
      this.newInvoiceDetail.measure = this.editableProduct.unit;
      this.newInvoiceDetail.invoiceId = this.invoiceId;
    }
  },
  computed: {
    ...mapGetters(["getInvoice"]),
  },
};
</script>

<template>
  <div>
    <div class="row justify-content-md-center">
      <div class="col-12 col-md-12">
        <div class="row px-5"></div>
        <b-alert class="col-12" :variant="variantValue" :show="show">{{
          alertMessage
        }}</b-alert>

        <b-form>
          <fieldset class="border p-3">
            <b-form-group label="Descripción de la línea">
              <b-form-input
                :state="findError('description')"
                v-model="newInvoiceDetail.description"
                placeholder="Ingrese una descripción del produto"
              ></b-form-input>
              <b-form-invalid-feedback :state="findError('description')">
                Escriba una descripción del producto
              </b-form-invalid-feedback>
            </b-form-group>

            <b-input-group class="mb-3">
              <label class="ml-2 w-100">Código CABYS</label>
              <b-form-input
                class="w-50"
                v-model="newInvoiceDetail.cabys"
                placeholder="Ingrese el código CABYS"
                :state="findError('cabys')"
              ></b-form-input>
              <b-button
                @click="showModalSearchCabys"
                variant="primary"
                class="col-12 col-md-5 ml-md-1"
              >
                <b-icon-search /> Abrir buscador</b-button
              >
              <b-form-invalid-feedback :state="findError('cabys')">
                Escriba un código cabys o presione el botón buscar para
                encontrarlo
              </b-form-invalid-feedback>
            </b-input-group>

            <b-modal
              id="search-cabys"
              size="lg"
              :no-close-on-backdrop="true"
              @ok="onCabysSelectedOk"
            >
              <cabys-finder @selected="onCabysSelection" />
            </b-modal>

            <b-form-group label="Impuesto">
              <b-form-input
                :state="findError('tax')"
                v-model="newInvoiceDetail.tax"
                placeholder="Ingrese el impuesto"
              ></b-form-input>
              <b-form-invalid-feedback :state="findError('tax')">
                Escriba un impuesto
              </b-form-invalid-feedback>
            </b-form-group>

            <b-form-group label="Unidad de medida">
              <b-form-select
                :state="findError('measure')"
                v-model="newInvoiceDetail.measure"
                :options="optionsMeasure"
              >
                <b-form-select-option :value="null"
                  >Seleccione una opción</b-form-select-option
                >
              </b-form-select>
              <b-form-invalid-feedback :state="findError('measure')">
                Seleccione una unidad de medida
              </b-form-invalid-feedback>
            </b-form-group>

            <b-form-group label="Cantidad">
              <b-form-input
                v-model="newInvoiceDetail.quantity"
                placeholder="Ingrese la cantidad"
                :state="findError('description')"
              ></b-form-input>
              <b-form-invalid-feedback :state="findError('quantity')">
                Escriba una cantidad
              </b-form-invalid-feedback>
            </b-form-group>

            <b-form-group label="Precio unitario">
              <b-form-input
                v-model="newInvoiceDetail.price"
                placeholder="Ingrese el precio unitario"
                :state="findError('price')"
              ></b-form-input>
              <b-form-invalid-feedback :state="findError('price')">
                Escriba un precio
              </b-form-invalid-feedback>
            </b-form-group>
          </fieldset>
        </b-form>

        <div class="text-right mt-5">
          <b-button variant="primary" class="col-12 col-lg-4" @click="saveInvoiceDetail()"
            >Agregar</b-button
          >
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
