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
package com.github.gdiazs.fortbiz.config;

import java.io.InputStream;
import java.nio.file.FileSystemNotFoundException;

import org.eclipse.microprofile.config.ConfigProvider;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade.Request;

public class FortbizKeyCloakConfigResolver implements KeycloakConfigResolver {

  @Override
  public KeycloakDeployment resolve(Request facade) {

    final var config = ConfigProvider.getConfig(this.getClass().getClassLoader());

    var keycloakFileName = config.getOptionalValue("keycloak.config", String.class);
    if (keycloakFileName.isPresent()) {
      InputStream is = this.getClass().getClassLoader().getResourceAsStream(keycloakFileName.get());
      return KeycloakDeploymentBuilder.build(is);
    }
    throw new FileSystemNotFoundException("keycloak json file not found");
  }
}
