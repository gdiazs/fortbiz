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
package com.github.gdiazs.fortbiz.web.resources.invoices;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.gdiazs.fortbiz.utils.CurrencyFormatter;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceDto;
import com.github.gdiazs.fortbiz.invoices.services.InvoiceService;
import com.github.gdiazs.fortbiz.invoices.services.InvoiceUnitsService;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
@Path("/invoices")
public class InvoiceResource {

  private static Logger LOGGER = Logger.getLogger(InvoiceResource.class.getName());

  @Inject private InvoiceService invoiceService;

  @Inject private JsonWebToken jsonWebToken;

  @Inject private InvoiceUnitsService invoiceUnitsService;

  @Inject private CurrencyFormatter currencyFormatter;

  @GET
  @Path("/{invoiceNumber}")
  public Response findInvoiceByNumber(@PathParam("invoiceNumber") String invoiceNumber) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();
    var foundInvoiceDto = this.invoiceService.findInvoiceByNumber(invoiceNumber, userId);
    foundInvoiceDto.setUnits(this.invoiceUnitsService.getAvailableUnits());
    return Response.ok(foundInvoiceDto).build();
  }

  @GET
  @Path("/{invoiceNumber}/items")
  public Response findInvoiceDetails(@PathParam("invoiceNumber") String invoiceNumber) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();
    var foundInvoiceDetailsDto = this.invoiceService.findInvoiceDetails(userId, invoiceNumber);
    if (null == foundInvoiceDetailsDto) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    foundInvoiceDetailsDto.setSubtotal(
        currencyFormatter.twoDecimalFormatString(foundInvoiceDetailsDto.getSubtotal()));
    foundInvoiceDetailsDto.setTaxesTotal(
        currencyFormatter.twoDecimalFormatString(foundInvoiceDetailsDto.getTaxesTotal()));
    foundInvoiceDetailsDto.setTotal(
        currencyFormatter.twoDecimalFormatString(foundInvoiceDetailsDto.getTotal()));
    foundInvoiceDetailsDto
        .getItems()
        .forEach(
            product -> {
              product.setSubtotal(currencyFormatter.twoDecimalFormatString(product.getSubtotal()));
              product.setPrice(currencyFormatter.twoDecimalFormatString(product.getPrice()));
              product.setQuantity(currencyFormatter.twoDecimalFormatString(product.getQuantity()));
            });

    return Response.ok(foundInvoiceDetailsDto).build();
  }

  @POST
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response addHeaderInvoice(InvoiceDto invoiceDto) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();
    final var createHeaderInvoice = this.invoiceService.createHeaderInvoice(invoiceDto, userId);
    return Response.ok(createHeaderInvoice).build();
  }

  @POST
  @Path("/{invoiceNumber}")
  public Response sendInvoiceToHacienda(@PathParam("invoiceNumber") String invoiceNumber) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();

    try {
      this.invoiceService.sendInvoiceToHacienda(userId, invoiceNumber);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error sending info to Hacienda", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    return Response.ok().build();
  }

  @DELETE
  @Path("/{invoiceNumber}")
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response deleteInvoiceByNumber(@PathParam("invoiceNumber") String invoiceNumber) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();
    this.invoiceService.deleteInvoiceByNumber(userId, invoiceNumber);
    return Response.ok().build();
  }

  @DELETE
  @Path("/{invoiceNumber}/product/{idProduct}")
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response deleteProductInvoiceDetail(
      @PathParam("invoiceNumber") String invoiceNumber, @PathParam("idProduct") String idProduct) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();
    var invoiceItemDto = this.invoiceService.deleteInvoiceDetail(userId, invoiceNumber, idProduct);
    return Response.ok(invoiceItemDto).build();
  }
}
