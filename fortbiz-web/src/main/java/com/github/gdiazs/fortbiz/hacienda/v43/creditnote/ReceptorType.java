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
package com.github.gdiazs.fortbiz.hacienda.v43.creditnote;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for ReceptorType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ReceptorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Nombre"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="100"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Identificacion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}IdentificacionType" minOccurs="0"/&gt;
 *         &lt;element name="IdentificacionExtranjero" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NombreComercial" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="80"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Ubicacion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}UbicacionType" minOccurs="0"/&gt;
 *         &lt;element name="OtrasSenasExtranjero" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="300"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Telefono" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}TelefonoType" minOccurs="0"/&gt;
 *         &lt;element name="Fax" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}TelefonoType" minOccurs="0"/&gt;
 *         &lt;element name="CorreoElectronico" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="160"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ReceptorType",
    propOrder = {
      "nombre",
      "identificacion",
      "identificacionExtranjero",
      "nombreComercial",
      "ubicacion",
      "otrasSenasExtranjero",
      "telefono",
      "fax",
      "correoElectronico"
    })
public class ReceptorType {

  @XmlElement(name = "Nombre", required = true)
  protected String nombre;

  @XmlElement(name = "Identificacion")
  protected IdentificacionType identificacion;

  @XmlElement(name = "IdentificacionExtranjero")
  protected String identificacionExtranjero;

  @XmlElement(name = "NombreComercial")
  protected String nombreComercial;

  @XmlElement(name = "Ubicacion")
  protected UbicacionType ubicacion;

  @XmlElement(name = "OtrasSenasExtranjero")
  protected String otrasSenasExtranjero;

  @XmlElement(name = "Telefono")
  protected TelefonoType telefono;

  @XmlElement(name = "Fax")
  protected TelefonoType fax;

  @XmlElement(name = "CorreoElectronico")
  protected String correoElectronico;

  /**
   * Gets the value of the nombre property.
   *
   * @return possible object is {@link String }
   */
  public String getNombre() {
    return nombre;
  }

  /**
   * Sets the value of the nombre property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNombre(String value) {
    this.nombre = value;
  }

  /**
   * Gets the value of the identificacion property.
   *
   * @return possible object is {@link IdentificacionType }
   */
  public IdentificacionType getIdentificacion() {
    return identificacion;
  }

  /**
   * Sets the value of the identificacion property.
   *
   * @param value allowed object is {@link IdentificacionType }
   */
  public void setIdentificacion(IdentificacionType value) {
    this.identificacion = value;
  }

  /**
   * Gets the value of the identificacionExtranjero property.
   *
   * @return possible object is {@link String }
   */
  public String getIdentificacionExtranjero() {
    return identificacionExtranjero;
  }

  /**
   * Sets the value of the identificacionExtranjero property.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdentificacionExtranjero(String value) {
    this.identificacionExtranjero = value;
  }

  /**
   * Gets the value of the nombreComercial property.
   *
   * @return possible object is {@link String }
   */
  public String getNombreComercial() {
    return nombreComercial;
  }

  /**
   * Sets the value of the nombreComercial property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNombreComercial(String value) {
    this.nombreComercial = value;
  }

  /**
   * Gets the value of the ubicacion property.
   *
   * @return possible object is {@link UbicacionType }
   */
  public UbicacionType getUbicacion() {
    return ubicacion;
  }

  /**
   * Sets the value of the ubicacion property.
   *
   * @param value allowed object is {@link UbicacionType }
   */
  public void setUbicacion(UbicacionType value) {
    this.ubicacion = value;
  }

  /**
   * Gets the value of the otrasSenasExtranjero property.
   *
   * @return possible object is {@link String }
   */
  public String getOtrasSenasExtranjero() {
    return otrasSenasExtranjero;
  }

  /**
   * Sets the value of the otrasSenasExtranjero property.
   *
   * @param value allowed object is {@link String }
   */
  public void setOtrasSenasExtranjero(String value) {
    this.otrasSenasExtranjero = value;
  }

  /**
   * Gets the value of the telefono property.
   *
   * @return possible object is {@link TelefonoType }
   */
  public TelefonoType getTelefono() {
    return telefono;
  }

  /**
   * Sets the value of the telefono property.
   *
   * @param value allowed object is {@link TelefonoType }
   */
  public void setTelefono(TelefonoType value) {
    this.telefono = value;
  }

  /**
   * Gets the value of the fax property.
   *
   * @return possible object is {@link TelefonoType }
   */
  public TelefonoType getFax() {
    return fax;
  }

  /**
   * Sets the value of the fax property.
   *
   * @param value allowed object is {@link TelefonoType }
   */
  public void setFax(TelefonoType value) {
    this.fax = value;
  }

  /**
   * Gets the value of the correoElectronico property.
   *
   * @return possible object is {@link String }
   */
  public String getCorreoElectronico() {
    return correoElectronico;
  }

  /**
   * Sets the value of the correoElectronico property.
   *
   * @param value allowed object is {@link String }
   */
  public void setCorreoElectronico(String value) {
    this.correoElectronico = value;
  }
}
