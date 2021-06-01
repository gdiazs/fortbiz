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
package com.github.gdiazs.fortbiz.products.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceItemEntity;
import com.github.gdiazs.fortbiz.entities.ProductEntity;
import com.github.gdiazs.fortbiz.invoices.repositories.InvoiceRepository;
import com.github.gdiazs.fortbiz.products.converters.ProductConverter;
import com.github.gdiazs.fortbiz.products.dto.ProductDto;
import com.github.gdiazs.fortbiz.products.repository.InvoiceItemRepository;
import com.github.gdiazs.fortbiz.products.repository.ProductRepository;
import com.github.gdiazs.fortbiz.utils.CurrencyFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductsServiceTest {

  private static final String CABYS = "2131300009900";
  private static final String DESCRIPTION = "Papas congeladas n.c.p.";
  private static final String INVOICE_ID = "57a8e197-f8e1-4739-9ccd-a5fe7b50ec1d";
  private static final String MEASURE = "kg";
  private static final String PRICE = "765.8741628436";
  private static final String PRODUCT_ID = "dcc12a53-0139-4b26-84aa-3c3fdc6f09d4";
  private static final String QUANTITY = "2";
  private static final String TAX = "13%";
  private static final String USER_ID = "44943f49-c5af-4d8e-8d36-af819cfc9b89";

  @Spy private ProductConverter productConverter = new ProductConverter(new CurrencyFormatter());

  @Mock private InvoiceRepository invoiceRepository;

  @Mock private ProductRepository productRepository;

  @Mock private InvoiceItemRepository invoiceItemRepository;

  @InjectMocks private ProductsService productsService;

  private ProductDto productDto = buildProductDto();

  @Test
  public void addNewProductTest_verifyUIShowsTwoDecimals_happyPath() {

    InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity.setId(INVOICE_ID);

    ProductEntity savedProductEntity = new ProductEntity();
    savedProductEntity.setCabys(CABYS);
    savedProductEntity.setDescription(DESCRIPTION);
    savedProductEntity.setId(PRODUCT_ID);
    savedProductEntity.setPrice(new BigDecimal(PRICE).setScale(5, RoundingMode.UP));

    // Behavior
    Mockito.when(this.invoiceRepository.findInvoiceByNumber(USER_ID, INVOICE_ID))
        .thenReturn(invoiceEntity);

    Mockito.when(this.productRepository.saveAndFlush(Mockito.any(ProductEntity.class)))
        .thenReturn(savedProductEntity);

    Mockito.when(this.invoiceItemRepository.saveAndFlush(Mockito.any(InvoiceItemEntity.class)))
        .thenReturn(new InvoiceItemEntity());

    // Execution
    ProductDto actualProductDto = this.productsService.addNewProduct(USER_ID, productDto);

    String actualUIPrice = actualProductDto.getPrice();

    // Verify
    String expectedPrice = "765.87";
    Assertions.assertEquals(expectedPrice, actualUIPrice);
  }

  @Test
  public void addNewProductTest_verifyPersistedCurrencyHasHaciendaFormat_happyPath() {
	  
	String expectedProductPrice = "765.87416";

    InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity.setId(INVOICE_ID);

    final ProductEntity productEntity = buildProductEntity();
    productEntity.setPrice(new BigDecimal(expectedProductPrice));
    

    // Behavior
    Mockito.when(this.invoiceRepository.findInvoiceByNumber(USER_ID, INVOICE_ID))
        .thenReturn(invoiceEntity);

    Mockito.when(this.productRepository.saveAndFlush(productEntity))
        .thenReturn(productEntity);

    Mockito.when(this.invoiceItemRepository.saveAndFlush(Mockito.any(InvoiceItemEntity.class)))
        .thenReturn(new InvoiceItemEntity());

    // Execution
    ProductDto actualProductDto = this.productsService.addNewProduct(USER_ID, productDto);

    String actualUIPrice = actualProductDto.getPrice();

    // Verify
    String expectedPrice = "765.87";
    Assertions.assertEquals(expectedPrice, actualUIPrice);
  }
  
  @Test
  public void addNewProductTest_verifyPersistedCurrencyHasHaciendaFormat_roundingPriceUp() {
	String proviedUserPrice = "765.5541678436";
	String expectedProductPrice = "765.55417";

    InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity.setId(INVOICE_ID);

    final ProductEntity productEntity = buildProductEntity();
    productEntity.setPrice(new BigDecimal(expectedProductPrice));
    

    // Behavior
    Mockito.when(this.invoiceRepository.findInvoiceByNumber(USER_ID, INVOICE_ID))
        .thenReturn(invoiceEntity);

    Mockito.when(this.productRepository.saveAndFlush(productEntity))
        .thenReturn(productEntity);

    Mockito.when(this.invoiceItemRepository.saveAndFlush(Mockito.any(InvoiceItemEntity.class)))
        .thenReturn(new InvoiceItemEntity());

    // Execution
    this.productDto.setPrice(proviedUserPrice);
    
    ProductDto actualProductDto = this.productsService.addNewProduct(USER_ID, productDto);

    String actualUIPrice = actualProductDto.getPrice();

    // Verify
    String expectedPrice = "765.55";
    Assertions.assertEquals(expectedPrice, actualUIPrice);
  }
  
  @Test
  public void addNewProductTest_verifyPersistedCurrencyHasHaciendaFormat_roundingPriceUpUsing5() {
	String proviedUserPrice = "765.9341658436";
	String expectedProductPrice = "765.93417";

    InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity.setId(INVOICE_ID);

    final ProductEntity productEntity = buildProductEntity();
    productEntity.setPrice(new BigDecimal(expectedProductPrice));
    

    // Behavior
    Mockito.when(this.invoiceRepository.findInvoiceByNumber(USER_ID, INVOICE_ID))
        .thenReturn(invoiceEntity);

    Mockito.when(this.productRepository.saveAndFlush(productEntity))
        .thenReturn(productEntity);

    Mockito.when(this.invoiceItemRepository.saveAndFlush(Mockito.any(InvoiceItemEntity.class)))
        .thenReturn(new InvoiceItemEntity());

    // Execution
    this.productDto.setPrice(proviedUserPrice);
    
    ProductDto actualProductDto = this.productsService.addNewProduct(USER_ID, productDto);

    String actualUIPrice = actualProductDto.getPrice();

    // Verify
    String expectedPrice = "765.93";
    Assertions.assertEquals(expectedPrice, actualUIPrice);
  }
  
  @Test
  public void addNewProductTest_verifyPersistedCurrencyHasHaciendaFormat_threeDecimals() {
	String proviedUserPrice = "765.456";
	String expectedProductPrice = "765.45600";

    InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity.setId(INVOICE_ID);

    final ProductEntity productEntity = buildProductEntity();
    productEntity.setPrice(new BigDecimal(expectedProductPrice));
    

    // Behavior
    Mockito.when(this.invoiceRepository.findInvoiceByNumber(USER_ID, INVOICE_ID))
        .thenReturn(invoiceEntity);

    Mockito.when(this.productRepository.saveAndFlush(productEntity))
        .thenReturn(productEntity);

    Mockito.when(this.invoiceItemRepository.saveAndFlush(Mockito.any(InvoiceItemEntity.class)))
        .thenReturn(new InvoiceItemEntity());

    // Execution
    this.productDto.setPrice(proviedUserPrice);
    
    ProductDto actualProductDto = this.productsService.addNewProduct(USER_ID, productDto);

    String actualUIPrice = actualProductDto.getPrice();

    // Verify
    String expectedPrice = "765.45";
    Assertions.assertEquals(expectedPrice, actualUIPrice);
  }

  private ProductEntity buildProductEntity() {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setCabys(CABYS);
    productEntity.setDescription(DESCRIPTION);
    productEntity.setPrice(new BigDecimal(PRICE));
    productEntity.setTax(TAX);
    productEntity.setUnit(MEASURE);
    return productEntity;
  }

  private ProductDto buildProductDto() {
    return ProductDto.builder()
        .price(PRICE)
        .productId(PRODUCT_ID)
        .cabys(CABYS)
        .description(DESCRIPTION)
        .measure(MEASURE)
        .tax(TAX)
        .quantity(QUANTITY)
        .invoiceId(INVOICE_ID)
        .build();
  }
}
