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
package com.github.gdiazs.fortbiz.web.resources.branches;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.gdiazs.fortbiz.branches.dto.BranchDto;
import com.github.gdiazs.fortbiz.branches.services.BranchService;
import com.github.gdiazs.fortbiz.commons.dto.ErrorDto;
import com.github.gdiazs.fortbiz.invoices.services.InvoiceService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/branches/v1")
@RequestScoped
public class BranchResource {

  @Inject private BranchService branchService;

  @Inject private InvoiceService invoiceService;

  @Inject private JsonWebToken jsonWebToken;

  @POST
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response addBranch(BranchDto branchDto) {
    final var userId = this.jsonWebToken.getClaim("sub").toString();
    final var createdBranch = this.branchService.addBranch(branchDto, userId);
    return Response.ok(createdBranch).build();
  }

  @PUT
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response updateBranch(BranchDto branchDto) {
    final var createdBranch = this.branchService.updateBranch(branchDto);
    return Response.ok(createdBranch).build();
  }

  @DELETE
  @Path("/{branchId}")
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response updateBranch(@PathParam("branchId") String branchId) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();

    try {
      this.branchService.deleteBranchById(userId, branchId);
    } catch (Exception e) {
      return this.handleException(e);
    }
    return Response.ok().build();
  }

  private Response handleException(Exception e) {
    Throwable rootCause = ExceptionUtils.getRootCause(e);
    if (rootCause.getMessage().contains("violates foreign key constraint")) {
      var errorDto =
          ErrorDto.builder()
              .status(String.valueOf(Status.CONFLICT.getStatusCode()))
              .timestamp(String.valueOf(System.currentTimeMillis()))
              .errorMessage("Branch has invoices")
              .build();
      return Response.status(Status.CONFLICT).entity(errorDto).build();
    }
    throw new RuntimeException("Unable to handle", rootCause);
  }

  @GET
  @Path("/{branchId}/invoices")
  public Response getInvoices(@PathParam("branchId") String branchId) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();
    return Response.ok(this.invoiceService.findInvoicesByBranchId(userId, branchId)).build();
  }
}
