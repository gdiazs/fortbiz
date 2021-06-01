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

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for ImpuestoType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ImpuestoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Codigo"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="01"/&gt;
 *               &lt;enumeration value="02"/&gt;
 *               &lt;enumeration value="03"/&gt;
 *               &lt;enumeration value="04"/&gt;
 *               &lt;enumeration value="05"/&gt;
 *               &lt;enumeration value="06"/&gt;
 *               &lt;enumeration value="07"/&gt;
 *               &lt;enumeration value="08"/&gt;
 *               &lt;enumeration value="12"/&gt;
 *               &lt;enumeration value="99"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CodigoTarifa" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *               &lt;minLength value="2"/&gt;
 *               &lt;enumeration value="01"/&gt;
 *               &lt;enumeration value="02"/&gt;
 *               &lt;enumeration value="03"/&gt;
 *               &lt;enumeration value="04"/&gt;
 *               &lt;enumeration value="05"/&gt;
 *               &lt;enumeration value="06"/&gt;
 *               &lt;enumeration value="07"/&gt;
 *               &lt;enumeration value="08"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Tarifa" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="4"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FactorIVA" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="5"/&gt;
 *               &lt;fractionDigits value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Monto" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}DecimalDineroType"/&gt;
 *         &lt;element name="MontoExportacion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}DecimalDineroType" minOccurs="0"/&gt;
 *         &lt;element name="Exoneracion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}ExoneracionType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ImpuestoType",
    propOrder = {
      "codigo",
      "codigoTarifa",
      "tarifa",
      "factorIVA",
      "monto",
      "montoExportacion",
      "exoneracion"
    })
public class ImpuestoType {

  @XmlElement(name = "Codigo", required = true)
  protected String codigo;

  @XmlElement(name = "CodigoTarifa")
  protected String codigoTarifa;

  @XmlElement(name = "Tarifa")
  protected BigDecimal tarifa;

  @XmlElement(name = "FactorIVA")
  protected BigDecimal factorIVA;

  @XmlElement(name = "Monto", required = true)
  protected BigDecimal monto;

  @XmlElement(name = "MontoExportacion")
  protected BigDecimal montoExportacion;

  @XmlElement(name = "Exoneracion")
  protected ExoneracionType exoneracion;

  /**
   * Gets the value of the codigo property.
   *
   * @return possible object is {@link String }
   */
  public String getCodigo() {
    return codigo;
  }

  /**
   * Sets the value of the codigo property.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodigo(String value) {
    this.codigo = value;
  }

  /**
   * Gets the value of the codigoTarifa property.
   *
   * @return possible object is {@link String }
   */
  public String getCodigoTarifa() {
    return codigoTarifa;
  }

  /**
   * Sets the value of the codigoTarifa property.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodigoTarifa(String value) {
    this.codigoTarifa = value;
  }

  /**
   * Gets the value of the tarifa property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTarifa() {
    return tarifa;
  }

  /**
   * Sets the value of the tarifa property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTarifa(BigDecimal value) {
    this.tarifa = value;
  }

  /**
   * Gets the value of the factorIVA property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getFactorIVA() {
    return factorIVA;
  }

  /**
   * Sets the value of the factorIVA property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setFactorIVA(BigDecimal value) {
    this.factorIVA = value;
  }

  /**
   * Gets the value of the monto property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMonto() {
    return monto;
  }

  /**
   * Sets the value of the monto property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMonto(BigDecimal value) {
    this.monto = value;
  }

  /**
   * Gets the value of the montoExportacion property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontoExportacion() {
    return montoExportacion;
  }

  /**
   * Sets the value of the montoExportacion property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontoExportacion(BigDecimal value) {
    this.montoExportacion = value;
  }

  /**
   * Gets the value of the exoneracion property.
   *
   * @return possible object is {@link ExoneracionType }
   */
  public ExoneracionType getExoneracion() {
    return exoneracion;
  }

  /**
   * Sets the value of the exoneracion property.
   *
   * @param value allowed object is {@link ExoneracionType }
   */
  public void setExoneracion(ExoneracionType value) {
    this.exoneracion = value;
  }
}
