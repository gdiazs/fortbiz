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
package com.github.gdiazs.fortbiz.invoices.converters;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceItemEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceItemEntityPK;
import com.github.gdiazs.fortbiz.entities.ProductEntity;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDetailsDto;
import com.github.gdiazs.fortbiz.products.repository.InvoiceItemRepository;
import com.github.gdiazs.fortbiz.products.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InvoiceDeatilsConverterTest {

  private static final String PRODUCT_ID = "dcc12a53-0139-4b26-84aa-3c3fdc6f09d4";
  private static final String INVOICE_ID = "4115bc64-b88d-4bea-a853-121b7fe8d5c0";
  private static final String INVOICE_NUMBER = "00100001010000000001";
  private static final String BRANCH_ID = "6da9bad5-a540-4a6a-828a-76a3ac68c277";
  private static final String CURRENCY = "CRC";
  private static final String STATUS = "1";
  private static final String PAYMENT_METHOD = "01";
  private static final String CABYS = "2131300009900";
  private static final String DESCRIPTION = "Papas congeladas n.c.p.";
  private static final String MEASURE = "kg";
  private static final String PRICE = "765.87416";
  private static final String TAX = "13%";

  @InjectMocks InvoiceDetailsConverter invoiceDetailsConverter;

  @Mock private ProductRepository productRepository;

  @Mock private InvoiceItemRepository invoiceItemRepository;

  @Test
  public void invoiceConverterTest_veritfyCalculateInvoiceDeatil_happyPath() {

    ProductEntity productEntity = new ProductEntity();
    productEntity = buildProductEntity();
    ProductEntity productEntityTwo = new ProductEntity();
    productEntityTwo = buildProductEntityTwo();

    InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity = buildInvoiceEntity();

    InvoiceItemEntityPK invoiceItemEntityPK = new InvoiceItemEntityPK();
    invoiceItemEntityPK.setProductId(PRODUCT_ID);
    InvoiceItemEntityPK invoiceItemEntityPKTwo = new InvoiceItemEntityPK();
    invoiceItemEntityPKTwo.setProductId("fe22dff2-f6c5-4987-baa3-3404a6913930");

    InvoiceItemEntity invoiceItemEntity = new InvoiceItemEntity();
    invoiceItemEntity.setQuantity(new BigDecimal("1.36"));
    invoiceItemEntity.setId(invoiceItemEntityPK);
    InvoiceItemEntity invoiceItemEntityTwo = new InvoiceItemEntity();
    invoiceItemEntityTwo.setQuantity(new BigDecimal("1.25"));
    invoiceItemEntityTwo.setId(invoiceItemEntityPKTwo);

    List<InvoiceItemEntity> invoiceItemList = new ArrayList<InvoiceItemEntity>();
    invoiceItemList.add(invoiceItemEntity);
    invoiceItemList.add(invoiceItemEntityTwo);

    List<ProductEntity> invoiceProductList = new ArrayList<ProductEntity>();
    invoiceProductList.add(productEntity);
    invoiceProductList.add(productEntityTwo);

    // Behavior
    Mockito.when(this.productRepository.findProductsByInvoiceId(INVOICE_ID))
        .thenReturn(invoiceProductList);

    Mockito.when(this.invoiceItemRepository.findItemsByInvoiceId(INVOICE_ID))
        .thenReturn(invoiceItemList);

    // Execution
    InvoiceDetailsDto actualInvoiceDeatilsDto =
        this.invoiceDetailsConverter.convertTo(invoiceEntity);

    // Verify

    String expectedSubtotal = "63985.99318";
    String expectedTaxesTotal = "8318.17911";
    String expectedTotal = "72304.17230";

    Assertions.assertEquals(expectedSubtotal, actualInvoiceDeatilsDto.getSubtotal());
    Assertions.assertEquals(expectedTaxesTotal, actualInvoiceDeatilsDto.getTaxesTotal());
    Assertions.assertEquals(expectedTotal, actualInvoiceDeatilsDto.getTotal());
  }

  private ProductEntity buildProductEntity() {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setId(PRODUCT_ID);
    productEntity.setCabys(CABYS);
    productEntity.setDescription(DESCRIPTION);
    productEntity.setPrice(new BigDecimal(PRICE));
    productEntity.setTax(TAX);
    productEntity.setUnit(MEASURE);
    return productEntity;
  }

  private ProductEntity buildProductEntityTwo() {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setId("fe22dff2-f6c5-4987-baa3-3404a6913930");
    productEntity.setCabys("002152211545");
    productEntity.setDescription("Guitarra");
    productEntity.setPrice(new BigDecimal("50355.52346"));
    productEntity.setTax("13%");
    productEntity.setUnit("unit");
    return productEntity;
  }

  private InvoiceEntity buildInvoiceEntity() {
    InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity.setBranchId(BRANCH_ID);
    invoiceEntity.setCurrency(CURRENCY);
    invoiceEntity.setId(INVOICE_ID);
    invoiceEntity.setInvoiceNumber(INVOICE_NUMBER);
    invoiceEntity.setPaymentMethod(PAYMENT_METHOD);
    invoiceEntity.setStatus(STATUS);

    return invoiceEntity;
  }
}
