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
package com.github.gdiazs.fortbiz.customers.converters;

import javax.inject.Singleton;

import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.entities.CustomerEntity;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceCustomerDto;

@Singleton
public class CustomerConverter implements ModelConverter<InvoiceCustomerDto, CustomerEntity> {

  public CustomerConverter() {}

  @Override
  public CustomerEntity convertTo(InvoiceCustomerDto source) {
    final CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setCustomerName(source.getCustomerName());
    customerEntity.setAddress(source.getAddress());
    customerEntity.setFirstLastName(source.getFirstLastName());
    customerEntity.setSecondLastName(source.getSecondLastName());
    customerEntity.setIdentificationNumber(source.getIdentificationNumber());
    customerEntity.setIdentificationType(source.getIdentificationType());
    customerEntity.setPhoneNumber(source.getPhoneNumber());
    return customerEntity;
  }

  @Override
  public InvoiceCustomerDto convertFrom(CustomerEntity target) {
    return InvoiceCustomerDto.builder()
        .customerId(target.getId())
        .address(target.getAddress())
        .identificationNumber(target.getIdentificationNumber())
        .identificationType(target.getIdentificationType())
        .customerName(target.getCustomerName())
        .firstLastName(target.getFirstLastName())
        .phoneNumber(target.getPhoneNumber())
        .secondLastName(target.getSecondLastName())
        .build();
  }
}
