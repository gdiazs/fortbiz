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
import InvoiceCustomer from "@/components/molecules/invoices/InvoiceCustomer";
import InvoiceDetail from "../../molecules/invoices/InvoiceDetail";

export default {
  name: "EditInvoice",
  components: {
    InvoiceCustomer,
    InvoiceDetail,
  },

  data() {
    return {
      invoiceArticle: [
        { key: "description", label: "Descripción" },
        { key: "unit", label: "Unidad de medida" },
        { key: "quantity", label: "Cantidad", thClass: "text-right" },
        { key: "price", label: "Precio", thClass: "text-right" },
        { key: "subtotal", label: "Total", thClass: "text-right" },
        { key: "options", label: "Opciones", thClass: "text-right" },
      ],
      invoiceNumber: null,
      invoice: null,
      productId: null,
      invoiceItemToEdit: null,
      productName: null,
    };
  },
  methods: {
    ...mapActions([
      "findInvoice",
      "findInvoiceDetail",
      "deleteInvoiceDetail",
      "findProductDetail",
      "deleteInvoiceCustomer",
      "sendInvoiceToHacienda",
    ]),
    formatPaymentMethod(paymentMethod) {
      return paymentMethod.toUpperCase().replace(/,(?=[^,]*$)/, " y");
    },
    showModalCustomer() {
      this.$bvModal.show("invoice-customer");
    },
    formatCustomerName() {
      if (this.getInvoice.invoicesCustomer) {
        return `${this.getInvoice.invoicesCustomer.customerName} ${this.getInvoice.invoicesCustomer.firstLastName} ${this.getInvoice.invoicesCustomer.secondLastName}`;
      }
      return "";
    },
    showModalInvoiceDetail() {
      this.invoiceItemToEdit = null;
      this.$bvModal.show("invoice-detail");
    },
    showConfirmDeleteItem(idProduct, name) {
      this.productId = idProduct;
      this.productName = name;
      this.$bvModal.show("confirm-delete");
    },
    deleteProduct() {
      let item = {
        productId: this.productId,
        invoiceNumber: this.invoiceNumber,
      };
      this.deleteInvoiceDetail(item).then();
    },
    editProduct(invoiceItem) {
      this.getListInvoiceDetail.items.find((item) => {
        if (item.productId === invoiceItem.productId) {
          this.invoiceItemToEdit = item;
        }
      });
    },
    showConfirmDeleteCustomer() {
      this.$bvModal.show("confirm-delete-customer");
    },
    deleteCustomer() {
      let item = {
        customerId: this.getInvoice.invoicesCustomer.identificationNumber,
        invoiceNumber: this.invoiceNumber,
      };
      this.deleteInvoiceCustomer(item);
    },
    mapUnits(data) {
      let unit = this.getInvoice.units;
      return unit[data];
    },
    sendInvoice() {
      this.sendInvoiceToHacienda(this.invoiceNumber);
    },
  },

  created() {
    this.invoiceNumber = this.$route.params.invoiceNumber;
    this.findInvoice(this.invoiceNumber);
    this.findInvoiceDetail(this.invoiceNumber);
  },

  computed: {
    ...mapGetters(["getInvoice", "getListInvoiceDetail"]),
  },
};
</script>

<template>
  <div class="container bg-white pb-3 mb-5" v-if="getInvoice">
    <div class="row bg-light rounded p-3 border">
      <div class="col col-12 col-lg-4 mt-2 size title-size text-left">
        <h5 class="font-weight-bold">
          FACTURA {{ formatPaymentMethod(getInvoice.invoiceType) }}
        </h5>
        <h4>
          {{ getInvoice.invoiceCredentialDto.tradeName }} -
          {{ getInvoice.invoiceCredentialDto.economicActivity }}
        </h4>
        <h4>
          Ced. {{ getInvoice.invoiceCredentialDto.ownerIdentificationNumber }}
        </h4>
      </div>
      <div class="col-12 offset-lg-5 col-lg-3 pt-2 text-left">
        <span class="font-weight-bold">Nº: </span>
        {{ getInvoice.invoiceNumber }}
        <div class="block font-weight-bold">
          ESTADO {{ getInvoice.status.toUpperCase() }}
        </div>
        <div class="block">
          <span class="font-weight-bold">FECHA: </span>
          {{ getInvoice.createdAt }}
        </div>
      </div>
    </div>
    <div class="row border-left border-bottom border-right p-1">
      <div class="col-12 col-md-5 mb-3 mt-3">
        <h4>
          Señor(a):
          <span class="ml-1">
            <span id="receptor" class="d-inline-block" tabindex="0">
              <b-icon-info-circle />
            </span>
            <b-tooltip target="receptor"
              >Dejar en blanco si desea una factura sin nombre</b-tooltip
            >
          </span>
        </h4>
        <form class="form-inline">
          <b-input
            class="w-75"
            placeholder="Escriba los datos receptor"
            @click="showModalCustomer"
            :value="formatCustomerName()"
          >
          </b-input>
          <b-button
            @click="showConfirmDeleteCustomer()"
            variant="outline-danger"
            class="ml-1"
          >
            <b-icon icon="x"></b-icon>
          </b-button>
        </form>
      </div>
      <b-modal
        id="confirm-delete-customer"
        ok-title="Borrar"
        cancel-title="Cancelar"
        @ok="deleteCustomer()"
      >
        ¿Desea eliminar el cliente
        <span class="font-weight-bold">{{ getInvoice.customerName }}</span
        >?
      </b-modal>

      <div class="col-12 offset-lg-9 col-lg-3 text-right">
        <b-button
          class="w-100"
          @click="showModalInvoiceDetail"
          variant="primary"
          >Agregar artículo</b-button
        >
      </div>
      <b-modal
        id="invoice-customer"
        title="Cliente"
        :hide-footer="true"
        size="md"
        :no-close-on-backdrop="true"
      >
        <invoice-customer
          :invoice-number="getInvoice.invoiceNumber"
          :editable-customer="getInvoice.invoicesCustomer"
        ></invoice-customer>
      </b-modal>
      <b-modal
        id="invoice-detail"
        title="Detalle de factura"
        :hide-footer="true"
        size="md"
        :no-close-on-backdrop="true"
      >
        <div class="px-4">
          <invoice-detail
            :invoice-id="getInvoice.invoiceNumber"
            :invoice-number="getInvoice.invoiceNumber"
            :editable-product="invoiceItemToEdit"
          ></invoice-detail>
        </div>
      </b-modal>
      <b-modal
        id="confirm-delete"
        ok-title="Borrar"
        cancel-title="Cancelar"
        @ok="deleteProduct"
      >
        ¿Desea eliminar el producto {{ productName }}?
      </b-modal>
      <div class="col-12 mt-4">
        <b-table
          hover
          show-empty
          empty-text="No hay productos ingresados"
          class="w-100"
          :fields="invoiceArticle"
          responsive
          :items="getListInvoiceDetail.items"
        >
          <template v-slot:cell(description)="data">
            <div class="description-size">
              {{ data.item.description }}
            </div>
          </template>

          <template v-slot:cell(unit)="data">
            <div>
              {{ mapUnits(data.item.unit) }}
            </div>
          </template>

          <template v-slot:cell(quantity)="data">
            <div class="text-right">{{ data.item.quantity }}</div>
          </template>

          <template v-slot:cell(price)="data">
            <div class="text-right">
              {{ data.item.price }}
            </div>
          </template>

          <template v-slot:cell(subtotal)="data">
            <div class="text-right">
              {{ data.item.subtotal }}
            </div>
          </template>

          <template v-slot:cell(options)="data" class="col-2">
            <div class="d-flex justify-content-end">
              <b-button variant="primary">
                <b-icon
                  icon="pencil-square"
                  @click="editProduct(data.item)"
                  v-b-modal.invoice-detail
                ></b-icon>
              </b-button>
              <b-button
                @click="
                  showConfirmDeleteItem(
                    data.item.productId,
                    data.item.description
                  )
                "
                variant="outline-danger"
              >
                <b-icon icon="x"></b-icon>
              </b-button>
            </div>
          </template>
        </b-table>
      </div>
    </div>
    <div class="row mt-3">
      <div class="col-md-3 offset-md-7">
        <b-table-simple borderlessFSF>
          <tr>
            <td class="text-right font-weight-bold">Sub Total:</td>
            <td class="text-right pr-2">{{ getListInvoiceDetail.subtotal }}</td>
          </tr>
          <tr>
            <td class="text-right font-weight-bold">Impuestos:</td>
            <td class="text-right pr-2">
              {{ getListInvoiceDetail.taxesTotal }}
            </td>
          </tr>
          <tr>
            <td class="text-right font-weight-bold">Total:</td>
            <td class="text-right pr-2">
              {{ getInvoice.currency }} {{ getListInvoiceDetail.total }}
            </td>
          </tr>
        </b-table-simple>
      </div>
    </div>

    <div class="row mt-3 text-right">
      <div class="col-12 offset-lg-8 col-lg-2">
        <b-button
          class="btn-block"
          variant="outline-secondary"
          href="#/invoices"
          >Regresar</b-button
        >
      </div>
      <div class="col-12 col-lg-2 mt-3 mt-lg-0">
        <b-button class="btn-block" variant="primary" @click="sendInvoice"
          >Facturar</b-button
        >
      </div>
    </div>
  </div>
</template>
<style>
.description-size {
  width: 12em;
}
@media (min-width: 768px) {
  .description-size {
    width: auto;
  }
}
</style>
