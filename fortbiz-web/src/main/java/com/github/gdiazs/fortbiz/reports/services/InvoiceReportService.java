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
package com.github.gdiazs.fortbiz.reports.services;

import com.github.gdiazs.fortbiz.commons.inject.Service;
import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.credentials.repository.CredentialRepository;
import com.github.gdiazs.fortbiz.entities.CredentialEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDetailsDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDto;
import com.github.gdiazs.fortbiz.invoices.repositories.InvoiceRepository;
import com.github.gdiazs.fortbiz.reports.exceptions.ReportsException;
import com.github.gdiazs.fortbiz.reports.services.dto.invoice.InvoiceItemReportDto;
import com.github.gdiazs.fortbiz.utils.CurrencyFormatter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.inject.Inject;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceReportService {

  private static String INVOICE_REPORT_TEMPLATE_DEFAULT =
      "invoices/templates/invoice-template.jrxml";

  private final InvoiceRepository invoiceRepository;
  private final CredentialRepository credentialRepository;
  private final CurrencyFormatter currencyFormatter;
  private final ModelConverter<InvoiceDto, InvoiceEntity> invoiceConverter;
  private final ModelConverter<InvoiceEntity, InvoiceDetailsDto> invoiceDetailsConverter;

  @Inject
  public InvoiceReportService(
      InvoiceRepository invoiceRepository,
      CredentialRepository credentialRepository,
      CurrencyFormatter currencyFormatter,
      ModelConverter<InvoiceDto, InvoiceEntity> invoiceConverter,
      ModelConverter<InvoiceEntity, InvoiceDetailsDto> invoiceDetailsConverter) {
    this.invoiceRepository = invoiceRepository;
    this.credentialRepository = credentialRepository;
    this.currencyFormatter = currencyFormatter;
    this.invoiceConverter = invoiceConverter;
    this.invoiceDetailsConverter = invoiceDetailsConverter;
  }

  public byte[] generateInvoiceReport(final String userId, final String invoiceNumber)
      throws ReportsException {

    final InvoiceEntity invoiceEntity =
        this.invoiceRepository.findInvoiceByNumber(userId, invoiceNumber);

    final CredentialEntity credentialEntity =
        this.credentialRepository.findCredentialByBranchId(invoiceEntity.getBranchId());

    final InvoiceDto invoiceDto = this.invoiceConverter.convertFrom(invoiceEntity);

    final InvoiceDetailsDto invoiceDetailsDto =
        this.invoiceDetailsConverter.convertTo(invoiceEntity);

    try {
      final Optional<JasperPrint> jasperPrint =
          this.buildReport(
              this.buildParameters(credentialEntity, invoiceDto, invoiceDetailsDto),
              INVOICE_REPORT_TEMPLATE_DEFAULT);

      if (jasperPrint.isPresent()) {
        return JasperExportManager.exportReportToPdf(jasperPrint.get());
      }

    } catch (JRException e) {
      throw new ReportsException("There was a problem building an Invoice Report (PDF)", e);
    }
    return new byte[] {};
  }

  private HashMap<String, Object> buildParameters(
      CredentialEntity credentialEntity,
      InvoiceDto invoiceDto,
      InvoiceDetailsDto invoiceDetailsDto) {

    final HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("InvoiceType", this.defineInvoiceType(invoiceDto.getInvoiceType()));
    parameters.put("EconomicActivityAndLegalName", buildEconomicActivityAndLegalName(invoiceDto));
    parameters.put(
        "EmitterId", invoiceDto.getInvoiceCredentialDto().getOwnerIdentificationNumber());
    parameters.put("InvoiceNumber", invoiceDto.getInvoiceNumber());
    parameters.put("InvoiceDate", invoiceDto.getCreatedAt());
    parameters.put("InvoicePhone", invoiceDto.getBranchPhone());
    parameters.put("InvoiceEmail", invoiceDto.getBranchEmail());
    String image =
        new String(
                Base64.getDecoder().decode(credentialEntity.getBrandImage()),
                Charset.forName("utf8"))
            .replace("data:image/png;base64,", "")
            .replace("data:image/jpg;base64,", "")
            .replace("data:image/jpeg;base64,", "")
            .replace("data:image/gif;base64,", "");
    parameters.put("CompanyImageBas64", image);
    parameters.put("ReceptorFullName", invoiceDto.getCustomerName());
    parameters.put("ReceptorId", invoiceDto.getCustomerIdentification());
    parameters.put("ReceptorEmail", invoiceDto.getCustomerEmail());
    parameters.put("Subtotal", this.currencyFormatter.twoDecimalFormatString(invoiceDetailsDto.getSubtotal()));
    parameters.put("Taxes", this.currencyFormatter.twoDecimalFormatString(invoiceDetailsDto.getTaxesTotal()));
    parameters.put("Total", this.currencyFormatter.twoDecimalFormatString(invoiceDetailsDto.getTotal()));
    parameters.put(
        "ItemDetailsCollection",
        new JRBeanCollectionDataSource(this.getInvoiceItemReportDtos(invoiceDetailsDto)));
    return parameters;
  }

  private String defineInvoiceType(String invoiceType) {
    if ("1".equals(invoiceType)) {
      return "CONTADO";
    }
    return "";
  }

  private List<InvoiceItemReportDto> getInvoiceItemReportDtos(InvoiceDetailsDto invoiceDetailsDto) {

    return invoiceDetailsDto.getItems().stream()
        .map(
            (item) ->
                InvoiceItemReportDto.builder()
                    .description(item.getDescription())
                    .price(this.currencyFormatter.twoDecimalFormatString(item.getPrice()))
                    .quantity(this.currencyFormatter.twoDecimalFormatString(item.getQuantity()))
                    .total(this.currencyFormatter.twoDecimalFormatString(item.getSubtotal()))
                    .units(item.getUnit())
                    .build())
        .collect(Collectors.toList());
  }

  private String buildEconomicActivityAndLegalName(InvoiceDto invoiceDto) {
    return String.format(
        "%s - %s",
        invoiceDto.getInvoiceCredentialDto().getEconomicActivity(),
        invoiceDto.getInvoiceCredentialDto().getTradeName());
  }

  private Optional<JasperPrint> buildReport(
      final HashMap<String, Object> parameters, final String reportName) throws JRException {

    JasperPrint jasperPrint = null;

    final Optional<URL> url =
        Optional.ofNullable(this.getClass().getClassLoader().getResource(reportName));

    if (url.isPresent()) {
      final JasperDesign jd = JRXmlLoader.load(url.get().getPath());
      final JasperReport report = JasperCompileManager.compileReport(jd);
      jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
    }

    return Optional.ofNullable(jasperPrint);
  }
}
