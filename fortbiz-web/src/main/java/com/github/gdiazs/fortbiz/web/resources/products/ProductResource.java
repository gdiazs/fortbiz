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
package com.github.gdiazs.fortbiz.web.resources.products;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.gdiazs.fortbiz.hacienda.cabys.CabysService;
import com.github.gdiazs.fortbiz.products.dto.ProductDto;
import com.github.gdiazs.fortbiz.products.service.ProductsService;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
@Path("/products")
public class ProductResource {

  @Inject private CabysService cabysService;

  @Inject private ProductsService productsService;

  @Inject private JsonWebToken jsonWebToken;

  @GET
  @Path("/cabys")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findCabysProduct(@QueryParam("search") String search) {
    var response = cabysService.searchProductsByDescription(search);

    if (null == response || null == search || search.isEmpty()) {
      return Response.noContent().build();
    }
    return Response.ok(response).build();
  }

  @POST
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response addInvoiceDetail(ProductDto productDto) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();
    var response = this.productsService.addNewProduct(userId, productDto);
    return Response.ok(response).build();
  }

  @PUT
  @Produces(value = MediaType.APPLICATION_JSON)
  @Consumes(value = MediaType.APPLICATION_JSON)
  public Response updateInvoiceDetail(ProductDto productDto) {
    final String userId = this.jsonWebToken.getClaim("sub").toString();
    var product = this.productsService.updateProduct(userId, productDto);
    return Response.ok(product).build();
  }
}
