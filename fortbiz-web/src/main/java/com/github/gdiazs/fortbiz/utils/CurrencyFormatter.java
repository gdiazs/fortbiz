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
package com.github.gdiazs.fortbiz.utils;

import java.math.BigDecimal;

import javax.inject.Singleton;



@Singleton
public class CurrencyFormatter {

  private static final int ONE_DECIMAL = 1;
  private static final String DECIMAL_MISSING = "0";
  private static final int DO_NOT_EXIST = -1;
  private static final int DECIMAL_SCALE = 2;

  public String twoDecimalFormat(BigDecimal decimalValue) {
    String formattedAmount = "";

    if (decimalValue == null) {
      throw new IllegalArgumentException("decimal value is missing");
    }

    final String amount = decimalValue.toString();
    formattedAmount = amount;

    int dotIndex = formattedAmount.indexOf(".");
    if (dotIndex != DO_NOT_EXIST) {

      String decimalPart = formattedAmount.substring(dotIndex + 1);

      if (decimalPart.length() > DECIMAL_SCALE) { // n +1
        formattedAmount = cutDecimals(amount, dotIndex);

      } else if (decimalPart.length() == ONE_DECIMAL) {
        formattedAmount = formattedAmount + DECIMAL_MISSING;
      }
    }

    return formattedAmount;
  }

  public String twoDecimalFormatString(String value){
    return this.twoDecimalFormat(new BigDecimal(value));
  }

  private String cutDecimals(final String amount, int dotIndex) {
    return amount.substring(0, dotIndex + DECIMAL_SCALE + 1);
  }
}
