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
import StringFormatter from "@/utils/StringFormatter";
import ValidationUtils from "@/utils/ValidationUtils";

export default {
  name: "BranchesAdd",
  props: {
    editableBranch: { type: Object, default: null },
  },
  methods: {
    ...mapActions(["addBranch", "updateBranch", "fetchCredentialsByUserId"]),
    validateForm() {
      this.validationErrors = [];
      if (ValidationUtils.isEmptyOrNull(this.newBranch.name)) {
        this.validationErrors.push({ field: "name" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newBranch.address)) {
        this.validationErrors.push({ field: "address" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newBranch.email)) {
        this.validationErrors.push({ field: "email" });
      } else {
        if (!StringFormatter.validateEmail(this.newBranch.email)) {
          this.validationErrors.push({ field: "email" });
        }
      }
      if (ValidationUtils.isEmptyOrNull(this.newBranch.phone)) {
        this.validationErrors.push({ field: "phone" });
      }
    },
    findError(field) {
      let error = this.validationErrors.find((error) => error.field === field);
      return error ? false : null;
    },
    saveBranch() {
      this.validateForm();
      if (this.validationErrors.length > 0) {
        this.showAlertMessage(
          "danger",
          true,
          "Hay algunos errores, por favor corrígalos"
        );
      } else {
        if (this.editableBranch) {
          this.updateBranch(this.newBranch)
            .then(() => {
              this.showAlertMessage(
                "success",
                true,
                "Sucursal actualizada satisfactoriamente"
              );
              this.cleanForm();
            })
            .catch(() => {
              this.showAlertMessage(
                "danger",
                true,
                "Ha ocurrido un error, intente nuevamente"
              );
            });
        } else {
          this.addBranch(this.newBranch)
            .then(() => {
              this.showAlertMessage(
                "success",
                true,
                "Sucursal creada satisfactoriamente"
              );
              this.cleanForm();
            })
            .catch(() => {
              this.showAlertMessage(
                "danger",
                true,
                "Ha ocurrido un error, intente nuevamente"
              );
            });
        }
      }
    },
    showAlertMessage(variantValue, show, alertMessage) {
      this.variantValue = variantValue;
      this.show = show;
      this.alertMessage = alertMessage;
    },
    cleanForm() {
      this.newBranch = {
        name: "",
        description: "",
        address: "",
        phone: "",
        email: "",
        credentialId: "",
      };
    },
    formatPhone(value) {
      return StringFormatter.formatCrPhone(value);
    },
  },
  computed: {
    ...mapGetters(["getCredentials"]),
    getCredentialsOptions() {
      const options = [{ value: "", text: "Seleccione una opción" }];
      this.getCredentials.forEach(function(credential) {
        options.push({ value: credential.id, text: credential.tradeName });
      });
      return options;
    },
  },
  data() {
    return {
      newBranch: {
        name: "",
        description: "",
        address: "",
        phone: "",
        email: "",
        credentialId: "",
      },
      show: null,
      variantValue: "success",
      alertMessage: "",
      validationErrors: [],
    };
  },
  created() {
    this.cleanForm();
    this.fetchCredentialsByUserId();

    if (this.editableBranch) {
      this.newBranch = this.editableBranch;
      this.newBranch.phone = this.formatPhone(this.newBranch.phone);
    }
  },
};
</script>
<template>
  <div>
    <div class="row px-5">
      <b-alert class="col" :variant="variantValue" :show="show"
        ><span v-html="alertMessage"
      /></b-alert>
    </div>
    <div class="row">
      <div class="col-12 col-md-6">
        <b-form>
          <fieldset class="border px-4 py-2">
            <legend>
              Información General
            </legend>

            <b-form-group label="Nombre:">
              <b-form-input
                :state="findError('name')"
                v-model="newBranch.name"
                placeholder="Escriba el nombre"
                type="text"
              ></b-form-input>
              <b-form-invalid-feedback :state="findError('name')">
                Escriba un nombre válido
              </b-form-invalid-feedback>
            </b-form-group>

            <b-form-group label="Teléfono:">
              <b-form-input
                :formatter="formatPhone"
                :state="findError('phone')"
                v-model="newBranch.phone"
                placeholder="Escriba el teléfono"
                type="text"
              ></b-form-input>
              <b-form-invalid-feedback :state="findError('phone')">
                Escriba un número de teléfono válido
              </b-form-invalid-feedback>
            </b-form-group>

            <b-form-group label="Correo electrónico:">
              <b-form-input
                :state="findError('email')"
                v-model="newBranch.email"
                type="text"
                placeholder="Escriba el correo electrónico"
              ></b-form-input>
              <b-form-invalid-feedback :state="findError('email')">
                Escriba un correo electrónico válido
              </b-form-invalid-feedback>
            </b-form-group>

            <b-form-group label="Dirección:">
              <b-form-input
                :state="findError('address')"
                v-model="newBranch.address"
                placeholder="Escriba la dirección"
                type="text"
              ></b-form-input>
              <b-form-invalid-feedback :state="findError('address')">
                Escriba una dirección válida
              </b-form-invalid-feedback>
            </b-form-group>

            <b-form-group label="Descripción:">
              <b-form-input
                v-model="newBranch.description"
                placeholder="Escriba una descripción"
                type="text"
              ></b-form-input>
            </b-form-group>
          </fieldset>
        </b-form>
      </div>
      <div class="col-12 col-md-6">
        <b-form>
          <fieldset class="border px-4 py-2">
            <b-alert show variant="info"
              >Los credenciales del emisor permiten a la sucursal facturar, si
              no ha agregado uno todavía, puede realizarlo
              <a href="#/config/credentials">aquí</a></b-alert
            >
            <legend>Configuración del emisor de facturas</legend>
            <b-form-group label="Asignar credenciales del emisor">
              <b-form-select
                v-model="newBranch.credentialId"
                :options="getCredentialsOptions"
              >
              </b-form-select>
            </b-form-group>
          </fieldset>
        </b-form>
      </div>
    </div>

    <div class="border gray-200 h-1 w-100 my-4"></div>
    <div class="text-right">
      <b-button variant="primary" class="col-12 col-lg-auto" @click="saveBranch"
        >Guardar sucursal</b-button
      >
    </div>
  </div>
</template>

<style scoped></style>
