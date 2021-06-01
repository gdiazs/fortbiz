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
import ValidationUtils from "@/utils/ValidationUtils";
import StepsSection from "@/components/atoms/StepsSection";
import ImageUploader from "vue-image-upload-resize";

export default {
  name: "CredentialsForm",
  props: {
    editableCredential: { type: Object, default: null },
  },
  components: {
    StepsSection,
    ImageUploader,
  },
  methods: {
    ...mapActions([
      "addCredentials",
      "updateCredential",
      "fetchProvinces",
      "findCantontsByProvinceId",
      "findDistrictsByCantonId",
      "cleanListLocalizations",
      "fetchCredentialsByUserId",
      "findHaciendaActivityByIdentification"
    ]),
    validateFormStep1() {
      this.validationErrors = [];
      if (
        ValidationUtils.isEmptyOrNull(this.newCredential.identificationNumber)
      ) {
        this.validationErrors.push({ field: "identificationNumber" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newCredential.tradeName)) {
        this.validationErrors.push({ field: "tradeName" });
      }
      console.log(this.newCredential.activityCode)
      if (ValidationUtils.isEmptyOrNull(this.newCredential.activityCode)) {
        this.validationErrors.push({ field: "economicActivity" });
      }
    },
    validateFormStep2() {
      this.validationErrors = [];
      if (ValidationUtils.isEmptyOrNull(this.newCredential.username)) {
        this.validationErrors.push({ field: "username" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newCredential.password)) {
        this.validationErrors.push({ field: "password" });
      }
      if (ValidationUtils.isEmptyOrNull(this.newCredential.pin)) {
        this.validationErrors.push({ field: "pin" });
      }
    },
    findError(field) {
      let error = this.validationErrors.find((error) => error.field === field);
      if (error) this.isLoading = false;

      return error ? false : null;
    },
    saveCredentials() {
      this.hasBackendErrors = false;
      this.isLoading = true;
      this.validateFormStep2();
      if (this.validationErrors.length > 0) {
        this.showAlertMessage(
          "danger",
          true,
          "Complete los campos correctamente"
        );
        this.isLoading = false;
      } else {
        if (this.editableCredential) {
          this.updateCredential(this.newCredential)
            .then(() => {
              this.handleBackendSuccess();
            })
            .catch(() => {
              this.showAlertMessage(
                "danger",
                true,
                "Ha ocurrido un error, intente nuevamente"
              );
              this.isLoading = false;
            });
        } else {
          this.addCredentials(this.newCredential)
            .then((response) => {
              this.handleBackendSuccess(response);
            })
            .catch((error) => {
              this.handleBackendError(error);
            });
        }
      }
    },
    showAlertMessage(variantValue, show, alertMessage) {
      this.variantValue = variantValue;
      this.showAlert = show;
      this.alertMessage = alertMessage;
    },
    handleBackendSuccess() {
      this.showAlertMessage(
        "success",
        true,
        "Credenciales validados y guardados satisfactoriamente"
      );
      this.isLoading = false;
      this.cleanForm();
    },
    handleBackendError(error) {
      this.showAlertMessage(
        "danger",
        true,
        "Ha ocurrido un error, intente nuevamente"
      );
      const response = error.response;
      if (response.status === 400) {
        this.hasBackendErrors = true;
        if (response.data.error === "Invalid Hacienda credentials") {
          this.backendValidationMessage =
            "Usuario o contraseña de Hacienda inválidos, verifique los datos en ATV";
          this.validationErrors.push({ field: "username" });
          this.validationErrors.push({ field: "password" });
        }
        if (response.data.error === "Invalid Keystore PIN") {
          this.backendValidationMessage =
            "Llave privada o PIN de Hacienda inválidos, verifique los datos en ATV";
          this.validationErrors.push({ field: "pin" });
        }
      }
      this.isLoading = false;
    },
    cleanForm() {
      this.newCredential = {
        identificationNumber: "",
        username: "",
        password: "",
        keystore: null,
        tradeName: "",
        identificationType: "",
        economicActivity: "",
        pin: "",
        province: "",
        canton: "",
        district: "",
        brandImage: "",
      };
      this.disableCanton = true;
      this.disableDistrict = true;
    },
    onIdentificationTypeChange(event) {
      if (event) {
        this.customerHaciendaIdNumber = "";
      }

      if (this.newCredential.identificationType === "1") {
        this.cedMask = "#-####-####";
      }
      if (this.newCredential.identificationType === "2") {
        this.cedMask = "3-###-######";
      }
    },
    onProvinceSelectChange() {
      this.enableCanton = false;
      this.findCantontsByProvinceId(this.newCredential.province).then();
    },
    onCantonSelectChange() {
      this.enableDistrict = false;
      this.findDistrictsByCantonId({
        idProvince: this.newCredential.province,
        idCanton: this.newCredential.canton,
      }).then();
    },

    nextStep() {
      this.validateFormStep1();
      if (this.validationErrors.length == 0) {
        this.step += 1;
      }
    },
    backStep() {
      this.step -= 1;
    },
    setImage(output) {
      this.hasImage = true;
      this.image = output;
      this.newCredential.brandImage = this.image.dataUrl;
    },
    isImageLodeadAlready() {
      return (
        !this.hasImage &&
        this.newCredential.brandImage !== null &&
        this.newCredential.brandImage.length > 0
      );
    },
    onActivityDescriptionChange(selectedValue) {
       this.newCredential.activityCode = selectedValue;
    },
    onCustomerIdentificationChange(value){
      this.findHaciendaActivityByIdentification(this.newCredential.identificationNumber.replaceAll("-", ""));
    }
  },
  data() {
    return {
      isReady: false,
      firstStep: 1,
      secondStep: 2,
      thirdStep: 3,
      isLoading: false,
      cedMask: "",
      newCredential: {
        identificationNumber: "",
        username: "",
        password: "",
        pin: "",
        keystore: null,
        tradeName: "",
        identificationType: null,
        economicActivity: null,
        activityCode: "",
        province: "",
        canton: "",
        district: "",
        brandImage: "",
      },
      errors: [],
      showAlert: null,
      variantValue: "success",
      alertMessage: "",
      backendValidationMessage: "",
      hasBackendErrors: false,
      customerHaciendaIdNumber: "",

      identificationTypes: [
        { value: "", text: "Seleccione" },
        { value: "1", text: "Cédula Física" },
        { value: "2", text: "Cédula Jurídica" },
      ],
      validationErrors: [],
      enableCanton: true,
      enableDistrict: true,
      step: 1,
      image: null,
      hasImage: false,
    };
  },
  created() {
    this.fetchProvinces().then();
    this.cleanForm();
    if (this.editableCredential) {
      this.newCredential = this.editableCredential;
      this.findHaciendaActivityByIdentification(this.editableCredential.identificationNumber.replaceAll("-", ""));
      this.onIdentificationTypeChange();
      this.onProvinceSelectChange();
      this.onCantonSelectChange();
    }
  },
  computed: {
    ...mapGetters([
      "getListProvinces",
      "getListCantons",
      "getListDistricts",
      "getUserActivities",
    ]),
  },
  updated(){
   
  },
  destroyed() {
    this.fetchCredentialsByUserId();
  },
};
</script>

<template>
  <div v-if="true"> 
    <div class="row justify-content-md-center">
      <div class="col-12">
        <div class="row px-3">
          <b-alert class="col" :variant="variantValue" :show="showAlert">{{
            alertMessage
          }}</b-alert>
        </div>
        <steps-section :current-step="step" :steps-count="3" />
        <b-form v-if="step === firstStep">
          <fieldset class="border py-2">
            <legend><h4>Información de la empresa</h4></legend>
            <div class="d-flex flex-row m-auto col-12">
              <b-form-group label="Tipo" class="w-40">
                <b-select
                  :state="findError('identificationType')"
                  :options="identificationTypes"
                  v-model="newCredential.identificationType"
                  @change="onIdentificationTypeChange"
                ></b-select>
              </b-form-group>

              <b-form-group label="Identificación" class="w-60">
                <b-form-input v-mask="cedMask" :state="findError('identificationNumber')" placeholder="Número de identificación"
                              v-model="newCredential.identificationNumber"
                              debounce="100"
                              @change="onCustomerIdentificationChange"
                ></b-form-input>
                <b-form-invalid-feedback
                  :state="findError('identificationNumber')"
                >
                  Escriba una identificación
                </b-form-invalid-feedback>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Actividad económica">
                <b-select
                  :options="getUserActivities"
                  v-model="newCredential.activityCode"
                  @change="onActivityDescriptionChange"
                ></b-select>
                <b-form-invalid-feedback :state="findError('economicActivity')">
                  Seleccione una actividad económica
                </b-form-invalid-feedback>
              </b-form-group>
            </div>
            <div class="m-auto col-lg-12">
              <b-form-group label="Nombre comercial de la empresa">
                <b-form-input
                  :state="findError('tradeName')"
                  class="form-control"
                  type="text"
                  placeholder="Escriba el nombre comercial"
                  v-model="newCredential.tradeName"
                ></b-form-input>
                <b-form-invalid-feedback :state="findError('tradeName')">
                  Escriba un nombre comercial
                </b-form-invalid-feedback>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Provincia">
                <b-select
                  @change="onProvinceSelectChange"
                  :options="getListProvinces"
                  v-model="newCredential.province"
                ></b-select>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Cantón">
                <b-select
                  @change="onCantonSelectChange"
                  :options="getListCantons"
                  :disabled="enableCanton"
                  v-model="newCredential.canton"
                ></b-select>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Distrito">
                <b-select
                  :options="getListDistricts"
                  :disabled="enableDistrict"
                  v-model="newCredential.district"
                ></b-select>
              </b-form-group>
            </div>
          </fieldset>
        </b-form>
        <b-form v-if="step === secondStep" class="col-mt-4 mt-3">
          <fieldset class="border py-2">
            <legend>
              <h4>Logotipo de la factura</h4>
              <span class="text-muted">Este es un paso opcional </span>
            </legend>
            <div class="px-2 pb-2">
              <b-alert show variant="info"
                >Eliga una imagen de su preferencia, esta se usará como
                <span class="font-weight-bold">logotipo</span> en la confección
                de las facturas.</b-alert
              >
              <div class="image-section">
                <img
                  class="loadedPreviewImg text-center"
                  v-if="isImageLodeadAlready()"
                  width="460"
                  height="auto"
                  :src="newCredential.brandImage"
                />
                <image-uploader
                  class="text-center"
                  :maxWidth="460"
                  :maxHeight="460"
                  :preview="true"
                  :className="['fileinput', { 'fileinput--loaded': hasImage }]"
                  capture="environment"
                  :debug="1"
                  doNotResize="gif"
                  :autoRotate="true"
                  outputFormat="verbose"
                  @input="setImage"
                ></image-uploader>
              </div>
            </div>
          </fieldset>
        </b-form>
        <b-form v-if="step === thirdStep" class="col-mt-4 mt-3">
          <fieldset class="border py-2">
            <legend><h4>Datos de conexión con hacienda</h4></legend>

            <div class="m-auto col-lg-12">
              <b-form-group label="Nombre de usuario de hacienda">
                <b-form-input
                  :state="findError('username')"
                  class="form-control"
                  type="text"
                  v-model="newCredential.username"
                  placeholder="Escriba nombre de usuario"
                ></b-form-input>
                <b-form-invalid-feedback :state="findError('username')">
                  Escriba un nombre de usuario
                </b-form-invalid-feedback>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Contraseña de hacienda">
                <b-form-input
                  :state="findError('password')"
                  type="password"
                  v-model="newCredential.password"
                  placeholder="Contraseña"
                ></b-form-input>
                <b-form-invalid-feedback :state="findError('password')">
                  Escriba una contraseña correcta
                </b-form-invalid-feedback>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Llave criptográfica">
                <div>
                  <b-form-file
                    class="border gray-200 p-2"
                    v-model="newCredential.keystore"
                    :state="!newCredential.keystore"
                    plain
                  ></b-form-file>
                </div>
              </b-form-group>
            </div>

            <div class="m-auto col-lg-12">
              <b-form-group label="Pin de la llave criptográfica">
                <b-form-input
                  :state="findError('pin')"
                  type="password"
                  v-model="newCredential.pin"
                  placeholder="Escriba el pin"
                ></b-form-input>
                <b-form-invalid-feedback :state="findError('pin')">
                  Escriba un pin válido
                </b-form-invalid-feedback>
              </b-form-group>
            </div>
          </fieldset>
        </b-form>
      </div>
    </div>
    <div class="border gray-200 h-1 w-100 my-4"></div>
    <div class="text-lg-right text-center">
      <span v-if="hasBackendErrors" class="col-12 col-lg-8 text-danger">
        <b-icon-exclamation-triangle-fill></b-icon-exclamation-triangle-fill>
        {{ backendValidationMessage }}
      </span>
      <b-button
        v-if="step === firstStep"
        class="col-12 col-lg-4"
        variant="primary"
        @click="nextStep"
      >
        Siguiente paso
      </b-button>
      <div v-if="step === secondStep">
        <b-button
          class="col-12 col-lg-4 mr-lg-3 mb-2 mb-lg-0"
          @click="backStep"
        >
          Regresar
        </b-button>
        <b-button class="col-12 col-lg-4" variant="primary" @click="nextStep">
          Siguiente
        </b-button>
      </div>
      <div v-if="step === thirdStep">
        <b-button
          class="col-12 col-lg-4 mr-lg-3 mb-2 mb-lg-0"
          @click="backStep"
        >
          Regresar
        </b-button>
        <b-button
          class="col-12 col-lg-4"
          variant="primary"
          @click="saveCredentials"
        >
          <b-spinner
            v-if="isLoading"
            small
            type="grow"
            class="mr-2"
          ></b-spinner>
          <span v-if="!isLoading">Probar y Guardar</span>
          <span v-if="isLoading">Validando información</span>
        </b-button>
      </div>
    </div>
  </div>
  <div v-else>
    <b-spinner class="auto" variant="primary" label="Spinning"></b-spinner>
  </div>
</template>

<style>
.w-60 {
  width: 60%;
}
.w-40 {
  width: 40%;
}
.image-section .text-center .img-preview,
.loadedPreviewImg {
  max-width: 100%;
  min-width: 80%;
  border: 0.2em lightgray dotted;
  height: auto;
}
.image-section .text-center input {
  width: 100%;
  margin-top: 1.3em;
}
</style>
