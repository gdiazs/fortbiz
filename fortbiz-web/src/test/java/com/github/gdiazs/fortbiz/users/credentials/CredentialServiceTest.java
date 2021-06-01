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
package com.github.gdiazs.fortbiz.users.credentials;

import com.github.gdiazs.fortbiz.credentials.dto.CredentialDto;
import com.github.gdiazs.fortbiz.hacienda.authentication.dto.AuthenticationResponseDto;
import com.github.gdiazs.fortbiz.hacienda.authentication.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTest {

  private final AuthenticationService authenticationService =
      Mockito.mock(AuthenticationService.class);

  @Test
  void addCredential() {
    CredentialDto.builder().id("1").password("12344").username("memo").build();

    var authenticationResponseDto = new AuthenticationResponseDto();
    authenticationResponseDto.setAccessToken("3241243");
    authenticationResponseDto.setRefreshToken("3o47123894172");

    // Expects

    Mockito.when(authenticationService.doLogin(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(authenticationResponseDto);

    // credentialService.addCredential(newCredential);

    // Asserts

  }
}
