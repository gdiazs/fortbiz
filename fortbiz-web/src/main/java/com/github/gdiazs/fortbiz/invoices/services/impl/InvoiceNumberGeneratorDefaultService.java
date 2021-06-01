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
package com.github.gdiazs.fortbiz.invoices.services.impl;

import java.time.LocalDate;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.gdiazs.fortbiz.invoices.dto.InvoiceRequestKeyGeneratorDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceRequestNextNumber;
import com.github.gdiazs.fortbiz.invoices.repositories.SystemSequenceRepository;
import com.github.gdiazs.fortbiz.invoices.services.InvoiceNumberGeneratorService;

@Default
@Singleton
public class InvoiceNumberGeneratorDefaultService implements InvoiceNumberGeneratorService {

  private static final String ELECTRONIC_STATUS_NORMAL = "1";
  private SystemSequenceRepository systemSequenceRepository;

  @Inject
  public InvoiceNumberGeneratorDefaultService(SystemSequenceRepository systemSequenceRepository) {
    this.systemSequenceRepository = systemSequenceRepository;
  }

  @Override
  public String generateNextNumber(InvoiceRequestNextNumber invoiceRequestNextNumber) {
    final StringBuilder stringBuilder = new StringBuilder();
    return stringBuilder
        .append(String.format("%03d", invoiceRequestNextNumber.getBranchNumber()))
        .append(String.format("%05d", invoiceRequestNextNumber.getRegisterCashNumber()))
        .append(String.format("%02d", invoiceRequestNextNumber.getInvoiceType()))
        .append(String.format("%010d", invoiceRequestNextNumber.getVoucherNumber()))
        .toString();
  }

  @Override
  public String generateKeyHacienda(InvoiceRequestKeyGeneratorDto invoiceRequestKeyGeneratorDto) {

    return this.generateKeyHacienda(invoiceRequestKeyGeneratorDto, true, null);
  }

  private String generateKeyHacienda(
      InvoiceRequestKeyGeneratorDto invoiceRequestKeyGeneratorDto,
      boolean generateHaciendaKey,
      String securityCode) {

    if (null == invoiceRequestKeyGeneratorDto.getEmitterIdentification()
        || invoiceRequestKeyGeneratorDto.getEmitterIdentification().isEmpty()) {
      throw new IllegalArgumentException("customerIdentification is null or empty");
    }
    if (null == invoiceRequestKeyGeneratorDto.getCountryCode()
        || invoiceRequestKeyGeneratorDto.getCountryCode().isEmpty()) {
      throw new IllegalArgumentException("countryCode is null or empty");
    }
    if (null == invoiceRequestKeyGeneratorDto.getInvoiceNumber()
        || invoiceRequestKeyGeneratorDto.getInvoiceNumber().isEmpty()) {
      throw new IllegalArgumentException("invoiceNumber is null or empty");
    }

    final StringBuilder invoiceKey = new StringBuilder();

    invoiceKey
        .append(invoiceRequestKeyGeneratorDto.getCountryCode())
        .append(getDateToday())
        .append(formatCustomerIdentification(invoiceRequestKeyGeneratorDto.getEmitterIdentification()))
        .append(invoiceRequestKeyGeneratorDto.getInvoiceNumber())
        .append(ELECTRONIC_STATUS_NORMAL)
        .append(
            String.format(
                "%08d",
                generateHaciendaKey
                    ? this.systemSequenceRepository.generateNextSecurityCode()
                    : securityCode));

    return invoiceKey.toString();
  }

  public String getDateToday() {
    StringBuilder date = new StringBuilder();
    LocalDate fecha = LocalDate.now();

    date.append(String.format("%02d", fecha.getDayOfMonth()))
        .append(String.format("%02d", fecha.getMonthValue()))
        .append(String.valueOf(fecha.getYear()).substring(2));

    return date.toString();
  }

  @Override
  public String generateKeyHacienda(
      InvoiceRequestKeyGeneratorDto invoiceRequestKeyGeneratorDto, String securityCode) {

    return this.generateKeyHacienda(invoiceRequestKeyGeneratorDto, false, securityCode);
  }
  
  private String formatCustomerIdentification(String customerIdentification) {
  	return String.format("%012d", Long.parseLong(customerIdentification));
  }
}
