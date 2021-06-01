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
package com.github.gdiazs.fortbiz.web.resources.customers;

import com.github.gdiazs.fortbiz.customers.services.CustomerService;
import com.github.gdiazs.fortbiz.invoices.dto.InvoiceCustomerDto;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
@RequestScoped
public class CustomerResource {

  @Inject private CustomerService customerService;

  @Inject private JsonWebToken jsonWebToken;

  @POST
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response addCustomerInvoice(InvoiceCustomerDto invoiceCustomerDto) {
    final var userId = this.jsonWebToken.getClaim("sub").toString();
    final var addCustomerinvoice =
        this.customerService.addCustomerInvoice(invoiceCustomerDto, userId);
    return Response.ok(addCustomerinvoice).build();
  }

  @GET
  @Path("/{identificationNumber}")
  public Response findCustomer(@PathParam("identificationNumber") String identificationNumber) {
    final var findCustomerByIdentificationNumber =
        this.customerService.findCustomerByIdentificationNumber(identificationNumber);
    if (null == findCustomerByIdentificationNumber) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }
    return Response.ok(findCustomerByIdentificationNumber).build();
  }

  @PUT
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response editCustomerInvoice(InvoiceCustomerDto invoiceCustomerDto) {
    final var userId = this.jsonWebToken.getClaim("sub").toString();
    final var createCustomerInvoice =
        this.customerService.addCustomerInvoice(invoiceCustomerDto, userId);
    if (null == createCustomerInvoice) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(createCustomerInvoice).build();
  }
  
  @DELETE
  @Path("/{customerId}/invoice/{invoiceNumber}")
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response deleteCustomerInvoice(@PathParam("invoiceNumber") String invoiceNumber, @PathParam("customerId") String customerId) {
	  final var userId = this.jsonWebToken.getClaim("sub").toString();
	  this.customerService.deleteCustomerInvoice(customerId, invoiceNumber, userId);
	  return Response.ok().build();
  }
}
