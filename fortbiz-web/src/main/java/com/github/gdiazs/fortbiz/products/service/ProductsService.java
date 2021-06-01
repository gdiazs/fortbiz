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

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.products.dto.ProductDto;
import com.github.gdiazs.fortbiz.products.repository.InvoiceItemRepository;
import com.github.gdiazs.fortbiz.products.repository.ProductRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceItemEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceItemEntityPK;
import com.github.gdiazs.fortbiz.entities.ProductEntity;
import com.github.gdiazs.fortbiz.invoices.repositories.InvoiceRepository;

@Singleton
public class ProductsService {

  private final ProductRepository productRepository;
  private final InvoiceItemRepository invoiceItemRepository;
  private final InvoiceRepository invoiceRepository;
  private final ModelConverter<ProductDto, ProductEntity> productConverter;

  @Inject
  public ProductsService(
      ProductRepository productRepository,
      InvoiceItemRepository invoiceItemRepository,
      ModelConverter<ProductDto, ProductEntity> productConverter,
      InvoiceRepository invoiceRepository) {
    this.productRepository = productRepository;
    this.invoiceItemRepository = invoiceItemRepository;
    this.invoiceRepository = invoiceRepository;
    this.productConverter = productConverter;
  }

  @Transactional
  public ProductDto addNewProduct(String userId, ProductDto productDto) {
    final InvoiceEntity foundInvoiceEntity =
        this.invoiceRepository.findInvoiceByNumber(userId, productDto.getInvoiceId());

    ProductEntity productEntity = productConverter.convertTo(productDto);
    productEntity = this.productRepository.saveAndFlush(productEntity);

    final InvoiceItemEntityPK invoiceItemPk = new InvoiceItemEntityPK();
    invoiceItemPk.setInvoiceId(foundInvoiceEntity.getId());
    invoiceItemPk.setProductId(productEntity.getId());

    final InvoiceItemEntity invoiceItemEntity = new InvoiceItemEntity();
    invoiceItemEntity.setQuantity(new BigDecimal(productDto.getQuantity()));
    invoiceItemEntity.setId(invoiceItemPk);

    this.invoiceItemRepository.saveAndFlush(invoiceItemEntity);
    return this.productConverter.convertFrom(productEntity);
  }

  @Transactional
  public ProductDto updateProduct(String userId, ProductDto productDto) {
    final InvoiceEntity foundInvoiceEntity =
        this.invoiceRepository.findInvoiceByNumber(userId, productDto.getInvoiceId());

    ProductEntity foundProduct = this.productRepository.findBy(productDto.getProductId());
    foundProduct.setCabys(productDto.getCabys());
    foundProduct.setDescription(productDto.getDescription());
    foundProduct.setPrice(
        new BigDecimal(productDto.getPrice()).setScale(5, RoundingMode.HALF_DOWN));
    foundProduct.setTax(productDto.getTax());
    foundProduct.setUnit(productDto.getMeasure());

    foundProduct = this.productRepository.save(foundProduct);

    InvoiceItemEntity invoiceItemEntity =
        this.invoiceItemRepository.findInvoiceItem(
            foundInvoiceEntity.getId(), foundProduct.getId(), userId);
    invoiceItemEntity.setQuantity(new BigDecimal(productDto.getQuantity()));
    this.invoiceItemRepository.save(invoiceItemEntity);

    return this.productConverter.convertFrom(foundProduct);
  }
}
