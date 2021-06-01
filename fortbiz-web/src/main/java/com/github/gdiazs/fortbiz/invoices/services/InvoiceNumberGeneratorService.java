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
package com.github.gdiazs.fortbiz.invoices.services;

import com.github.gdiazs.fortbiz.invoices.dto.InvoiceRequestKeyGeneratorDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceRequestNextNumber;

public interface InvoiceNumberGeneratorService {
  String generateNextNumber(InvoiceRequestNextNumber invoiceRequestNextNumber);

  String generateKeyHacienda(InvoiceRequestKeyGeneratorDto invoiceRequestKeyGeneratorDto);

  /**
   * @param invoiceRequestKeyGeneratorDto contiene las variables para generar la clave
   * @param securityCode si este parametro se envía, significa que no debe generarse una clave de
   *     seguridad
   * @return la clave de hacienda.
   */
  String generateKeyHacienda(
      InvoiceRequestKeyGeneratorDto invoiceRequestKeyGeneratorDto, String securityCode);
}
