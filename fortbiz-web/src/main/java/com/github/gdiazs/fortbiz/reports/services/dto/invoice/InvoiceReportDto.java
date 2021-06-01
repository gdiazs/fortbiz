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
package com.github.gdiazs.fortbiz.reports.services.dto.invoice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class InvoiceReportDto {
  private String invoiceType;
  private String economicActivityAndLegalName;
  private String emitterId;
  private String invoiceNumber;
  private String invoiceDate;
  private String invoicePhone;
  private String invoiceEmail;
  private String companyImageBase64;
  private String receptorFullName;
  private String receptorId;
  private String subTotal;
  private String taxes;
  private String total;
  private List<InvoiceItemReportDto> items;
}
