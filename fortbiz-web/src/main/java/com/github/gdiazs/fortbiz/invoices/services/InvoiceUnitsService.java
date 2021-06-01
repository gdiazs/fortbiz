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
package com.github.gdiazs.fortbiz.invoices.services;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class InvoiceUnitsService {

  @Inject
  @ConfigProperty(name = "fortbiz.invoice.units", defaultValue = "")
  private String invoiceUnits;

  public Map<String, String> getAvailableUnits() {
    final Map<String, String> units = new HashMap<>();

    final String[] splitUnits = this.invoiceUnits.split(",");

    for (String splitUnit : splitUnits) {
      final var theUnit = splitUnit.split("=");

      if (theUnit.length == 2) {
        units.put(theUnit[0], theUnit[1]);
      } else {
        units.put(splitUnit, splitUnit);
      }
    }

    return units;
  }
}
