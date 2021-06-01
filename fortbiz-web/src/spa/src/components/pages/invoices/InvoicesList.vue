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
import OptionsBar from "@/components/molecules/OptionsBar";
import InvoicesHeader from "@/components/molecules/invoices/InvoicesHeader";
import { mapGetters, mapActions } from "vuex";
import BranchSwitcher from "../../molecules/BranchSwitcher.vue";

export default {
  name: "InvoicesHome",
  components: {
    InvoicesHeader,
    OptionsBar,
    BranchSwitcher,
  },

  methods: {
    ...mapActions(["fetchInvoices", "deleteInvoiceByNumber"]),

    showInvoiceHeader() {
      this.$bvModal.show("invoice-header");
    },

    optionBarTitle() {
      return "Todas las facturas";
    },
    showDeleteConfirm(invoiceNumber) {
      this.invoiceNumberToDelete = invoiceNumber;
      this.$bvModal.show("confirm-delete");
    },
    deleteInvoice() {
      this.deleteInvoiceByNumber(this.invoiceNumberToDelete)
        .then(() => {
          this.showAlertMessage("success", true, "Factura eliminada con exito");
        })
        .catch(() => {
          this.showAlertMessage(
            "danger",
            true,
            "Esta factura posee productos registrados, no puede ser eliminado"
          );
        });
    },
    showAlertMessage(variantValue, show, alertMessage) {
      this.variantValue = variantValue;
      this.show = show;
      this.alertMessage = alertMessage;
    },
  },

  data() {
    return {
      invoicesTableHeader: [
        { key: "invoiceNumber", label: "N°", thClass: "" },
        { key: "status", label: "Estado", thClass: "", sortable: true },
        { key: "customerName", label: "Receptor", thClass: "" },
        { key: "customerIdentification", label: "Cédula", thClass: "" },
        { key: "paymentMethod", label: "Tipo", thClass: "" },
        { key: "total", label: "Total", thClass: "" },
        { key: "createdAt", label: "Emisión", thClass: "", sortable: true },
        { key: "options", label: "Opciones", thClass: "text-right" },
      ],
      invoiceNumberToDelete: null,
      tableFilter: "",
      show: null,
      variantValue: "success",
      alertMessage: "",
    };
  },
  computed: {
    ...mapGetters(["getInvoiceHeader", "getActiveBranch"]),
    showItemsInvoiceTable() {
      let invoiceData = [];
      this.getInvoiceHeader.forEach((invoice) => {
        invoiceData.push({
          status: invoice.status,
          invoiceNumber: invoice.invoiceNumber,
          customerName: invoice.customerName,
          customerIdentification: invoice.customerIdentification,
          createdAt: invoice.createdAt,
          paymentMethod: invoice.paymentMethod,
          total: invoice.total,
        });
      });
      return invoiceData;
    },
  },
  created() {
    this.fetchInvoices(this.getActiveBranch);
  },
};
</script>

<template>
  <div>
    <div class="row mb-3">
      <div class="col-12 col-md-6 offset-md-3">
        <b-alert class="col" :variant="variantValue" :show="show"
          ><span v-html="alertMessage"
        /></b-alert>

        <branch-switcher />
      </div>
    </div>


    <options-bar :title="optionBarTitle()">
      <b-button @click="showInvoiceHeader" variant="primary">
        <b-icon class="mr-2" icon="plus-square"></b-icon>
        Crear nueva factura
      </b-button>
    </options-bar>

    <b-modal
      id="invoice-header"
      title="Nueva factura"
      :hide-footer="true"
      size="md"
      :no-close-on-backdrop="true"
    >
      <invoices-header></invoices-header>
    </b-modal>

    <b-modal
      id="confirm-delete"
      ok-title="Borrar"
      title="Borrar Factura"
      cancel-title="Cancelar"
      @ok="deleteInvoice"
    >
      ¿Desea eliminar la factura {{ this.invoiceNumberToDelete }}?
    </b-modal>

    <div class="row mt-3">
      <div class="bg-white w-100 p-2">
        <b-input
          placeholder="Filtrar"
          class="w-25 float-right"
          v-model="tableFilter"
        />
      </div>
      <div class="bg-white table-responsive col-12 rounded">
        <b-table
          :filter="tableFilter"
          hover
          :fields="invoicesTableHeader"
          :items="showItemsInvoiceTable"
          show-empty
          empty-text="No hay registros para mostrar"
          empty-filtered-text="No se encontraron registros"
        >
          <template v-slot:cell(options)="data" class="col-2">
            <div
              v-if="data.item.status === 'Borrador'"
              class="d-flex justify-content-end"
            >
              <b-link
                class="btn btn-primary"
                :href="'#/invoices/' + data.item.invoiceNumber"
                ><b-icon icon="pencil-square"></b-icon> Editar</b-link
              >
              <b-button
                @click="showDeleteConfirm(data.item.invoiceNumber)"
                variant="outline-danger"
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

<style scoped></style>
