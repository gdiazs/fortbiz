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
import CredentialsForm from "@/components/organisms/credentials/CredentialsForm";
import OptionsBar from "@/components/molecules/OptionsBar";
import { mapActions, mapGetters } from "vuex";

export default {
  name: "ConfigurationCredentials",
  components: {
    OptionsBar,
    CredentialsForm,
  },
  methods: {
    ...mapActions([
      "fetchCredentialsByUserId",
      "deleteCredentialById",
      "validateCredentials",
    ]),
    defineOptionBarTitle() {
      return "Todos los credenciales";
    },
    showCredentialForm() {
      this.credentialToEdit = null;
      this.$bvModal.show("credential-form");
    },
    showDeleteConfirm(credentialId, name) {
      this.credentialToDelete = credentialId;
      this.credentialIdentificationToDelete = name;
      this.$bvModal.show("confirm-delete");
    },
    deleteCredential() {
      this.deleteCredentialById(this.credentialToDelete)
        .then(() => {
          this.showAlert = true;
          this.alertMessage =
            "Los credenciales han sido elimninados satisfactoriamente";
          this.alertVariant = "success";
        })
        .catch(({ response }) => {
          if (
            response.status === 409 &&
            response.data.errorMessage.includes("Credential has invoices")
          ) {
            this.showAlert = true;
            this.alertMessage =
              "Los credenciales no pueden ser eliminados, estos credenciales contienen facturas registradas.";
            this.alertVariant = "danger";
          } else {
            this.showAlert = true;
            this.alertMessage =
              "Ha ocurrido un error interno, por favor vuelta a interntarlo.";
            this.alertVariant = "danger";
          }
        });
    },
    editCredential(credentialToEdit) {
      const credentialFound = this.getCredentials.find(
        (credential) => credential.id === credentialToEdit.id
      );
      if (credentialFound) {
        this.credentialToEdit = credentialFound;
      } else {
        this.credentialToEdit = {};
      }
    },
    onValidateCredentials(credentialToEdit) {
      const credentialFound = this.getCredentials.find(
        (credential) => credential.id === credentialToEdit.id
      );
      this.validateCredentials(credentialFound.id).then((res) => {
        const isValid = res.data.valid;
        if (isValid) {
          this.showAlert = true;
          this.alertMessage =
            "Los credenciales han sido válidados satisfactoriamente";
          this.alertVariant = "success";
        } else {
          this.showAlert = true;
          this.alertMessage =
            "Los credenciales no son válidos, verifique que los credenciales corresponden a los brindados por el sistema de <a target='blank' href='https://www.hacienda.go.cr/ATV/Login.aspx'>ATV</a> del ministerio de Hacienda";
          this.alertVariant = "danger";
        }
      });
    },
  },
  data() {
    return {
      credentialsTableHeader: [
        { key: "identificationNumber", label: "Identificación", thClass: "" },
        { key: "username", label: "Usuario de ingreso" },
        { key: "createdAt", label: "Creado", thClass: "" },
        { key: "updatedAt", label: "Modificado", thClass: "" },
        { key: "options", label: "Opciones", thClass: "text-right" },
      ],
      credentialToDelete: null,
      credentialToEdit: null,
      credentialIdentificationToDelete: null,
      showAlert: false,
      alertMessage: "",
      alertVariant: "",
    };
  },
  computed: {
    ...mapGetters(["getCredentials"]),
    showItemsCredentialsTable() {
      let credentialData = [];
      this.getCredentials.forEach((credentials) => {
        credentialData.push({
          identificationNumber: credentials.identificationNumber,
          username: credentials.username,
          id: credentials.id,
          createdAt: credentials.createdAt,
          updatedAt: credentials.updatedAt,
        });
      });
      return credentialData;
    },
  },
  created() {
    this.fetchCredentialsByUserId();
  },
};
</script>

<template>
  <div>
    <b-alert
      :variant="alertVariant"
      class="border"
      :show="showAlert"
      dismissible
      @dismissed="showAlert = false"
      ><span v-html="alertMessage"
    /></b-alert>
    <options-bar :title="defineOptionBarTitle()">
      <b-button @click="showCredentialForm" variant="primary">
        <b-icon class="mr-2" icon="plus-square"></b-icon> Agregar credenciales
      </b-button>
    </options-bar>

    <b-modal
      id="credential-form"
      title="Credenciales"
      :hide-footer="true"
      size="md"
      :no-close-on-backdrop="true"
    >
      <credentials-form
        :editable-credential="credentialToEdit"
      ></credentials-form>
    </b-modal>
    <b-modal
      id="confirm-delete"
      ok-title="Borrar"
      cancel-title="Cancelar"
      @ok="deleteCredential"
    >
      ¿Desea eliminar la credencial con la cédula
      {{ credentialIdentificationToDelete }}?
    </b-modal>
    <div class="row mt-3">
      <div class=" table-responsive rounded shadow-sm py-2 px-2 bg-white">
        <b-table
          hover
          :fields="credentialsTableHeader"
          :items="showItemsCredentialsTable"
          show-empty
          empty-text="No hay registros para mostrar"
          class="w-100"
        >
          <template v-slot:cell(identificationNumber)="data">
            {{ data.item.identificationNumber }}
          </template>
          <template v-slot:cell(username)="data">
            {{ data.item.username }}
          </template>
          <template v-slot:cell(created_at)="data">
            {{ data.item.createdAt }}
          </template>
          <template v-slot:cell(updated_at)="data">
            {{ data.item.updatedAt }}
          </template>
          <template v-slot:cell(options)="data" class="col-2">
            <div class="d-flex justify-content-end">
              <b-button
                variant="primary"
                @click="editCredential(data.item)"
                v-b-modal.credential-form
                >Editar <b-icon icon="pencil-square"></b-icon>
              </b-button>
              <b-button
                @click="onValidateCredentials(data.item)"
                title="Comprueba que los credenciales puedar acceder a la plataforma del Ministerio de Hacienda"
                >Probar <b-icon icon="check-square"></b-icon>
              </b-button>
              <b-button
                variant="outline-danger"
                @click="
                  showDeleteConfirm(
                    data.item.id,
                    data.item.identificationNumber
                  )
                "
              >
                <b-icon icon="x"></b-icon>
              </b-button>
            </div>
          </template>
        </b-table>
      </div>
    </div>
  </div>
</template>
