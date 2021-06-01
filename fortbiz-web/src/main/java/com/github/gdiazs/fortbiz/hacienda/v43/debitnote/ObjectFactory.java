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
package com.github.gdiazs.fortbiz.hacienda.v43.debitnote;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.aedeatech.fortbiz.hacienda.v43.debitnote package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  private static final QName _EmisorTypeTelefono_QNAME =
      new QName(
          "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaDebitoElectronica",
          "Telefono");
  private static final QName _EmisorTypeFax_QNAME =
      new QName(
          "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaDebitoElectronica",
          "Fax");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: com.aedeatech.fortbiz.hacienda.v43.debitnote
   */
  public ObjectFactory() {}

  /** Create an instance of {@link NotaDebitoElectronica } */
  public NotaDebitoElectronica createNotaDebitoElectronica() {
    return new NotaDebitoElectronica();
  }

  /** Create an instance of {@link NotaDebitoElectronica.Otros } */
  public NotaDebitoElectronica.Otros createNotaDebitoElectronicaOtros() {
    return new NotaDebitoElectronica.Otros();
  }

  /** Create an instance of {@link NotaDebitoElectronica.DetalleServicio } */
  public NotaDebitoElectronica.DetalleServicio createNotaDebitoElectronicaDetalleServicio() {
    return new NotaDebitoElectronica.DetalleServicio();
  }

  /** Create an instance of {@link EmisorType } */
  public EmisorType createEmisorType() {
    return new EmisorType();
  }

  /** Create an instance of {@link ReceptorType } */
  public ReceptorType createReceptorType() {
    return new ReceptorType();
  }

  /** Create an instance of {@link OtrosCargosType } */
  public OtrosCargosType createOtrosCargosType() {
    return new OtrosCargosType();
  }

  /** Create an instance of {@link NotaDebitoElectronica.ResumenFactura } */
  public NotaDebitoElectronica.ResumenFactura createNotaDebitoElectronicaResumenFactura() {
    return new NotaDebitoElectronica.ResumenFactura();
  }

  /** Create an instance of {@link NotaDebitoElectronica.InformacionReferencia } */
  public NotaDebitoElectronica.InformacionReferencia
      createNotaDebitoElectronicaInformacionReferencia() {
    return new NotaDebitoElectronica.InformacionReferencia();
  }

  /** Create an instance of {@link IdentificacionType } */
  public IdentificacionType createIdentificacionType() {
    return new IdentificacionType();
  }

  /** Create an instance of {@link UbicacionType } */
  public UbicacionType createUbicacionType() {
    return new UbicacionType();
  }

  /** Create an instance of {@link TelefonoType } */
  public TelefonoType createTelefonoType() {
    return new TelefonoType();
  }

  /** Create an instance of {@link ExoneracionType } */
  public ExoneracionType createExoneracionType() {
    return new ExoneracionType();
  }

  /** Create an instance of {@link ImpuestoType } */
  public ImpuestoType createImpuestoType() {
    return new ImpuestoType();
  }

  /** Create an instance of {@link CodigoType } */
  public CodigoType createCodigoType() {
    return new CodigoType();
  }

  /** Create an instance of {@link DescuentoType } */
  public DescuentoType createDescuentoType() {
    return new DescuentoType();
  }

  /** Create an instance of {@link CodigoMonedaType } */
  public CodigoMonedaType createCodigoMonedaType() {
    return new CodigoMonedaType();
  }

  /** Create an instance of {@link NotaDebitoElectronica.Otros.OtroTexto } */
  public NotaDebitoElectronica.Otros.OtroTexto createNotaDebitoElectronicaOtrosOtroTexto() {
    return new NotaDebitoElectronica.Otros.OtroTexto();
  }

  /** Create an instance of {@link NotaDebitoElectronica.Otros.OtroContenido } */
  public NotaDebitoElectronica.Otros.OtroContenido createNotaDebitoElectronicaOtrosOtroContenido() {
    return new NotaDebitoElectronica.Otros.OtroContenido();
  }

  /** Create an instance of {@link NotaDebitoElectronica.DetalleServicio.LineaDetalle } */
  public NotaDebitoElectronica.DetalleServicio.LineaDetalle
      createNotaDebitoElectronicaDetalleServicioLineaDetalle() {
    return new NotaDebitoElectronica.DetalleServicio.LineaDetalle();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link TelefonoType }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link TelefonoType }{@code >}
   */
  @XmlElementDecl(
      namespace =
          "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaDebitoElectronica",
      name = "Telefono",
      scope = EmisorType.class)
  public JAXBElement<TelefonoType> createEmisorTypeTelefono(TelefonoType value) {
    return new JAXBElement<TelefonoType>(
        _EmisorTypeTelefono_QNAME, TelefonoType.class, EmisorType.class, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link TelefonoType }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link TelefonoType }{@code >}
   */
  @XmlElementDecl(
      namespace =
          "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaDebitoElectronica",
      name = "Fax",
      scope = EmisorType.class)
  public JAXBElement<TelefonoType> createEmisorTypeFax(TelefonoType value) {
    return new JAXBElement<TelefonoType>(
        _EmisorTypeFax_QNAME, TelefonoType.class, EmisorType.class, value);
  }
}
