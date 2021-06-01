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

import com.github.gdiazs.fortbiz.invoices.dto.InvoiceRequestKeyGeneratorDto;
import com.github.gdiazs.fortbiz.invoices.repositories.InvoiceRepository;
import com.github.gdiazs.fortbiz.invoices.repositories.SystemSequenceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceNumberGeneratorDefaultServiceTest {

  @Mock private InvoiceRepository invoiceRepository;

  @Mock private SystemSequenceRepository systemSequenceRepository;

  @InjectMocks private InvoiceNumberGeneratorDefaultService invoiceNumberGeneratorDefaultService;

  @Test
  void testGenerateKeyHacienda_invalidRequestKeyGenerator() {

    InvoiceNumberGeneratorDefaultService invoiceNumberGeneratorDefaultService =
        new InvoiceNumberGeneratorDefaultService(null);
    InvoiceRequestKeyGeneratorDto invoiceRequestKeyGeneratorDto =
        InvoiceRequestKeyGeneratorDto.builder().build();

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          invoiceNumberGeneratorDefaultService.generateKeyHacienda(invoiceRequestKeyGeneratorDto);
        });
  }

  @Test
  void testGenerateKeyHacienda() {

    InvoiceRequestKeyGeneratorDto invoiceRequestKeyGeneratorDto =
        InvoiceRequestKeyGeneratorDto.builder()
            .countryCode("506")
            .emitterIdentification("3101123456")
            .invoiceNumber("00100001010000000001")
            .build();

    Mockito.when(this.systemSequenceRepository.generateNextSecurityCode()).thenReturn(99999999L);
    
    var actualKey =
        this.invoiceNumberGeneratorDefaultService.generateKeyHacienda(
            invoiceRequestKeyGeneratorDto);

    StringBuilder verifyKey = new StringBuilder();
    verifyKey.append("506").append(getDateToday()).append("00310112345600100001010000000001199999999");

    Assertions.assertEquals(actualKey, verifyKey.toString()); 
  }

  private String getDateToday() {
    StringBuilder date = new StringBuilder();
    LocalDate fecha = LocalDate.now();

    date.append(String.format("%02d", fecha.getDayOfMonth()))
        .append(String.format("%02d", fecha.getMonthValue()))
        .append(String.valueOf(fecha.getYear()).substring(2));

    return date.toString();
  }
}
