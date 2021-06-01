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
package com.github.gdiazs.fortbiz.factories;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.keycloak.AuthorizationContext;
import org.keycloak.KeycloakSecurityContext;

@RequestScoped
public class SecurityContextProducer {

  @Inject private HttpSession session;

  @Produces
  @RequestScoped
  public KeycloakSecurityContext keycloakSecurityContext() {
    final Object securityContextObj =
        this.session.getAttribute(KeycloakSecurityContext.class.getName());
    if (isValidSecurityContext(securityContextObj)) {
      return (KeycloakSecurityContext) securityContextObj;
    }
    return new KeycloakSecurityContext();
  }

  @Produces
  @RequestScoped
  public AuthorizationContext authorizationContext() {
    final var keycloakSecurityContext = this.keycloakSecurityContext();
    if (null != keycloakSecurityContext) {
      return keycloakSecurityContext.getAuthorizationContext();
    }
    return new AuthorizationContext();
  }

  private boolean isValidSecurityContext(Object securityContextObj) {
    return securityContextObj != null && securityContextObj instanceof KeycloakSecurityContext;
  }
}
