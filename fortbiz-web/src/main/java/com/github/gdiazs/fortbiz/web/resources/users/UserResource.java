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
package com.github.gdiazs.fortbiz.web.resources.users;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.github.gdiazs.fortbiz.branches.services.BranchService;
import com.github.gdiazs.fortbiz.credentials.services.CredentialService;
import com.github.gdiazs.fortbiz.invoices.exceptions.InvoicePdfHandlingException;
import com.github.gdiazs.fortbiz.users.dto.UserDto;
import com.github.gdiazs.fortbiz.users.services.UserService;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/users")
@RequestScoped
public class UserResource {

  @Inject private UserService usersService;

  @Inject private JsonWebToken jsonWebToken;

  @Inject private CredentialService credentialService;

  @Inject private BranchService branchService;

  @POST
  @Path("/verify")
  public Response addUserIfNotExists() {
    final var userId = jsonWebToken.getClaim("sub").toString();
    final var username = jsonWebToken.getClaim("preferred_username").toString();
    final var email = jsonWebToken.getClaim("email").toString();
    final var userEntity = this.usersService.addUserIfNotExists(userId, username, email);
    var userDto = UserDto.builder().exists(userEntity != null).userId(userEntity.getId()).build();

    return Response.ok(userDto).build();
  }

  @GET
  @Path("/{userId}/branches")
  public Response findBranchesByUserId(@PathParam("userId") String userId) {
    return Response.ok(this.branchService.findBranchesByUserId(userId)).build();
  }

  @GET
  @Path("/{userId}/credentials")
  public Response findCredentialsByUserId(@PathParam("userId") String userId)
      throws InvoicePdfHandlingException {
    return Response.ok(this.credentialService.findCredentialsByUserId(userId)).build();
  }
}
