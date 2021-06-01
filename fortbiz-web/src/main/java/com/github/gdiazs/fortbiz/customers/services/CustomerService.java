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
package com.github.gdiazs.fortbiz.customers.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.customers.repositories.CustomerRepository;
import com.github.gdiazs.fortbiz.entities.CustomerEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceCustomerDto;
import com.github.gdiazs.fortbiz.invoices.repositories.InvoiceRepository;

@Singleton
public class CustomerService {

  private static Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

  private final ModelConverter<InvoiceCustomerDto, CustomerEntity> customerConverter;
  private final CustomerRepository customerRepository;
  private final InvoiceRepository invoiceRepository;

  @Inject
  public CustomerService(
      ModelConverter<InvoiceCustomerDto, CustomerEntity> customerModelConverter,
      CustomerRepository customerRepository,
      InvoiceRepository invoiceRepository) {
    this.customerRepository = customerRepository;
    this.customerConverter = customerModelConverter;
    this.invoiceRepository = invoiceRepository;
  }

  @Transactional
  public InvoiceCustomerDto addCustomerInvoice(
      InvoiceCustomerDto invoiceCustomerDto, String userId) {

    final CustomerEntity customerEntity = customerConverter.convertTo(invoiceCustomerDto);
    customerEntity.setId(invoiceCustomerDto.getCustomerId());
    final CustomerEntity saveCustomer = this.customerRepository.saveAndFlush(customerEntity);

    InvoiceEntity invoiceEntity =
        this.invoiceRepository.findInvoiceByNumber(userId, invoiceCustomerDto.getInvoiceNumber());
    invoiceEntity.setCustomerId(customerEntity.getId());
    this.invoiceRepository.saveAndFlush(invoiceEntity);

    return this.customerConverter.convertFrom(saveCustomer);
  }

  @Transactional
  public InvoiceCustomerDto findCustomerByIdentificationNumber(String identificationNumber) {
    CustomerEntity customerEntity = null;

    try {
      customerEntity = this.customerRepository.findByIdentificationNumber(identificationNumber);
    } catch (NoResultException noResultException) {
      LOGGER.log(Level.INFO, "No result for " + identificationNumber, noResultException);
    }
    return null == customerEntity ? null : this.customerConverter.convertFrom(customerEntity);
  }

  @Transactional
  public void deleteCustomerInvoice(String customerId, String invoiceNumber, String userId) {
    InvoiceEntity invoiceEntity = this.invoiceRepository.findInvoiceByNumber(userId, invoiceNumber);
    invoiceEntity.setCustomerId(null);
    this.invoiceRepository.saveAndFlush(invoiceEntity);
  }
}
