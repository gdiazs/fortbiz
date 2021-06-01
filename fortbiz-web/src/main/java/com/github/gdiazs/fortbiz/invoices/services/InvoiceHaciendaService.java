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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.github.gdiazs.fortbiz.branches.repositories.BranchRepository;
import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.credentials.repository.CredentialRepository;
import com.github.gdiazs.fortbiz.customers.repositories.CustomerRepository;
import com.github.gdiazs.fortbiz.entities.BranchEntity;
import com.github.gdiazs.fortbiz.entities.CredentialEntity;
import com.github.gdiazs.fortbiz.entities.CustomerEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.hacienda.v43.invoice.*;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDetailsDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceItemDto;
import com.github.gdiazs.fortbiz.invoices.exeptions.InvoiceHaciendaException;
import com.github.gdiazs.fortbiz.invoices.repositories.InvoiceRepository;

@Singleton
public class InvoiceHaciendaService {

  private final CredentialRepository credentialRepository;
  private final BranchRepository branchRepository;
  private final CustomerRepository customerRepository;
  private final InvoiceRepository invoiceRepository;
  private final ModelConverter<InvoiceDto, InvoiceEntity> invoiceConverter;
  private final ModelConverter<InvoiceEntity, InvoiceDetailsDto> invoiceDetailsConverter;

  @Inject
  public InvoiceHaciendaService(
      CredentialRepository credentialRepository,
      BranchRepository branchRepository,
      CustomerRepository customerRepository,
      InvoiceRepository invoiceRepository,
      ModelConverter<InvoiceDto, InvoiceEntity> invoiceConverter,
      ModelConverter<InvoiceEntity, InvoiceDetailsDto> invoiceDetailsConverter) {
    this.credentialRepository = credentialRepository;
    this.branchRepository = branchRepository;
    this.customerRepository = customerRepository;
    this.invoiceRepository = invoiceRepository;
    this.invoiceConverter = invoiceConverter;
    this.invoiceDetailsConverter = invoiceDetailsConverter;
  }

  public FacturaElectronica fillElectronicInvoice(final String userId, final String invoiceNumber) {

    final InvoiceEntity invoiceEntity =
        this.invoiceRepository.findInvoiceByNumber(userId, invoiceNumber);

    final CredentialEntity credentialEntity =
        this.credentialRepository.findCredentialByBranchId(invoiceEntity.getBranchId());
    final InvoiceDto invoiceDto = this.invoiceConverter.convertFrom(invoiceEntity);

    final InvoiceDetailsDto invoiceDetailsDto =
        this.invoiceDetailsConverter.convertTo(invoiceEntity);

    ObjectFactory objectFactory = new ObjectFactory();
    FacturaElectronica facturaElectronica = objectFactory.createFacturaElectronica();
    try {
      facturaElectronica.setClave(invoiceEntity.getHaciendaKey());
      facturaElectronica.setCodigoActividad(credentialEntity.getActivityCode()); // biene de loz kredemziales
      facturaElectronica.setNumeroConsecutivo(invoiceEntity.getInvoiceNumber());
      facturaElectronica.setFechaEmision(
          buildFechaDeEmision());
      facturaElectronica.setEmisor(buildEmisor(objectFactory, invoiceEntity.getBranchId()));
      facturaElectronica.setReceptor(buildReceptor(objectFactory, invoiceEntity.getCustomerId()));
      facturaElectronica.setCondicionVenta(invoiceEntity.getInvoiceType().toString());
      facturaElectronica.getMedioPago().add(invoiceEntity.getPaymentMethod());
      facturaElectronica.setDetalleServicio(
          buildDetalleServicio(objectFactory, invoiceDetailsDto.getItems()));
      facturaElectronica.setResumenFactura(buildResumenDeFactura(objectFactory, invoiceDetailsDto));
    } catch (DatatypeConfigurationException e) {
      throw new InvoiceHaciendaException(
          "There was an unexpected error building FacturaElectronica", e);
    }

    return facturaElectronica;
  }

  private XMLGregorianCalendar buildFechaDeEmision()
      throws DatatypeConfigurationException {

    Date date = new Date(System.currentTimeMillis());

    SimpleDateFormat sdf;
    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    sdf.setTimeZone(TimeZone.getTimeZone("America/Costa_Rica"));
    String invoiceDate = sdf.format(date);
    return DatatypeFactory.newInstance().newXMLGregorianCalendar(invoiceDate);
  }

  private EmisorType buildEmisor(ObjectFactory objectFactory, String branchId) {
    EmisorType emisorType = objectFactory.createEmisorType();

    CredentialEntity credentialEntity =
        this.credentialRepository.findCredentialByBranchId(branchId);

    BranchEntity branchEntity = this.branchRepository.findBy(branchId);

    emisorType.setNombre(branchEntity.getName());
    emisorType.setIdentificacion(
        buildIdentificationType(
            objectFactory,
            credentialEntity.getIndentificationType(),
            credentialEntity.getIndentificationNumber()));
    emisorType.setUbicacion(buildUbicacionType(objectFactory, credentialEntity.getLocalization(), branchEntity.getAddress()));
    emisorType.setCorreoElectronico(branchEntity.getEmail());
    return emisorType;
  }

  private IdentificacionType buildIdentificationType(
      ObjectFactory objectFactory, String type, String number) {
    IdentificacionType identificacionType = objectFactory.createIdentificacionType();
    identificacionType.setTipo(type);
    identificacionType.setNumero(number);
    return identificacionType;
  }

  private UbicacionType buildUbicacionType(ObjectFactory objectFactory, String localization, String otrasSenas) {
    UbicacionType ubicacionType = objectFactory.createUbicacionType();
    String[] localizationArray = localization.split("/");

    ubicacionType.setProvincia(new BigInteger(localizationArray[0]));
    ubicacionType.setCanton(new BigInteger(localizationArray[1]));
    ubicacionType.setDistrito(new BigInteger(localizationArray[2]));
    ubicacionType.setOtrasSenas(otrasSenas);
    return ubicacionType;
  }

  private ReceptorType buildReceptor(ObjectFactory objectFactory, String customerId) {
    ReceptorType receptorType = objectFactory.createReceptorType();
    if(customerId != null){
      CustomerEntity customerEntity = this.customerRepository.findBy(customerId);
      receptorType.setNombre(customerEntity.getCustomerName());
      receptorType.setIdentificacion(
              buildIdentificationType(objectFactory, customerEntity.getIdentificationType(), customerEntity.getIdentificationNumber()));
      return receptorType;
    }else{
      receptorType.setNombre("");
      receptorType.setIdentificacion(
              buildIdentificationType(objectFactory, "", ""));
      return receptorType;
    }
  }

  private FacturaElectronica.DetalleServicio buildDetalleServicio(
      ObjectFactory objectFactory, List<InvoiceItemDto> listItems) {
    FacturaElectronica.DetalleServicio detalleServicio =
        objectFactory.createFacturaElectronicaDetalleServicio();
    List<FacturaElectronica.DetalleServicio.LineaDetalle> lineaDetalle =
        detalleServicio.getLineaDetalle();

    for(int i=0; i<listItems.size(); i++){
      BigInteger numberLine = new BigInteger(String.valueOf(i + 1));
      lineaDetalle.add(buildLineaDetalle(objectFactory, listItems.get(i), numberLine));
    }
    return detalleServicio;
  }

  private FacturaElectronica.DetalleServicio.LineaDetalle buildLineaDetalle(
      ObjectFactory objectFactory, InvoiceItemDto item, BigInteger numberLine) {
    FacturaElectronica.DetalleServicio.LineaDetalle lineaDetalle =
        objectFactory.createFacturaElectronicaDetalleServicioLineaDetalle();
    lineaDetalle.setNumeroLinea(numberLine);
    lineaDetalle.setCodigo(null);
    lineaDetalle.setCantidad(new BigDecimal(item.getQuantity()));
    lineaDetalle.setUnidadMedida(item.getUnit());
    lineaDetalle.setDetalle(item.getDescription());
    lineaDetalle.setPrecioUnitario(new BigDecimal(item.getPrice()));
    lineaDetalle.setMontoTotal(new BigDecimal(item.getSubtotal()));
    lineaDetalle.setSubTotal(new BigDecimal(item.getSubtotal()));
    lineaDetalle.getImpuesto().add(buildImpuestoType(objectFactory, item));
    lineaDetalle.setMontoTotalLinea(new BigDecimal(item.getTotalLine()));
    return lineaDetalle;
  }

  private ImpuestoType buildImpuestoType(ObjectFactory objectFactory, InvoiceItemDto item) {
    ImpuestoType impuestoType = objectFactory.createImpuestoType();
    //Modificar para añadir más códigos
    impuestoType.setCodigo("01"); //queda en 01 por defecto, impuesto de valor agregado
    impuestoType.setCodigoTarifa("08"); // queda 08 por defecto por tarifa general

    String tax = item.getTax().replace("%", "");
    impuestoType.setTarifa(new BigDecimal(tax));
    impuestoType.setMonto(new BigDecimal(item.getTaxSubtotal()));
    return impuestoType;
  }

  private FacturaElectronica.ResumenFactura buildResumenDeFactura(
      ObjectFactory objectFactory, InvoiceDetailsDto invoiceDetailsDto) {
    FacturaElectronica.ResumenFactura resumenDeFactura =
        objectFactory.createFacturaElectronicaResumenFactura();
    resumenDeFactura.setTotalVenta(new BigDecimal(invoiceDetailsDto.getSubtotal()));
    resumenDeFactura.setTotalVentaNeta(new BigDecimal(invoiceDetailsDto.getSubtotal()));
    resumenDeFactura.setTotalImpuesto(new BigDecimal(invoiceDetailsDto.getTaxesTotal()));
    resumenDeFactura.setTotalComprobante(new BigDecimal(invoiceDetailsDto.getTotal()));
    return resumenDeFactura;
  }
}
