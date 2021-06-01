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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/** The primary key class for the invoice_items database table. */
@Embeddable
public class InvoiceItemEntityPK implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "invoice_id")
  private String invoiceId;

  @Column(name = "product_id")
  private String productId;

  public InvoiceItemEntityPK() {}

  public String getInvoiceId() {
    return this.invoiceId;
  }

  public void setInvoiceId(String invoiceId) {
    this.invoiceId = invoiceId;
  }

  public String getProductId() {
    return this.productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof InvoiceItemEntityPK)) {
      return false;
    }
    InvoiceItemEntityPK castOther = (InvoiceItemEntityPK) other;
    return this.invoiceId.equals(castOther.invoiceId) && this.productId.equals(castOther.productId);
  }

  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.invoiceId.hashCode();
    hash = hash * prime + this.productId.hashCode();

    return hash;
  }
}
