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
package com.github.gdiazs.fortbiz.web.resources.localization;

import com.github.gdiazs.fortbiz.localization.services.LocalizationService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addresses")
public class AddressesRosource {

  @Inject private LocalizationService localizationService;

  @GET
  @Path("/provinces")
  @Produces(value = MediaType.APPLICATION_JSON)
  public Response findListProvince() {
    return Response.ok(this.localizationService.getCostarricanProvinces()).build();
  }

  @GET
  @Path("/{id}/cantons")
  @Produces(value = MediaType.APPLICATION_JSON)
  public Response findListCanton(@PathParam("id") String idProvince) {
    return Response.ok(this.localizationService.getCostarricanCantons(idProvince)).build();
  }

  @GET
  @Path("/{idProvince}/cantons/{idCanton}/disctrics")
  @Produces(value = MediaType.APPLICATION_JSON)
  public Response findListDistrict(
      @PathParam("idProvince") String idProvince, @PathParam("idCanton") String idCanton) {
    return Response.ok(this.localizationService.getCostarricanDistrict(idProvince, idCanton))
        .build();
  }
}
