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
package com.github.gdiazs.fortbiz.digitalsignature;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;

import org.eclipse.microprofile.config.ConfigProvider;

import xades4j.production.XadesEpesSigningProfile;
import xades4j.production.XadesSigner;
import xades4j.production.XadesSigningProfile;
import xades4j.properties.ObjectIdentifier;
import xades4j.properties.SignaturePolicyIdentifierProperty;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.SignaturePolicyInfoProvider;

public abstract class AbstractXadesSignature {

  public static final int FIRST_CERTIFICATE = 0;
  public static final String POLICY_MESSAGE = "Politica de Factura Digital";
  private KeyStore keystore;
  protected XadesSigner xadesSigner;
  private SignaturePolicyInfoProvider policyInfoProvider;

  protected XadesSigningProfile getXadesSigningProfile(KeyStore keyStore, String password) {
    this.setKeyStore(keyStore);
    final String signaturePolicy =
        ConfigProvider.getConfig().getValue("fortbiz.hacienda.policy", String.class);

    KeyingDataProvider kp =
        new InMemoryKeyingDataProvider(
            this.keystore,
            (certificates) -> certificates.get(FIRST_CERTIFICATE),
            new KeyStorePasswordProviderImpl(password),
            new KeyStorePasswordProviderImpl(password),
            false);

    policyInfoProvider =
        () ->
            new SignaturePolicyIdentifierProperty(
                new ObjectIdentifier(signaturePolicy),
                new ByteArrayInputStream(POLICY_MESSAGE.getBytes(Charset.forName("UTF-8"))));

    return new XadesEpesSigningProfile(kp, policyInfoProvider);
  }

  public void setKeyStore(KeyStore keyStore) {
    this.keystore = keyStore;
  }

  public KeyStore getKeystore() {
    if (null == this.keystore) throw new DigitalSignatureException("Keystore is null");

    return this.keystore;
  }
}
