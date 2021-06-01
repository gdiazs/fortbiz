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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import com.github.gdiazs.fortbiz.hacienda.v43.invoice.EmisorType;
import com.github.gdiazs.fortbiz.hacienda.v43.invoice.FacturaElectronica;
import com.github.gdiazs.fortbiz.hacienda.v43.invoice.ReceptorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StringXmlDigitalSignatureServiceImplTest {

  @InjectMocks
  private DigitalSignatureService<String> digitalSignatureService =
      new StringXmlDigitalSignatureServiceImpl();

  private KeyStore keyStore;
  private final String ENCODING = "UTF8";

  @BeforeEach
  public void startUp()
      throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
    var file = new File("src/test/resources/test-keystore.p12");
    var fileInputStream = new FileInputStream(file);
    keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(fileInputStream, "1234".toCharArray());
  }

  @Test
  void sign() throws Exception {
    FacturaElectronica facturaElectronica = new FacturaElectronica();
    facturaElectronica.setEmisor(new EmisorType());
    facturaElectronica.setCodigoActividad("1");
    facturaElectronica.setReceptor(new ReceptorType());
    facturaElectronica.setDetalleServicio(new FacturaElectronica.DetalleServicio());

    JAXBContext contextObj = JAXBContext.newInstance(FacturaElectronica.class);
    Marshaller marshallerObj = contextObj.createMarshaller();
    marshallerObj.setProperty(Marshaller.JAXB_ENCODING, ENCODING);
    marshallerObj.setProperty(
        Marshaller.JAXB_SCHEMA_LOCATION,
        "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronica "
            + "https://www.hacienda.go.cr/ATV/ComprobanteElectronico/docs/esquemas/2016/v4.3/FacturaElectronica_V4.3.xsd");

    final var sw = new StringWriter();
    marshallerObj.marshal(facturaElectronica, new StreamResult(sw));
    assertTrue(
        digitalSignatureService.sign(sw.toString(), this.keyStore, "1234").contains("xades"));
  }
}
