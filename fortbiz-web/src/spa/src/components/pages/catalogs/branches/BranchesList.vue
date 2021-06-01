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
import { mapGetters, mapActions } from "vuex";
import OptionsBar from "@/components/molecules/OptionsBar";
import BranchesAdd from "@/components/organisms/branches/BranchesAdd";

export default {
  name: "List",
  components: {
    OptionsBar,
    BranchesAdd,
  },
  data() {
    return {
      branchesTableHeader: [
        { key: "name", label: "Sucursal", thClass: "" },
        { key: "created_at", label: "Creado en", thClass: "" },
        { key: "updated_at", label: "Modificado", thClass: "" },
        { key: "options", label: "Opciones", thClass: "text-right" },
      ],
      branchToEdit: null,
      branchToDelete: null,
      branchNameToDelete: null,
      show: null,
      variantValue: "success",
      alertMessage: "",
    };
  },
  methods: {
    ...mapActions(["fetchBranchesByUserId", "deleteBranchById"]),
    editBranch(branchToEdit) {
      const branchFound = this.getUserBranches.find(
        (branch) => branch.id === branchToEdit.id
      );
      if (branchFound) {
        this.branchToEdit = branchFound;
        this.branchToEdit.credentialId = this.branchToEdit.credentialId || "";
      } else {
        this.branchToEdit = {};
      }
    },
    deleteBranch() {
      this.deleteBranchById(this.branchToDelete)
        .then(() => {
          this.showAlertMessage("success", true, "Branch eliminado con exito");
        })
        .catch(({response}) => {
          if (response.status === 409 && response.data.errorMessage.includes("Branch has invoices")) {
            this.showAlertMessage(
              "danger",
              true,
              "Esta sucursal posee facturas registradas, no puede ser eliminada"
            );
          }else{
            this.showAlertMessage(
              "danger",
              true,
              "Estamos teniendo problemas internos, por favor vuelva a intentar más tarde"
            );
          }
        });
    },
    showAlertMessage(variantValue, show, alertMessage) {
      this.variantValue = variantValue;
      this.show = show;
      this.alertMessage = alertMessage;
    },
    showBranchForm() {
      this.branchToEdit = null;
      this.$bvModal.show("modal-sucursal");
    },
    showDeleteConfirm(branchId, name) {
      this.branchToDelete = branchId;
      this.branchNameToDelete = name;
      this.$bvModal.show("confirm-delete");
    },
    defineModalTitle() {
      return this.branchToEdit ? "Editar Sucursal" : "Nueva Sucursal";
    },
    optionBarTitle() {
      return `Todas las sucursales (${this.getUserBranches.length})`;
    },
  },
  computed: {
    ...mapGetters(["getUserBranches", "getUserId"]),
    branchesTableItems() {
      let branchesData = [];
      this.getUserBranches.forEach((branch) => {
        branchesData.push({
          name: branch.name,
          id: branch.id,
          createdAt: branch.createdAt,
          updatedAt: branch.updatedAt,
        });
      });
      return branchesData;
    },
  },
  created() {
    this.fetchBranchesByUserId(this.getUserId).then();
  },
};
</script>
<template>
  <div>
    <div class="row">
      <div class="col-12">
        <b-alert class="col" :variant="variantValue" :show="show"
          ><span v-html="alertMessage"
        /></b-alert>
        <options-bar :title="optionBarTitle()">
          <b-button variant="primary" @click="showBranchForm">
            <b-icon class="mr-2" icon="plus-square"></b-icon>
            Agregar Sucursal
          </b-button>
        </options-bar>
      </div>
    </div>
    <b-modal
      id="modal-sucursal"
      size="lg"
      class=""
      :title="defineModalTitle()"
      :hide-footer="true"
      :no-close-on-backdrop="true"
    >
      <branches-add :editable-branch="branchToEdit"></branches-add>
    </b-modal>
    <b-modal
      id="confirm-delete"
      ok-title="Borrar"
      cancel-title="Cancelar"
      @ok="deleteBranch"
    >
      ¿Desea eliminar la sucursal {{ branchNameToDelete }}?
    </b-modal>
    <div class="row mt-3">
      <div class="table-responsive shadow-sm py-2 px-2 bg-white rounded">
        <b-table
          hover
          :fields="branchesTableHeader"
          :items="branchesTableItems"
          show-empty
          empty-text="No hay registros para mostrar"
        >
          <template v-slot:cell(name)="data">
            {{ data.item.name }}
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
                @click="editBranch(data.item)"
                v-b-modal.modal-sucursal
                ><b-icon icon="pencil-square"></b-icon
              ></b-button>
              <b-button
                variant="outline-danger"
                @click="showDeleteConfirm(data.item.id, data.item.name)"
                ><b-icon icon="x"></b-icon
              ></b-button>
            </div>
          </template>
        </b-table>
      </div>
    </div>
  </div>
</template>

<style scoped>
@media (max-width: 767.98px) {
  .size {
    font-size: 1.2em;
  }
}
</style>
