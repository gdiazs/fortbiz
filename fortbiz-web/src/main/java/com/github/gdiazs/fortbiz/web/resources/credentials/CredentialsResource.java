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
package com.github.gdiazs.fortbiz.web.resources.credentials;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.github.gdiazs.fortbiz.hacienda.activities.services.HaciendaActivitiesService;
import com.github.gdiazs.fortbiz.commons.dto.ErrorDto;
import com.github.gdiazs.fortbiz.credentials.dto.CredentialDto;
import com.github.gdiazs.fortbiz.credentials.dto.CredentialsErrorsDto;
import com.github.gdiazs.fortbiz.credentials.exceptions.MinisterioHaciendaAuthException;
import com.github.gdiazs.fortbiz.credentials.services.CredentialService;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/credentials")
@RequestScoped
public class CredentialsResource {

  private static final Logger LOGGER = Logger.getLogger(CredentialsResource.class.getName());

  @Inject private CredentialService credentialService;

  @Inject private JsonWebToken jsonWebToken;

  @Inject private HaciendaActivitiesService haciendaActivitiesService;

  public CredentialsResource() {}

  @POST
  @Consumes({MediaType.MULTIPART_FORM_DATA})
  @Produces(value = MediaType.APPLICATION_JSON)
  public Response addCredential(MultipartBody body) {
    try {
      final var credentialDto = buildCredentialDto(body);
      var credentialSaved =
          credentialService.addCredential(credentialDto, jsonWebToken.getClaim("sub").toString());
      return Response.ok(credentialSaved).build();
    } catch (MinisterioHaciendaAuthException ex) {
      return handleException(ex);
    }
  }

  @DELETE
  @Path("/{credentialId}")
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response deleteBranch(@PathParam("credentialId") String credentialId) {
    Response response = null;
    try {
      this.credentialService.deleteCredentialById(
          this.jsonWebToken.getClaim("sub").toString(), credentialId);
      response = Response.ok().build();
    } catch (DeleteCredentialException e) {
      var error =
          ErrorDto.builder()
              .status(String.valueOf(Status.CONFLICT.getStatusCode()))
              .errorMessage(e.getMessage())
              .timestamp(String.valueOf(System.currentTimeMillis()))
              .build();
      response = Response.status(Status.CONFLICT).entity(error).build();
    }
    return response;
  }

  @PUT
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes({MediaType.MULTIPART_FORM_DATA})
  public Response updateCredential(MultipartBody body) throws IOException {
    var credentialDto = buildCredentialDto(body);
    credentialDto = this.credentialService.updateCredential(credentialDto);
    return Response.ok(credentialDto).build();
  }

  @GET
  @Path("/{credentialId}/validate")
  public Response validateCredetials(@PathParam("credentialId") final String credentialId) {
    var usreId = jsonWebToken.getClaim("sub").toString();
    var credentialValidation = this.credentialService.validateCredentials(usreId, credentialId);
    return Response.ok(credentialValidation).build();
  }
  @GET
  @Path("/hacienda-actvities/{identification}")
  public Response findEconomyActivity(@PathParam("identification") final String identification){
    var haciendaActivity = haciendaActivitiesService.findCustomerInformationById(identification);
    return Response.ok(haciendaActivity).build();
  }

  private CredentialDto buildCredentialDto(MultipartBody body) {
    final InputStream keystoreInputStream = body.getAttachmentObject("keystore", InputStream.class);
    ByteArrayOutputStream target = null;
    try (ByteArrayOutputStream targetStream = new ByteArrayOutputStream()) {
      IOUtils.copy(keystoreInputStream, targetStream);
      target = targetStream;
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, null, ex);
    }

    return CredentialDto.builder()
        .username(body.getAttachmentObject("username", String.class))
        .password(body.getAttachmentObject("password", String.class))
        .identificationNumber(body.getAttachmentObject("identificationNumber", String.class))
        .identificationType(body.getAttachmentObject("identificationType", String.class))
        .localization(body.getAttachmentObject("localization", String.class))
        .tradeName(body.getAttachmentObject("tradeName", String.class))
        .economicActivity(body.getAttachmentObject("economicActivity", String.class))
        .activityCode(body.getAttachmentObject("activityCode", String.class))
        .pin(body.getAttachmentObject("pin", String.class))
        .id(body.getAttachmentObject("id", String.class))
        .brandImage(body.getAttachmentObject("brandImage", String.class))
        .keystore(target)
        .build();
  }

  private Response handleException(MinisterioHaciendaAuthException ex) {

    if (ex.getMessage().contains("access to Hacienda")) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(CredentialsErrorsDto.builder().error("Invalid Hacienda credentials").build())
          .build();
    }

    if (ex.getMessage().contains("Error trying to open user keystore")) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(CredentialsErrorsDto.builder().error("Invalid Keystore PIN").build())
          .build();
    }

    return Response.serverError().build();
  }
}
