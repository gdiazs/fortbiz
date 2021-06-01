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
 */package com.github.gdiazs.fortbiz.digitalsignature;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.KeyStore;

import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import xades4j.XAdES4jException;
import xades4j.production.Enveloped;

@Singleton
public class StringXmlDigitalSignatureServiceImpl extends AbstractXadesSignature
    implements DigitalSignatureService<String> {

  @Override
  public String sign(String xml, KeyStore keyStore, String pin) throws DigitalSignatureException {
    final var xadesProfile = this.getXadesSigningProfile(keyStore, pin);
    try {
      System.setProperty("org.apache.xml.security.ignoreLineBreaks", "true");
      final var signer = xadesProfile.newSigner();
      final var factory = DocumentBuilderFactory.newInstance();
      final var builder = factory.newDocumentBuilder();
      final var document = builder.parse(new InputSource(new StringReader(xml)));
      final var elementToSign = document.getDocumentElement();

      new Enveloped(signer).sign(elementToSign);
      elementToSign.setAttribute("xmlns:dsig", "http://www.w3.org/2000/09/xmldsig#");

      final var sw = new StringWriter();
      final var tf = TransformerFactory.newInstance();
      final var transformer = tf.newTransformer();

      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, Charset.forName("UTF-8").toString());

      transformer.transform(new DOMSource(document), new StreamResult(sw));
      return sw.toString();

    } catch (ParserConfigurationException
        | SAXException
        | IOException
        | XAdES4jException
        | TransformerException e) {
      throw new DigitalSignatureException("Error while signing an XML String format", e);
    }
  }
}
