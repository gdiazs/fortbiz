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

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for UbicacionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="UbicacionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Provincia"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger"&gt;
 *               &lt;totalDigits value="1"/&gt;
 *               &lt;pattern value="\d"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Canton"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger"&gt;
 *               &lt;totalDigits value="2"/&gt;
 *               &lt;pattern value="\d\d"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Distrito"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger"&gt;
 *               &lt;totalDigits value="2"/&gt;
 *               &lt;pattern value="\d\d"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Barrio" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger"&gt;
 *               &lt;totalDigits value="2"/&gt;
 *               &lt;pattern value="\d\d"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="OtrasSenas"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="250"/&gt;
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
    name = "UbicacionType",
    propOrder = {"provincia", "canton", "distrito", "barrio", "otrasSenas"})
public class UbicacionType {

  @XmlElement(name = "Provincia", required = true)
  protected BigInteger provincia;

  @XmlElement(name = "Canton", required = true)
  protected BigInteger canton;

  @XmlElement(name = "Distrito", required = true)
  protected BigInteger distrito;

  @XmlElement(name = "Barrio")
  protected BigInteger barrio;

  @XmlElement(name = "OtrasSenas", required = true)
  protected String otrasSenas;

  /**
   * Gets the value of the provincia property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getProvincia() {
    return provincia;
  }

  /**
   * Sets the value of the provincia property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setProvincia(BigInteger value) {
    this.provincia = value;
  }

  /**
   * Gets the value of the canton property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getCanton() {
    return canton;
  }

  /**
   * Sets the value of the canton property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setCanton(BigInteger value) {
    this.canton = value;
  }

  /**
   * Gets the value of the distrito property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getDistrito() {
    return distrito;
  }

  /**
   * Sets the value of the distrito property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setDistrito(BigInteger value) {
    this.distrito = value;
  }

  /**
   * Gets the value of the barrio property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getBarrio() {
    return barrio;
  }

  /**
   * Sets the value of the barrio property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setBarrio(BigInteger value) {
    this.barrio = value;
  }

  /**
   * Gets the value of the otrasSenas property.
   *
   * @return possible object is {@link String }
   */
  public String getOtrasSenas() {
    return otrasSenas;
  }

  /**
   * Sets the value of the otrasSenas property.
   *
   * @param value allowed object is {@link String }
   */
  public void setOtrasSenas(String value) {
    this.otrasSenas = value;
  }
}
