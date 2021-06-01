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
 * Java class for CodigoType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CodigoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Tipo"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="01"/&gt;
 *               &lt;enumeration value="02"/&gt;
 *               &lt;enumeration value="03"/&gt;
 *               &lt;enumeration value="04"/&gt;
 *               &lt;enumeration value="99"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Codigo"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="20"/&gt;
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
    name = "CodigoType",
    propOrder = {"tipo", "codigo"})
public class CodigoType {

  @XmlElement(name = "Tipo", required = true)
  protected String tipo;

  @XmlElement(name = "Codigo", required = true)
  protected String codigo;

  /**
   * Gets the value of the tipo property.
   *
   * @return possible object is {@link String }
   */
  public String getTipo() {
    return tipo;
  }

  /**
   * Sets the value of the tipo property.
   *
   * @param value allowed object is {@link String }
   */
  public void setTipo(String value) {
    this.tipo = value;
  }

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
}
