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
package com.github.gdiazs.fortbiz.credentials.services;

import com.github.gdiazs.fortbiz.credentials.dto.CredentialValidationDto;
import com.github.gdiazs.fortbiz.branches.services.BranchService;
import com.github.gdiazs.fortbiz.credentials.dto.CredentialDto;
import com.github.gdiazs.fortbiz.credentials.exceptions.MinisterioHaciendaAuthException;
import com.github.gdiazs.fortbiz.credentials.repository.CredentialRepository;
import com.github.gdiazs.fortbiz.entities.CredentialEntity;
import com.github.gdiazs.fortbiz.entities.UserCredentialEntity;
import com.github.gdiazs.fortbiz.entities.UserCredentialPk;
import com.github.gdiazs.fortbiz.hacienda.authentication.services.AuthenticationService;
import com.github.gdiazs.fortbiz.users.repositories.UserCredentialRepository;
import com.github.gdiazs.fortbiz.utils.StringFormatter;
import com.github.gdiazs.fortbiz.web.resources.credentials.DeleteCredentialException;

import java.io.ByteArrayInputStream;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public class CredentialService {

  private static Logger LOGGER = Logger.getLogger(CredentialService.class.getName());

  private final AuthenticationService authenticationService;
  private final StringFormatter stringFormatter;
  private final BranchService branchService;
  private final CredentialRepository credentialRepository;
  private final UserCredentialRepository userCredentialRepository;

  @Inject
  CredentialService(
      AuthenticationService authenticationService,
      StringFormatter stringFormatter,
      BranchService branchService,
      CredentialRepository credentialRepository,
      UserCredentialRepository userCredentialRepository) {
    this.authenticationService = authenticationService;
    this.stringFormatter = stringFormatter;
    this.branchService = branchService;
    this.credentialRepository = credentialRepository;
    this.userCredentialRepository = userCredentialRepository;
  }

  @Transactional
  public CredentialDto addCredential(CredentialDto credentialDto, String userId) {

    CredentialEntity credentialEntity = new CredentialEntity();
    final var authenticationResponseDto =
        authenticationService.doLogin(credentialDto.getUsername(), credentialDto.getPassword());

    if (authenticationResponseDto.getAccessToken() != null) {

      boolean isKeyStoreOpened =
          this.tryToOpenKeyStore(credentialDto.getKeystore().toByteArray(), credentialDto.getPin());
      try {

        if (isKeyStoreOpened) {
          credentialEntity = convertToEntity(credentialDto, credentialEntity);
          credentialRepository.saveAndFlush(credentialEntity);
          assignCredentialToUser(credentialEntity, userId);
        }
        authenticationService.doLogout(authenticationResponseDto.getRefreshToken());

      } catch (IOException | PersistenceException | MinisterioHaciendaAuthException ex) {
        LOGGER.log(Level.SEVERE, "Error persisting user credentials", ex);
        throw new MinisterioHaciendaAuthException("Unexpected error persisting credentials", ex);
      }
      return convertToDto(credentialEntity);
    } else {
      throw new MinisterioHaciendaAuthException("It was not possible to get access to Hacienda");
    }
  }

  /**
   * This method will use the pin and open the Hacienda Keystore in order to check if data provided
   * by user is right.
   */
  public boolean tryToOpenKeyStore(byte[] keystoreBytes, String keyStorePin) {
    String alias = null;
    try {

      KeyStore ks = KeyStore.getInstance("PKCS12");
      ks.load(new ByteArrayInputStream(keystoreBytes), keyStorePin.toCharArray());
      var aliases = ks.aliases();
      while (aliases.hasMoreElements()) {
        alias = aliases.nextElement();
      }
    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
      LOGGER.log(Level.SEVERE, "It was not possible to open the user keystore", e);
      throw new MinisterioHaciendaAuthException("Error trying to open user keystore", e);
    }
    return null != alias;
  }

  @Transactional
  public void deleteCredentialById(String userId, String credentialId) {
    try {
      branchService.unLinkCredential(credentialId, userId);
      this.userCredentialRepository.removeByUserIdAndBranchIdAndFlush(userId, credentialId);
    } catch (NoResultException noResultException) {
      LOGGER.log(Level.INFO, "");
    } catch (Exception e) {
      throw new DeleteCredentialException("Credential has invoices", e.getCause());
    }

    this.credentialRepository.remove(this.credentialRepository.findBy(credentialId));
  }

  @Transactional
  public CredentialDto updateCredential(CredentialDto credentialDto) throws IOException {

    CredentialEntity credentialEntity = this.credentialRepository.findBy(credentialDto.getId());
    final var authenticationResponseDto =
        authenticationService.doLogin(credentialDto.getUsername(), credentialDto.getPassword());
    if (authenticationResponseDto.getAccessToken() != null) {
      try {
        if (credentialDto.getKeystore() != null) {
          this.tryToOpenKeyStore(credentialDto.getKeystore().toByteArray(), credentialDto.getPin());
        }

        credentialEntity = convertToEntity(credentialDto, credentialEntity);
        this.credentialRepository.save(credentialEntity);

        authenticationService.doLogout(authenticationResponseDto.getRefreshToken());
      } catch (IOException | PersistenceException | MinisterioHaciendaAuthException ex) {
        LOGGER.log(Level.SEVERE, "Error persisting user credentials", ex);
        throw new MinisterioHaciendaAuthException("Unexpected error persisting credentials", ex);
      }
      return convertToDto(credentialEntity);
    } else {
      throw new MinisterioHaciendaAuthException("It was not possible to get access to Hacienda");
    }
  }

  public List<CredentialDto> findCredentialsByUserId(String userId) {

    final List<CredentialEntity> credentials =
        this.credentialRepository.findCredentialsByUserId(userId);

    return credentials
        .stream()
        .map(
            (credentialEntity) -> {
              final var credentialDto = convertToDto(credentialEntity);
              credentialDto.setPassword("");
              return credentialDto;
            })
        .collect(Collectors.toList());
  }

  @Transactional
  public void assignCredentialToUser(CredentialEntity credential, String userId) {

    var userCredentials = new UserCredentialEntity();
    var userCredentialsPk = new UserCredentialPk();
    userCredentialsPk.setUserId(userId);
    userCredentialsPk.setCredentialId(credential.getId());
    userCredentials.setUserCredentialPk(userCredentialsPk);

    this.userCredentialRepository.save(userCredentials);
  }

  private CredentialDto convertToDto(CredentialEntity credentialEntity) {
    final String[] localizations = credentialEntity.getLocalization().split("/");
    return CredentialDto.builder()
        .id(credentialEntity.getId())
        .username(credentialEntity.getUsername())
        .identificationNumber(credentialEntity.getIndentificationNumber())
        .identificationType(credentialEntity.getIndentificationType())
        .tradeName(credentialEntity.getTradeName())
        .economicActivity(credentialEntity.getEconomicActivity())
        .activityCode(credentialEntity.getActivityCode())
        .localization(credentialEntity.getLocalization())
        .province(localizations[0])
        .canton(localizations[1])
        .district(localizations[2])
        .brandImage(defineBrandImage(credentialEntity.getBrandImage()))
        .createdAt(stringFormatter.formatToCrDates(credentialEntity.getCreatedAt()))
        .updatedAt(stringFormatter.formatToCrDates(credentialEntity.getUpdatedAt()))
        .build();
  }

  private CredentialEntity convertToEntity(
      CredentialDto credentialDto, CredentialEntity credentialEntity) throws IOException {

    credentialEntity.setIndentificationNumber(credentialDto.getIdentificationNumber());
    credentialEntity.setUsername(credentialDto.getUsername());
    credentialEntity.setPassword(credentialDto.getPassword());
    credentialEntity.setPin(credentialDto.getPin());
    credentialEntity.setTradeName(credentialDto.getTradeName());
    credentialEntity.setIndentificationType(credentialDto.getIdentificationType());
    credentialEntity.setActivityCode(credentialDto.getActivityCode());
    credentialEntity.setLocalization(credentialDto.getLocalization());
    if (!credentialDto.getBrandImage().isBlank()) {
      credentialEntity.setBrandImage(
          Base64.getEncoder().encode(credentialDto.getBrandImage().getBytes()));
    }

    if (credentialDto.getKeystore() != null) {
      credentialEntity.setKeystoreBytes(credentialDto.getKeystore().toByteArray());
    }

    credentialEntity.setEconomicActivity(credentialDto.getEconomicActivity());

    return credentialEntity;
  }

  public CredentialValidationDto validateCredentials(String userId, String credentialId) {
    CredentialValidationDto credentialValidationDto = new CredentialValidationDto();
    final var credentialEntity =
        this.credentialRepository.findCredentialByUserIdAndCredentialId(userId, credentialId);

    final var authenticationResponseDto =
        authenticationService.doLogin(
            credentialEntity.getUsername(), credentialEntity.getPassword());

    if (authenticationResponseDto.getAccessToken() != null) {
      final Boolean canOpen =
          this.tryToOpenKeyStore(credentialEntity.getKeystoreBytes(), credentialEntity.getPin());
      credentialValidationDto.setValid(canOpen.toString());
    }
    authenticationService.doLogout(authenticationResponseDto.getRefreshToken());

    return credentialValidationDto;
  }

  private String defineBrandImage(byte[] brandImage) {
    return brandImage == null
        ? ""
        : new String(Base64.getDecoder().decode(brandImage), Charset.forName("utf8"));
  }
}
