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
package com.github.gdiazs.fortbiz.localization.services;

import com.github.gdiazs.fortbiz.localization.dto.LocalizationDto;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class LocalizationService {

  private String locationURL;

  @Inject
  public LocalizationService(
      @ConfigProperty(name = "fortbiz.locations.cr.endpoint") String locationURL) {
    this.locationURL = locationURL;
  }

  public List<LocalizationDto> getCostarricanProvinces() {

    return Arrays.asList(
        new LocalizationDto("null", "Seleccione una opción"),
        new LocalizationDto("1", "San José"),
        new LocalizationDto("2", "Alajuela"),
        new LocalizationDto("3", "Cartago"),
        new LocalizationDto("4", "Heredia"),
        new LocalizationDto("5", "Guanacaste"),
        new LocalizationDto("6", "Puntarenas"),
        new LocalizationDto("7", "Limón"));
  }

  public List<LocalizationDto> getCostarricanCantons(String provinceId) {
    Client client = getClient();
    Map<String, String> cantons =
        client
            .target(locationURL + "/provincia/" + provinceId + "/cantones.json")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get(new GenericType<HashMap<String, String>>() {});

    return cantons.entrySet().stream()
        .map((entry) -> new LocalizationDto(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  public List<LocalizationDto> getCostarricanDistrict(String provinceId, String cantonId) {
    Client client = getClient();
    Map<String, String> district =
        client
            .target(
                locationURL
                    + "/provincia/"
                    + provinceId
                    + "/canton/"
                    + cantonId
                    + "/distritos.json")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get(new GenericType<HashMap<String, String>>() {});

    return district.entrySet().stream()
        .map((entry) -> new LocalizationDto(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  protected Client getClient() {
    return ClientBuilder.newClient();
  }
}
