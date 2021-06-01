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

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for TelefonoType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TelefonoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodigoPais"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NumTelefono"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;totalDigits value="20"/&gt;
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
    name = "TelefonoType",
    propOrder = {"codigoPais", "numTelefono"})
public class TelefonoType {

  @XmlElement(name = "CodigoPais", required = true)
  protected BigInteger codigoPais;

  @XmlElement(name = "NumTelefono", required = true)
  protected BigInteger numTelefono;

  /**
   * Gets the value of the codigoPais property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getCodigoPais() {
    return codigoPais;
  }

  /**
   * Sets the value of the codigoPais property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setCodigoPais(BigInteger value) {
    this.codigoPais = value;
  }

  /**
   * Gets the value of the numTelefono property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getNumTelefono() {
    return numTelefono;
  }

  /**
   * Sets the value of the numTelefono property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setNumTelefono(BigInteger value) {
    this.numTelefono = value;
  }
}
