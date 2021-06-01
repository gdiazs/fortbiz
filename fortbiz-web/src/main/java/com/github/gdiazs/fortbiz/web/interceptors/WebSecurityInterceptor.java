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
package com.github.gdiazs.fortbiz.web.interceptors;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.keycloak.KeycloakSecurityContext;

import com.github.gdiazs.fortbiz.web.interceptors.annotations.Secured;

@Secured
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class WebSecurityInterceptor implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject private KeycloakSecurityContext keycloakSecurityContext;

  @AroundInvoke
  public Object logMethodEntry(InvocationContext ctx) throws Exception {
    boolean hasAccess;
    final Method method = ctx.getMethod();
    final RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);

    if (null == rolesAllowed) {
      return ctx.proceed();
    }

    var roles = Arrays.asList(rolesAllowed.value());
    hasAccess =
        keycloakSecurityContext.getToken().getRealmAccess().getRoles().stream()
            .anyMatch((role) -> roles.contains(role));

    if (hasAccess) {
      return ctx.proceed();
    }
    var access = keycloakSecurityContext.getToken().getResourceAccess().values();
    var iterator = access.iterator();
    while (iterator.hasNext()) {
      var next = iterator.next();
      hasAccess = next.getRoles().stream().anyMatch((role) -> roles.contains(role));
      if (hasAccess) {
        return ctx.proceed();
      }
    }

    throw new RuntimeException(
        "User: "
            + keycloakSecurityContext.getToken().getPreferredUsername()
            + " does not have next roles: "
            + roles
            + ". Please validate user roles in Keycloak");
  }
}