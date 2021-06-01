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
package com.github.gdiazs.fortbiz.invoices.converters;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.gdiazs.fortbiz.branches.repositories.BranchRepository;
import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.credentials.repository.CredentialRepository;
import com.github.gdiazs.fortbiz.customers.repositories.CustomerRepository;
import com.github.gdiazs.fortbiz.entities.BranchEntity;
import com.github.gdiazs.fortbiz.entities.CredentialEntity;
import com.github.gdiazs.fortbiz.entities.CustomerEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceCredentialDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDto;
import com.github.gdiazs.fortbiz.utils.StringFormatter;

@Singleton
public class InvoiceConverter implements ModelConverter<InvoiceDto, InvoiceEntity> {

  private final StringFormatter stringFormatter;
  private final CredentialRepository credentialRepository;
  private final CustomerRepository customerRepository;
  private final BranchRepository branchRepository;

  @Inject
  public InvoiceConverter(
      StringFormatter stringFormatter,
      CredentialRepository credentialRepository,
      CustomerRepository customerRepository,
      BranchRepository branchRepository) {
    this.stringFormatter = stringFormatter;
    this.credentialRepository = credentialRepository;
    this.customerRepository = customerRepository;
    this.branchRepository = branchRepository;
  }

  @Override
  public InvoiceDto convertFrom(InvoiceEntity source) {
    final CredentialEntity credentialEntity =
        credentialRepository.findCredentialByBranchId(source.getBranchId());

    final BranchEntity branchEntity = this.branchRepository.findBy(source.getBranchId());

    final CustomerEntity customer =
        source.getCustomerId() == null
            ? null
            : this.customerRepository.findBy(source.getCustomerId());
    String customerName = null;
    String customerIdentification = null;
    if (customer != null) {
      customerName =
          customer.getCustomerName()
              + " "
              + customer.getFirstLastName()
              + " "
              + customer.getSecondLastName();
      customerIdentification = customer.getIdentificationNumber();
    }

    return InvoiceDto.builder()
        .invoiceType(source.getInvoiceType().toString())
        .invoiceNumber(source.getInvoiceNumber())
        .createdAt(stringFormatter.formatToCrDates(source.getCreatedAt()))
        .invoiceCredentialDto(convertCredentialToDto(credentialEntity))
        .status(source.getStatus())
        .paymentMethod(Arrays.asList(source.getPaymentMethod().split(",")))
        .total("0")
        .customerName(customerName)
        .customerIdentification(customerIdentification)
        .currency(source.getCurrency())
        .branchPhone(branchEntity.getPhone().toString())
        .branchEmail(branchEntity.getEmail())
        //.customerEmail(customer.getAddress()) // TODO se debe cambiar por email
        .build();
  }

  @Override
  public InvoiceEntity convertTo(InvoiceDto target) {
    final InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity.setCurrency(target.getCurrency());
    invoiceEntity.setInvoiceType(Integer.parseInt(target.getInvoiceType()));
    invoiceEntity.setPaymentMethod(convertPaymentMethodToString(target.getPaymentMethod()));
    invoiceEntity.setStatus("1");
    invoiceEntity.setBranchId(target.getBranchId());
    return invoiceEntity;
  }

  private InvoiceCredentialDto convertCredentialToDto(CredentialEntity credentialEntity) {
    return InvoiceCredentialDto.builder()
        .economicActivity(credentialEntity.getEconomicActivity())
        .ownerIdentificationNumber(credentialEntity.getIndentificationNumber())
        .tradeName(credentialEntity.getTradeName())
        .build();
  }

  private String convertPaymentMethodToString(List<String> listPaymentMethod) {
    return listPaymentMethod.stream().collect(Collectors.joining(","));
  }
}
