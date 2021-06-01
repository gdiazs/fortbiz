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

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Clave"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="\d{50,50}"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NombreEmisor"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="100"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TipoIdentificacionEmisor"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="01"/&gt;
 *               &lt;enumeration value="02"/&gt;
 *               &lt;enumeration value="03"/&gt;
 *               &lt;enumeration value="04"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NumeroCedulaEmisor"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="12"/&gt;
 *               &lt;pattern value="\d{9,12}"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NombreReceptor" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="100"/&gt;
 *               &lt;minLength value="0"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TipoIdentificacionReceptor" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="01"/&gt;
 *               &lt;enumeration value="02"/&gt;
 *               &lt;enumeration value="03"/&gt;
 *               &lt;enumeration value="04"/&gt;
 *               &lt;enumeration value="05"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NumeroCedulaReceptor" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="12"/&gt;
 *               &lt;pattern value="\d{9,12}"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Mensaje"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;enumeration value="1"/&gt;
 *               &lt;enumeration value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="DetalleMensaje"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MontoTotalImpuesto" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="18"/&gt;
 *               &lt;fractionDigits value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TotalFactura"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="18"/&gt;
 *               &lt;fractionDigits value="5"/&gt;
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
    name = "",
    propOrder = {
      "clave",
      "nombreEmisor",
      "tipoIdentificacionEmisor",
      "numeroCedulaEmisor",
      "nombreReceptor",
      "tipoIdentificacionReceptor",
      "numeroCedulaReceptor",
      "mensaje",
      "detalleMensaje",
      "montoTotalImpuesto",
      "totalFactura"
    })
@XmlRootElement(name = "MensajeHacienda")
public class MensajeHacienda {

  @XmlElement(name = "Clave", required = true)
  protected String clave;

  @XmlElement(name = "NombreEmisor", required = true)
  protected String nombreEmisor;

  @XmlElement(name = "TipoIdentificacionEmisor", required = true)
  protected String tipoIdentificacionEmisor;

  @XmlElement(name = "NumeroCedulaEmisor", required = true)
  protected String numeroCedulaEmisor;

  @XmlElement(name = "NombreReceptor")
  protected String nombreReceptor;

  @XmlElementRef(
      name = "TipoIdentificacionReceptor",
      namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/mensajeHacienda",
      type = JAXBElement.class,
      required = false)
  protected JAXBElement<String> tipoIdentificacionReceptor;

  @XmlElementRef(
      name = "NumeroCedulaReceptor",
      namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/mensajeHacienda",
      type = JAXBElement.class,
      required = false)
  protected JAXBElement<String> numeroCedulaReceptor;

  @XmlElement(name = "Mensaje", required = true)
  protected BigInteger mensaje;

  @XmlElement(name = "DetalleMensaje", required = true)
  protected String detalleMensaje;

  @XmlElement(name = "MontoTotalImpuesto")
  protected BigDecimal montoTotalImpuesto;

  @XmlElement(name = "TotalFactura", required = true)
  protected BigDecimal totalFactura;

  /**
   * Gets the value of the clave property.
   *
   * @return possible object is {@link String }
   */
  public String getClave() {
    return clave;
  }

  /**
   * Sets the value of the clave property.
   *
   * @param value allowed object is {@link String }
   */
  public void setClave(String value) {
    this.clave = value;
  }

  /**
   * Gets the value of the nombreEmisor property.
   *
   * @return possible object is {@link String }
   */
  public String getNombreEmisor() {
    return nombreEmisor;
  }

  /**
   * Sets the value of the nombreEmisor property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNombreEmisor(String value) {
    this.nombreEmisor = value;
  }

  /**
   * Gets the value of the tipoIdentificacionEmisor property.
   *
   * @return possible object is {@link String }
   */
  public String getTipoIdentificacionEmisor() {
    return tipoIdentificacionEmisor;
  }

  /**
   * Sets the value of the tipoIdentificacionEmisor property.
   *
   * @param value allowed object is {@link String }
   */
  public void setTipoIdentificacionEmisor(String value) {
    this.tipoIdentificacionEmisor = value;
  }

  /**
   * Gets the value of the numeroCedulaEmisor property.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroCedulaEmisor() {
    return numeroCedulaEmisor;
  }

  /**
   * Sets the value of the numeroCedulaEmisor property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroCedulaEmisor(String value) {
    this.numeroCedulaEmisor = value;
  }

  /**
   * Gets the value of the nombreReceptor property.
   *
   * @return possible object is {@link String }
   */
  public String getNombreReceptor() {
    return nombreReceptor;
  }

  /**
   * Sets the value of the nombreReceptor property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNombreReceptor(String value) {
    this.nombreReceptor = value;
  }

  /**
   * Gets the value of the tipoIdentificacionReceptor property.
   *
   * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  public JAXBElement<String> getTipoIdentificacionReceptor() {
    return tipoIdentificacionReceptor;
  }

  /**
   * Sets the value of the tipoIdentificacionReceptor property.
   *
   * @param value allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  public void setTipoIdentificacionReceptor(JAXBElement<String> value) {
    this.tipoIdentificacionReceptor = value;
  }

  /**
   * Gets the value of the numeroCedulaReceptor property.
   *
   * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  public JAXBElement<String> getNumeroCedulaReceptor() {
    return numeroCedulaReceptor;
  }

  /**
   * Sets the value of the numeroCedulaReceptor property.
   *
   * @param value allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  public void setNumeroCedulaReceptor(JAXBElement<String> value) {
    this.numeroCedulaReceptor = value;
  }

  /**
   * Gets the value of the mensaje property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getMensaje() {
    return mensaje;
  }

  /**
   * Sets the value of the mensaje property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setMensaje(BigInteger value) {
    this.mensaje = value;
  }

  /**
   * Gets the value of the detalleMensaje property.
   *
   * @return possible object is {@link String }
   */
  public String getDetalleMensaje() {
    return detalleMensaje;
  }

  /**
   * Sets the value of the detalleMensaje property.
   *
   * @param value allowed object is {@link String }
   */
  public void setDetalleMensaje(String value) {
    this.detalleMensaje = value;
  }

  /**
   * Gets the value of the montoTotalImpuesto property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontoTotalImpuesto() {
    return montoTotalImpuesto;
  }

  /**
   * Sets the value of the montoTotalImpuesto property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontoTotalImpuesto(BigDecimal value) {
    this.montoTotalImpuesto = value;
  }

  /**
   * Gets the value of the totalFactura property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTotalFactura() {
    return totalFactura;
  }

  /**
   * Sets the value of the totalFactura property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTotalFactura(BigDecimal value) {
    this.totalFactura = value;
  }
}
