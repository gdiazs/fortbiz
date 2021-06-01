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

import java.io.StringWriter;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import com.github.gdiazs.fortbiz.reports.exceptions.ReportsException;
import com.github.gdiazs.fortbiz.reports.services.InvoiceReportService;
import com.github.gdiazs.fortbiz.digitalsignature.DigitalSignatureService;
import com.github.gdiazs.fortbiz.hacienda.v43.invoice.FacturaElectronica;
import com.github.gdiazs.fortbiz.cashregisters.repositories.CashRegisterRepository;
import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.credentials.repository.CredentialRepository;
import com.github.gdiazs.fortbiz.customers.repositories.CustomerRepository;
import com.github.gdiazs.fortbiz.invoices.repositories.InvoiceRepository;
import com.github.gdiazs.fortbiz.products.repository.InvoiceItemRepository;
import com.github.gdiazs.fortbiz.products.repository.ProductRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.github.gdiazs.fortbiz.branches.repositories.BranchRepository;
import com.github.gdiazs.fortbiz.entities.BranchEntity;
import com.github.gdiazs.fortbiz.entities.CashRegisterEntity;
import com.github.gdiazs.fortbiz.entities.CredentialEntity;
import com.github.gdiazs.fortbiz.entities.CustomerEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceItemEntity;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceCustomerDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDetailsDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceRequestKeyGeneratorDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceRequestNextNumber;

@Singleton
public class InvoiceService {

  private final InvoiceRepository invoiceRepository;
  private final BranchRepository branchRepository;
  private final CashRegisterRepository cashRegisterRepository;
  private final CustomerRepository customerRepository;
  private final InvoiceItemRepository invoiceItemRepository;
  private final ProductRepository productRepository;
  private final CredentialRepository credentialRepository;
  private final InvoiceNumberGeneratorService invoiceNumberGenerator;
  private final InvoiceNumberGeneratorService invoiceNumberGeneratorService;
  private final InvoiceReportService invoiceReportService;
  private final InvoiceHaciendaService invoiceHaciendaService;
  private final DigitalSignatureService<String> digitalSignatureService;

  private final ModelConverter<InvoiceDto, InvoiceEntity> invoiceConverter;
  private final ModelConverter<InvoiceCustomerDto, CustomerEntity> customerConverter;
  private final ModelConverter<InvoiceEntity, InvoiceDetailsDto> invoiceDetailsConverter;

  private final String haciendaApi;
  private final String ENCODING = "UTF8";

  @Inject
  public InvoiceService(
      final InvoiceRepository invoiceRepository,
      final BranchRepository branchRepository,
      final CashRegisterRepository cashRegisterRepository,
      final InvoiceNumberGeneratorService invoiceNumberGenerator,
      final CustomerRepository customerRepository,
      final InvoiceItemRepository invoiceItemRepository,
      final ProductRepository productRepository,
      final CredentialRepository credentialRepository,
      final InvoiceNumberGeneratorService invoiceNumberGeneratorService,
      final InvoiceReportService invoiceReportService,
      final DigitalSignatureService<String> digitalSignatureService,
      final ModelConverter<InvoiceDto, InvoiceEntity> invoiceConverter,
      final ModelConverter<InvoiceCustomerDto, CustomerEntity> customerConverter,
      final ModelConverter<InvoiceEntity, InvoiceDetailsDto> invoiceDetailsConverter,
      InvoiceHaciendaService invoiceHaciendaService,
      @ConfigProperty(name = "fortbiz.hacienda.api") String haciendaApi) {
    super();
    this.invoiceRepository = invoiceRepository;
    this.branchRepository = branchRepository;
    this.cashRegisterRepository = cashRegisterRepository;
    this.invoiceNumberGenerator = invoiceNumberGenerator;
    this.customerRepository = customerRepository;
    this.invoiceItemRepository = invoiceItemRepository;
    this.productRepository = productRepository;
    this.credentialRepository = credentialRepository;
    this.invoiceReportService = invoiceReportService;
    this.digitalSignatureService = digitalSignatureService;
    this.invoiceHaciendaService = invoiceHaciendaService;
    this.invoiceConverter = invoiceConverter;
    this.customerConverter = customerConverter;
    this.invoiceDetailsConverter = invoiceDetailsConverter;
    this.invoiceNumberGeneratorService = invoiceNumberGeneratorService;
    this.haciendaApi = haciendaApi;
  }

  @Transactional
  public InvoiceDto createHeaderInvoice(InvoiceDto invoiceDto, String userId) {
    final BranchEntity branchEntity = this.branchRepository.findBy(invoiceDto.getBranchId());
    final List<CashRegisterEntity> cashRegisters =
        this.cashRegisterRepository.findAllCashRegisterByBranchId(invoiceDto.getBranchId());

    final Long voucherNumber = generateNextInvoiceNumber(invoiceDto.getBranchId(), userId);
    int firstCashRegister =
        !cashRegisters.isEmpty() ? cashRegisters.get(0).getCashRegisterNumber() : 0;

    final InvoiceEntity invoiceEntity = this.invoiceConverter.convertTo(invoiceDto);

    final InvoiceRequestNextNumber invoiceRequestNextNumber =
        InvoiceRequestNextNumber.builder()
            .branchNumber(branchEntity.getBranchNumber())
            .invoiceType(Integer.parseInt(invoiceDto.getInvoiceType()))
            .registerCashNumber(firstCashRegister)
            .voucherNumber(voucherNumber)
            .build();

    invoiceEntity.setInvoiceNumber(
        this.invoiceNumberGenerator.generateNextNumber(invoiceRequestNextNumber));

    final InvoiceEntity savedInvoice = this.invoiceRepository.saveAndFlush(invoiceEntity);

    return this.invoiceConverter.convertFrom(savedInvoice);
  }

  @Transactional
  public List<InvoiceDto> findInvoicesByBranchId(String userId, String branchId) {
    return (List<InvoiceDto>)
        this.invoiceConverter.convertAllFrom(
            this.invoiceRepository.findInvoicesByBranchIdAndUserId(userId, branchId));
  }

  @Transactional
  public void deleteInvoiceByNumber(String userId, String invoiceNumber) {
    InvoiceEntity invoiceEntity = this.invoiceRepository.findInvoiceByNumber(userId, invoiceNumber);
    this.invoiceRepository.remove(invoiceEntity);
  }

  public InvoiceDetailsDto findInvoiceDetails(String userId, String invoiceNumber) {
    final InvoiceEntity foundInvoice =
        this.invoiceRepository.findInvoiceByNumber(userId, invoiceNumber);

    final InvoiceDetailsDto invoiceDetailsDto =
        this.invoiceDetailsConverter.convertTo(foundInvoice);

    return invoiceDetailsDto;
  }

  private Long generateNextInvoiceNumber(String branchId, String userId) {

    final Long invoiceCount =
        this.invoiceRepository.countInvoicesByBranchIdAndUserId(branchId, userId);

    if (invoiceCount == 0) {
      return invoiceCount + 1L;
    } else {
      final InvoiceEntity invoiceEntity =
          this.invoiceRepository.findInvoicesByBranchIdAndUserIdOrderByDate(userId, branchId, 1);
      final Long voucherNumber =
          Long.parseLong(
              invoiceEntity
                  .getInvoiceNumber()
                  .substring(invoiceEntity.getInvoiceNumber().length() - 10));
      return voucherNumber + 1;
    }
  }

  public InvoiceDto findInvoiceByNumber(String invoiceNumber, String userId) {
    final InvoiceEntity invoiceEntity =
        this.invoiceRepository.findInvoiceByNumber(userId, invoiceNumber);
    var invoiceDto = invoiceConverter.convertFrom(invoiceEntity);
    if (null != invoiceEntity.getCustomerId()) {
      CustomerEntity customerEntity = this.customerRepository.findBy(invoiceEntity.getCustomerId());
      invoiceDto.setInvoicesCustomer(customerConverter.convertFrom(customerEntity));
    }
    return invoiceDto;
  }

  @Transactional
  public InvoiceDetailsDto deleteInvoiceDetail(
      String userId, String invoiceNumber, String idProduct) {
    InvoiceEntity invoiceEntity = this.invoiceRepository.findInvoiceByNumber(userId, invoiceNumber);
    InvoiceItemEntity invoiceItemEntity =
        this.invoiceItemRepository.findInvoiceItem(invoiceEntity.getId(), idProduct, userId);
    this.invoiceItemRepository.remove(invoiceItemEntity);

    var product = productRepository.findBy(idProduct);
    productRepository.remove(product);

    return this.findInvoiceDetails(userId, invoiceNumber);
  }

  @Transactional
  public void sendInvoiceToHacienda(String userId, String invoiceNumber) throws ReportsException, JAXBException {

    InvoiceEntity invoiceEntity = this.invoiceRepository.findInvoiceByNumber(userId, invoiceNumber);

    CredentialEntity credentialEntity =
        this.credentialRepository.findCredentialByBranchId(invoiceEntity.getBranchId());

    InvoiceRequestKeyGeneratorDto invoiceRequestKeyGeneratorDto =
        InvoiceRequestKeyGeneratorDto.builder()
            .countryCode("506")
            .emitterIdentification(credentialEntity.getIndentificationNumber())
            .invoiceNumber(invoiceNumber)
            .build();

    String haciendaKey =
        invoiceNumberGeneratorService.generateKeyHacienda(invoiceRequestKeyGeneratorDto);
    invoiceEntity.setHaciendaKey(haciendaKey);

    this.invoiceRepository.save(invoiceEntity);

    //this.invoiceReportService.generateInvoiceReport(userId, invoiceNumber);

    InvoiceDetailsDto invoiDetailsDto = findInvoiceDetails(userId, invoiceNumber);
    var electronicInvoice =
        invoiceHaciendaService.fillElectronicInvoice(userId, invoiceNumber);

    JAXBContext contextObj = JAXBContext.newInstance(FacturaElectronica.class);
    Marshaller marshallerObj = contextObj.createMarshaller();
    marshallerObj.setProperty(Marshaller.JAXB_ENCODING, ENCODING);
    marshallerObj.setProperty(
        Marshaller.JAXB_SCHEMA_LOCATION,
        "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronica "
            + "https://www.hacienda.go.cr/ATV/ComprobanteElectronico/docs/esquemas/2016/v4.3/FacturaElectronica_V4.3.xsd");
    final var sw = new StringWriter();
    marshallerObj.marshal(electronicInvoice, new StreamResult(sw));

    final byte[] keyStore = credentialEntity.getKeystoreBytes();
    System.out.println(sw.toString());
    // digitalSignatureService.sign(sw.toString(), keyStore, "1234").contains("xades"));

  }
}