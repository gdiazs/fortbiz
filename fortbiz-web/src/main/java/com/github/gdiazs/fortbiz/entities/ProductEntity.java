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
package com.github.gdiazs.fortbiz.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

/** The persistent class for the products database table. */
@Entity
@Table(name = "products")
public class ProductEntity extends UUIDEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private String cabys;

  private String description;

  private BigDecimal price;

  private String tax;

  private String unit;

  public ProductEntity() {}

  public String getCabys() {
    return this.cabys;
  }

  public void setCabys(String cabys) {
    this.cabys = cabys;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return this.price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getTax() {
    return this.tax;
  }

  public void setTax(String tax) {
    this.tax = tax;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cabys == null) ? 0 : cabys.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((price == null) ? 0 : price.hashCode());
    result = prime * result + ((tax == null) ? 0 : tax.hashCode());
    result = prime * result + ((unit == null) ? 0 : unit.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ProductEntity other = (ProductEntity) obj;
    if (cabys == null) {
      if (other.cabys != null) return false;
    } else if (!cabys.equals(other.cabys)) return false;
    if (description == null) {
      if (other.description != null) return false;
    } else if (!description.equals(other.description)) return false;
    if (price == null) {
      if (other.price != null) return false;
    } else if (!price.equals(other.price)) return false;
    if (tax == null) {
      if (other.tax != null) return false;
    } else if (!tax.equals(other.tax)) return false;
    if (unit == null) {
      if (other.unit != null) return false;
    } else if (!unit.equals(other.unit)) return false;
    return true;
  }
}
