/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import axios from "axios";

const invoicesPaymentMethodLabel = {
  "01": "Efectivo",
  "02": "Tarjeta",
  "04": "Transferencia",
};
const invoicesStatusLabel = {
  "1": "Borrador",
  "2": "Aceptado",
  "3": "Rechazado",
};
const invoiceTypes = {
  1: "Contado",
};

let prepareInvoice = (invoice) => {
  invoice.paymentMethod = invoice.paymentMethod
    .map((method) => {
      return " " + invoicesPaymentMethodLabel[method];
    })
    .toString();
  invoice.invoiceType = invoiceTypes[invoice.invoiceType];
  invoice.status = invoicesStatusLabel[invoice.status];
  return invoice;
};

export default {
  state: {
    invoicesHeader: [],
    invoice: null,
    customer: null,
    invoiceDetails: [],
  },
  mutations: {
    setInvoices(state, data) {
      state.invoicesHeader = data.map((invoice) => {
        return prepareInvoice(invoice);
      });
    },
    addInvoiceHeader(state, data) {
      state.invoicesHeader.push(data);
    },
    setInvoice(state, data) {
      state.invoice = prepareInvoice(data);
    },
    removeInvoiceByNumber(state, invoiceNumber) {
      state.invoicesHeader.find((aInvoice, index) => {
        if (aInvoice.invoiceNumber === invoiceNumber) {
          state.invoicesHeader.splice(index, 1);
          return;
        }
      });
    },
    setCustomer(state, customer) {
      state.customer = customer;
    },
    addInvoiceDetail(state, data) {
      state.invoiceDetails = data;
    },
    deleteCustomerFromInvoice(state, data) {
      console.log(state.invoice);
      state.invoice.customerName = null;
      state.invoice.invoicesCustomer = null;
    },
  },
  getters: {
    getInvoiceHeader(state) {
      return state.invoicesHeader;
    },
    getInvoice(state) {
      return state.invoice;
    },
    getCustomer(state) {
      return state.customer;
    },
    getListInvoiceDetail(state) {
      return state.invoiceDetails;
    },
  },
  actions: {
    addInvoiceHeader(store, header) {
      return axios.post("/api/invoices", header).then((response) => {
        store.commit("addInvoiceHeader", response.data);
        return response;
      });
    },
    fetchInvoices(store, branchId) {
      return axios
        .get(`/api/branches/v1/${branchId}/invoices`)
        .then((response) => {
          store.commit("setInvoices", response.data);
        });
    },
    findInvoice(store, invoiceNumber) {
      return axios.get(`/api/invoices/${invoiceNumber}`).then((response) => {
        store.commit("setInvoice", response.data);
      });
    },
    deleteInvoiceByNumber(store, invoiceNumber) {
      return axios.delete(`/api/invoices/${invoiceNumber}`).then(() => {
        store.commit("removeInvoiceByNumber", invoiceNumber);
      });
    },
    saveInvoiceCustomer(store, customer) {
      return axios.post("/api/customers", customer).then();
    },
    updateInvoiceCustomer(store, customer) {
      return axios.put("/api/customers", customer).then();
    },
    findCustomer(store, identificationNumber) {
      return axios
        .get(`/api/customers/${identificationNumber}`)
        .then((response) => {
          store.commit("setCustomer", response.data);
        });
    },
    addInvoiceDetail(store, invoiceDetail) {
      return axios.post("/api/products/", invoiceDetail).then(() => {
        store.commit("addInvoiceDetail", invoiceDetail);
      });
    },
    updateInvoiceDetail(store, invoiceDetail) {
      return axios.put("api/products", invoiceDetail).then();
    },
    findInvoiceDetail(store, invoiceNumber) {
      return axios
        .get(`/api/invoices/${invoiceNumber}/items`)
        .then((response) => {
          store.commit("addInvoiceDetail", response.data);
        });
    },
    deleteInvoiceDetail(store, item) {
      return axios
        .delete(`/api/invoices/${item.invoiceNumber}/product/${item.productId}`)
        .then((response) => {
          store.commit("addInvoiceDetail", response.data);
        });
    },
    deleteInvoiceCustomer(store, item) {
      return axios
        .delete(
          `api/customers/${item.customerId}/invoice/${item.invoiceNumber}`
        )
        .then((response) => {
          if (response.status === 200) {
            store.commit("deleteCustomerFromInvoice", item.invoiceNumber);
          }
        });
    },
    sendInvoiceToHacienda(store, invoiceNumber) {
      return axios.post(`api/invoices/${invoiceNumber}`).then();
    },
  },
};
