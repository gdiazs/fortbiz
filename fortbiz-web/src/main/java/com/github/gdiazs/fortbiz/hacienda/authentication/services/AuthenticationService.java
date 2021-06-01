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
package com.github.gdiazs.fortbiz.hacienda.authentication.services;

import com.github.gdiazs.fortbiz.hacienda.authentication.dto.AuthenticationResponseDto;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import static com.github.gdiazs.fortbiz.hacienda.authentication.AuthenticationConstants.*;

import java.util.logging.Logger;

@Singleton
public class AuthenticationService {

  private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());
  private final String idpUrl;
  private final String clientId;
  private final String logoutUrl;

  @Inject
  public AuthenticationService(
      @ConfigProperty(name = "fortbiz.hacienda.auth") String idpUrl,
      @ConfigProperty(name = "fortbiz.hacienda.clientId") String clientId,
      @ConfigProperty(name = "fortbiz.hacienda.logout") String logoutUrl) {
    this.idpUrl = idpUrl;
    this.clientId = clientId;
    this.logoutUrl = logoutUrl;
  }

  public AuthenticationResponseDto doLogin(String username, String password) {

    LOGGER.info(String.format("Calling doLogin URL: %s", this.idpUrl));
    var client = ClientBuilder.newClient();

    var form = new Form();
    form.param(GRANT_TYPE_PARAM, GRANT_TYPE_VALUE);
    form.param(CLIENT_ID_PARAM, this.clientId);
    form.param(USERNAME, username);
    form.param(PASSWORD, password);

    var credentials = Entity.form(form);

    final var authenticationResponseDto =
        client
            .target(this.idpUrl)
            .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
            .post(credentials)
            .readEntity(AuthenticationResponseDto.class);

    LOGGER.info(String.format("Hacienda response %s", authenticationResponseDto.toString()));
    return authenticationResponseDto;
  }

  public void doLogout(String refreshToken) {
  	
  	LOGGER.info(String.format("Calling doLogout URL: %s", this.logoutUrl));
    var client = ClientBuilder.newClient();

    var form = new Form();
    form.param(CLIENT_ID_PARAM, this.clientId);
    form.param(REFRESH_TOKEN, refreshToken);

    var logout = Entity.form(form);

    int statusCode = client.target(this.logoutUrl).request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(logout).getStatus();
    if(statusCode == Status.NO_CONTENT.getStatusCode()) {
    	LOGGER.info("Logout successfully done");	
    }
    
  }
}
