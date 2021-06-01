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
package com.github.gdiazs.fortbiz.invoices.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

  private String invoiceNumber;
  private String invoiceType;
  private String branchId;
  private List<String> paymentMethod;
  private String currency;
  private String createdAt;
  private String status;
  private String total;
  private String customerName;
  private String customerIdentification;
  private String customerEmail;
  private String branchPhone;
  private String branchEmail;
  private InvoiceCredentialDto invoiceCredentialDto;
  private InvoiceCustomerDto invoicesCustomer;
  private Map<String, String> units;
}
