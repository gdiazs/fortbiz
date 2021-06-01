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
 * Java class for OtrosCargosType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="OtrosCargosType"&gt;
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
 *         &lt;element name="NumeroIdentidadTercero" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="12"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NombreTercero" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="100"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Detalle"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="160"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Porcentaje" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="6"/&gt;
 *               &lt;fractionDigits value="5"/&gt;
 *               &lt;minInclusive value="0"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MontoCargo" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}DecimalDineroType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "OtrosCargosType",
    propOrder = {
      "tipoDocumento",
      "numeroIdentidadTercero",
      "nombreTercero",
      "detalle",
      "porcentaje",
      "montoCargo"
    })
public class OtrosCargosType {

  @XmlElement(name = "TipoDocumento", required = true)
  protected String tipoDocumento;

  @XmlElement(name = "NumeroIdentidadTercero")
  protected String numeroIdentidadTercero;

  @XmlElement(name = "NombreTercero")
  protected String nombreTercero;

  @XmlElement(name = "Detalle", required = true)
  protected String detalle;

  @XmlElement(name = "Porcentaje")
  protected BigDecimal porcentaje;

  @XmlElement(name = "MontoCargo", required = true)
  protected BigDecimal montoCargo;

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
   * Gets the value of the numeroIdentidadTercero property.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroIdentidadTercero() {
    return numeroIdentidadTercero;
  }

  /**
   * Sets the value of the numeroIdentidadTercero property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroIdentidadTercero(String value) {
    this.numeroIdentidadTercero = value;
  }

  /**
   * Gets the value of the nombreTercero property.
   *
   * @return possible object is {@link String }
   */
  public String getNombreTercero() {
    return nombreTercero;
  }

  /**
   * Sets the value of the nombreTercero property.
   *
   * @param value allowed object is {@link String }
   */
  public void setNombreTercero(String value) {
    this.nombreTercero = value;
  }

  /**
   * Gets the value of the detalle property.
   *
   * @return possible object is {@link String }
   */
  public String getDetalle() {
    return detalle;
  }

  /**
   * Sets the value of the detalle property.
   *
   * @param value allowed object is {@link String }
   */
  public void setDetalle(String value) {
    this.detalle = value;
  }

  /**
   * Gets the value of the porcentaje property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getPorcentaje() {
    return porcentaje;
  }

  /**
   * Sets the value of the porcentaje property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setPorcentaje(BigDecimal value) {
    this.porcentaje = value;
  }

  /**
   * Gets the value of the montoCargo property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontoCargo() {
    return montoCargo;
  }

  /**
   * Sets the value of the montoCargo property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontoCargo(BigDecimal value) {
    this.montoCargo = value;
  }
}
