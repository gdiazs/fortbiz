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
package com.github.gdiazs.fortbiz.hacienda.v43.message;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.guillermods.fortbiz.hacienda.v43.message package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  private static final QName _MensajeHaciendaTipoIdentificacionReceptor_QNAME =
      new QName(
          "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/mensajeHacienda",
          "TipoIdentificacionReceptor");
  private static final QName _MensajeHaciendaNumeroCedulaReceptor_QNAME =
      new QName(
          "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/mensajeHacienda",
          "NumeroCedulaReceptor");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: com.guillermods.fortbiz.hacienda.v43.message
   */
  public ObjectFactory() {}

  /** Create an instance of {@link MensajeHacienda } */
  public MensajeHacienda createMensajeHacienda() {
    return new MensajeHacienda();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  @XmlElementDecl(
      namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/mensajeHacienda",
      name = "TipoIdentificacionReceptor",
      scope = MensajeHacienda.class)
  public JAXBElement<String> createMensajeHaciendaTipoIdentificacionReceptor(String value) {
    return new JAXBElement<String>(
        _MensajeHaciendaTipoIdentificacionReceptor_QNAME,
        String.class,
        MensajeHacienda.class,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  @XmlElementDecl(
      namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/mensajeHacienda",
      name = "NumeroCedulaReceptor",
      scope = MensajeHacienda.class)
  public JAXBElement<String> createMensajeHaciendaNumeroCedulaReceptor(String value) {
    return new JAXBElement<String>(
        _MensajeHaciendaNumeroCedulaReceptor_QNAME, String.class, MensajeHacienda.class, value);
  }
}
