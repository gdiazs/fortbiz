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
package com.github.gdiazs.fortbiz.hacienda.activities.services;

import com.github.gdiazs.fortbiz.commons.inject.Service;
import com.github.gdiazs.fortbiz.hacienda.activities.dto.HaciendaCustomerInformationDto;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@Service
public class HaciendaActivitiesService {

  private final String economicActivityUrl;

  @Inject
  public HaciendaActivitiesService(
      @ConfigProperty(name = "fortbiz.hacienda.ae") final String economicActivityUrl) {

    this.economicActivityUrl = economicActivityUrl;
  }

  public HaciendaCustomerInformationDto findCustomerInformationById(String id) {
    final Client restClient = getClient();

    final StringBuilder requestPath = new StringBuilder(this.economicActivityUrl);
    requestPath.append("?").append(String.format("identificacion=%s", id));

    final HaciendaCustomerInformationDto result =
        restClient
            .target(requestPath.toString())
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get(HaciendaCustomerInformationDto.class);

    return result;
  }

  protected Client getClient() {
    return ClientBuilder.newClient();
  }
}
