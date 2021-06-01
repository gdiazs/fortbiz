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
package com.github.gdiazs.fortbiz.products.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.entities.ProductEntity;
import com.github.gdiazs.fortbiz.products.dto.ProductDto;
import com.github.gdiazs.fortbiz.utils.CurrencyFormatter;

@Singleton
public class ProductConverter implements ModelConverter<ProductDto, ProductEntity> {

  private CurrencyFormatter currencyFormatter;

  @Inject
  public ProductConverter(CurrencyFormatter currencyFormatter) {
    this.currencyFormatter = currencyFormatter;
  }



  @Override
  public ProductEntity convertTo(ProductDto source) {
    final ProductEntity productEntity = new ProductEntity();
    if (null != source.getProductId()) {
      productEntity.setId(source.getProductId());
    }
    productEntity.setCabys(source.getCabys());
    productEntity.setUnit(source.getMeasure());
    productEntity.setTax(source.getTax());
    productEntity.setPrice(new BigDecimal(source.getPrice()).setScale(5, RoundingMode.HALF_DOWN));
    productEntity.setDescription(source.getDescription());
    return productEntity;
  }

  @Override
  public ProductDto convertFrom(ProductEntity target) {
    return ProductDto.builder()
        .cabys(target.getCabys())
        .description(target.getDescription())
        .measure(target.getUnit())
        .tax(target.getTax())
        .price(this.currencyFormatter.twoDecimalFormat(target.getPrice()))
        .build();
  }
}
