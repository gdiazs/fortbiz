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
package com.github.gdiazs.fortbiz.web.resources.reports;

import com.github.gdiazs.fortbiz.hacienda.v43.invoice.FacturaElectronica;
import com.github.gdiazs.fortbiz.invoices.services.InvoiceHaciendaService;
import com.github.gdiazs.fortbiz.reports.exceptions.ReportsException;
import com.github.gdiazs.fortbiz.reports.services.InvoiceReportService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;


@WebServlet(urlPatterns = "/reports/preview")
public class PreviewReportResource extends HttpServlet {

  @Inject private InvoiceReportService invoiceReportService;
  @Inject private InvoiceHaciendaService invoiceHaciendaService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    final String format = req.getParameter("format");
    final String invoiceId = req.getParameter("invoiceId");
    final String userId = req.getParameter("userId");

    if ("pdf".equals(format)) {

      try (final OutputStream responseOutputStream = resp.getOutputStream()) {
        final byte[] pdfBytes = this.invoiceReportService.generateInvoiceReport(userId, invoiceId);
        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition", String.format("inline; filename=%s.pdf", invoiceId));

        responseOutputStream.write(pdfBytes);

      } catch (ReportsException | IOException e) {
        e.printStackTrace();
      }
    }
    if ("xml".equals(format)) {

      try (final OutputStream responseOutputStream = resp.getOutputStream()) {
        final FacturaElectronica facturaElectronica = this.invoiceHaciendaService.fillElectronicInvoice(userId, invoiceId);
        resp.setContentType("application/xml");
        resp.setHeader("Content-disposition", String.format("inline; filename=%s.xml", invoiceId));

        JAXBContext contextObj = JAXBContext.newInstance(FacturaElectronica.class);
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        marshallerObj.setProperty(
                Marshaller.JAXB_SCHEMA_LOCATION,
                "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronica "
                        + "https://www.hacienda.go.cr/ATV/ComprobanteElectronico/docs/esquemas/2016/v4.3/FacturaElectronica_V4.3.xsd");
        final var sw = new StringWriter();
        marshallerObj.marshal(facturaElectronica, new StreamResult(sw));

        responseOutputStream.write(sw.toString().getBytes(StandardCharsets.UTF_8));

      } catch (IOException | JAXBException e) {
        e.printStackTrace();
      }
    }
  }
}
