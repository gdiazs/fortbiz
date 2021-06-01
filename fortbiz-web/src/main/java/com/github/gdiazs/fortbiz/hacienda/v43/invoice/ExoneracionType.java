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
package com.github.gdiazs.fortbiz.hacienda.v43.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Java class for ExoneracionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ExoneracionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TipoDocumento"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="01"/&gt;
 *               &lt;enumeration value="02"/&gt;
 *               &lt;enumeration value="03"/&gt;
 *               &lt;enumeration value="04"/&gt;
 *               &lt;enumeration value="05"/&gt;
 *               &lt;enumeration value="06"/&gt;
 *               &lt;enumeration value="07"/&gt;
 *               &lt;enumeration value="99"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NumeroDocumento"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NombreInstitucion"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="160"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FechaEmision" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="PorcentajeExoneracion"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MontoExoneracion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronica}DecimalDineroType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ExoneracionType",
    propOrder = {
      "tipoDocumento",
      "numeroDocumento",
      "nombreInstitucion",
      "fechaEmision",
      "porcentajeExoneracion",
      "montoExoneracion"
    })
public class ExoneracionType {

  @XmlElement(name = "TipoDocumento", required = true)
  protected String tipoDocumento;

  @XmlElement(name = "NumeroDocumento", required = true)
  protected String numeroDocumento;

  @XmlElement(name = "NombreInstitucion", required = true)
  protected String nombreInstitucion;

  @XmlElement(name = "FechaEmision", required = true)
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar fechaEmision;

  @XmlElement(name = "PorcentajeExoneracion", required = true)
  protected BigInteger porcentajeExoneracion;

  @XmlElement(name = "MontoExoneracion", required = true)
  protected BigDecimal montoExoneracion;

  /**
   * Gets the value of the tipoDocumento property.
   *
   * @return possible object is {@link String }
   */
  public String getTipoDocumento() {
    return tipoDocumento;
  }

  /**
   * Sets the value of the tipoDocumento property.
   *
   * @param value allowed object is {@link String }
   */
  public void setTipoDocumento(String value) {
    this.tipoDocumento = value;
  }

  /**
   * Gets the value of the numeroDocumento property.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  /**
   * Sets the value of the numeroDocumento property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroDocumento(String value) {
    this.numeroDocumento = value;
  }

  /**
   * Gets the value of the nombreInstitucion property.
   *
   * @return possible object is {@link String }
   */
  public String getNombreInstitucion() {
    return nombreInstitucion;
  }

  /**
   * Sets the value of the nombreInstitucion property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNombreInstitucion(String value) {
    this.nombreInstitucion = value;
  }

  /**
   * Gets the value of the fechaEmision property.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getFechaEmision() {
    return fechaEmision;
  }

  /**
   * Sets the value of the fechaEmision property.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setFechaEmision(XMLGregorianCalendar value) {
    this.fechaEmision = value;
  }

  /**
   * Gets the value of the porcentajeExoneracion property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getPorcentajeExoneracion() {
    return porcentajeExoneracion;
  }

  /**
   * Sets the value of the porcentajeExoneracion property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setPorcentajeExoneracion(BigInteger value) {
    this.porcentajeExoneracion = value;
  }

  /**
   * Gets the value of the montoExoneracion property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontoExoneracion() {
    return montoExoneracion;
  }

  /**
   * Sets the value of the montoExoneracion property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontoExoneracion(BigDecimal value) {
    this.montoExoneracion = value;
  }
}
