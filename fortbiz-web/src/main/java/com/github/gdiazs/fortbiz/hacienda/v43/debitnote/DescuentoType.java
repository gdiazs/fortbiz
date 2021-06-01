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

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for DescuentoType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DescuentoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MontoDescuento" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaDebitoElectronica}DecimalDineroType"/&gt;
 *         &lt;element name="NaturalezaDescuento"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="80"/&gt;
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
    name = "DescuentoType",
    propOrder = {"montoDescuento", "naturalezaDescuento"})
public class DescuentoType {

  @XmlElement(name = "MontoDescuento", required = true)
  protected BigDecimal montoDescuento;

  @XmlElement(name = "NaturalezaDescuento", required = true)
  protected String naturalezaDescuento;

  /**
   * Gets the value of the montoDescuento property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontoDescuento() {
    return montoDescuento;
  }

  /**
   * Sets the value of the montoDescuento property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontoDescuento(BigDecimal value) {
    this.montoDescuento = value;
  }

  /**
   * Gets the value of the naturalezaDescuento property.
   *
   * @return possible object is {@link String }
   */
  public String getNaturalezaDescuento() {
    return naturalezaDescuento;
  }

  /**
   * Sets the value of the naturalezaDescuento property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNaturalezaDescuento(String value) {
    this.naturalezaDescuento = value;
  }
}
