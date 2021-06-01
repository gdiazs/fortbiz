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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceItemEntity;
import com.github.gdiazs.fortbiz.entities.ProductEntity;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDetailsDto;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceItemDto;
import com.github.gdiazs.fortbiz.products.repository.InvoiceItemRepository;
import com.github.gdiazs.fortbiz.products.repository.ProductRepository;

@Singleton
public class InvoiceDetailsConverter implements ModelConverter<InvoiceEntity, InvoiceDetailsDto> {

  private static final int DECIMAL_SCALE = 5;
  private final ProductRepository productRepository;
  private final InvoiceItemRepository invoiceItemRepository;

  @Inject
  public InvoiceDetailsConverter(
      ProductRepository productRepository, InvoiceItemRepository invoiceItemRepository) {
    this.productRepository = productRepository;
    this.invoiceItemRepository = invoiceItemRepository;
  }

  @Override
  public InvoiceDetailsDto convertTo(InvoiceEntity source) {

    final List<ProductEntity> invoiceProducts =
        this.productRepository.findProductsByInvoiceId(source.getId());

    final List<InvoiceItemEntity> invoiceProductsQuantities =
        this.invoiceItemRepository.findItemsByInvoiceId(source.getId());

    final List<InvoiceItemDto> items = new ArrayList<>();

    invoiceProducts.forEach(
        (product) -> {
          final var quantity =
              this.findQuantityByProductId(product.getId(), invoiceProductsQuantities);

          final InvoiceItemDto newInvoiceItemDto =
              InvoiceItemDto.builder()
                  .description(product.getDescription())
                  .price(product.getPrice().toString())
                  .quantity(quantity.toString())
                  .subtotal(quantity.multiply(product.getPrice()).toString())
                  .tax(product.getTax())
                  .taxSubtotal(calculateTaxSubtotal(product).toString())
                  .productId(product.getId())
                  .cabys(product.getCabys())
                  .unit(product.getUnit())
                  .build();
          newInvoiceItemDto.setTotalLine(calculateTotalLine(newInvoiceItemDto.getSubtotal(), newInvoiceItemDto.getTaxSubtotal()).toString());
          items.add(newInvoiceItemDto);
        });

    final BigDecimal subtotal = calculateAllProductsSubTotal(items);
    final BigDecimal taxesTotal = calculateTaxesForProduct(items);

    final InvoiceDetailsDto builtInvoiceDetailsDto =
        InvoiceDetailsDto.builder()
            .items(formatEachItem(items))
            .subtotal(subtotal.setScale(DECIMAL_SCALE, RoundingMode.HALF_DOWN).toString())
            .taxesTotal(taxesTotal.setScale(DECIMAL_SCALE, RoundingMode.HALF_DOWN).toString())
            .total(
                subtotal.add(taxesTotal).setScale(DECIMAL_SCALE, RoundingMode.HALF_DOWN).toString())
            .build();

    return builtInvoiceDetailsDto;
  }

  private BigDecimal calculateTotalLine(String subTotalItem, String taxSubTotalItem) {
    BigDecimal taxSubTotal = new BigDecimal(taxSubTotalItem);
    BigDecimal subTotal = new BigDecimal(subTotalItem);

    return subTotal.add(taxSubTotal).setScale(DECIMAL_SCALE, RoundingMode.HALF_DOWN);
  }

  private BigDecimal calculateTaxSubtotal(ProductEntity product) {

    String tax = product.getTax().replace("%", "");
    BigDecimal taxPercent = new BigDecimal(tax);
    BigDecimal price = new BigDecimal(product.getPrice().toString());

    return price.multiply(taxPercent).divide(new BigDecimal(100));
  }

  private List<InvoiceItemDto> formatEachItem(List<InvoiceItemDto> items) {
    return items
        .stream()
        .map(
            (item) -> {
              item.setSubtotal(
                  new BigDecimal(item.getSubtotal())
                      .setScale(DECIMAL_SCALE, RoundingMode.HALF_DOWN)
                      .toString());
              return item;
            })
        .collect(Collectors.toList());
  }

  private BigDecimal calculateAllProductsSubTotal(List<InvoiceItemDto> items) {

    BigDecimal total = new BigDecimal(0);
    for (InvoiceItemDto invoiceItemDto : items) {
      total = total.add(new BigDecimal(invoiceItemDto.getSubtotal()));
    }

    return total;
  }

  private BigDecimal findQuantityByProductId(String productId, List<InvoiceItemEntity> items) {
    var foundItemWithQuantity =
        items.stream().filter((item) -> productId.equals(item.getId().getProductId())).findFirst();

    return foundItemWithQuantity.isEmpty()
        ? BigDecimal.ZERO
        : foundItemWithQuantity.get().getQuantity();
  }

  private BigDecimal calculateTaxesForProduct(List<InvoiceItemDto> products) {
    BigDecimal total = new BigDecimal(0);

    for (InvoiceItemDto product : products) {
      String tax = product.getTax().replace("%", "");
      BigDecimal taxPercent = new BigDecimal(tax);

      BigDecimal subTotal = new BigDecimal(product.getSubtotal());

      BigDecimal divideSubtotal = subTotal.divide(new BigDecimal(100));
      BigDecimal taxAmount = divideSubtotal.multiply(taxPercent);

      total = total.add(taxAmount);
    }

    return total;
  }

  @Override
  public InvoiceEntity convertFrom(InvoiceDetailsDto target) {
    // TODO Auto-generated method stub
    return null;
  }
}
