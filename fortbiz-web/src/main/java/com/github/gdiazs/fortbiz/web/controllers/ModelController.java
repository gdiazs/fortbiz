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
package com.github.gdiazs.fortbiz.web.controllers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.github.gdiazs.fortbiz.web.dto.ModelDto;

@Named
@RequestScoped
public class ModelController {

  @Inject
  @ConfigProperty(name = "fortbiz.oidc.url")
  private String oidcUrl;

  @Inject
  @ConfigProperty(name = "mp.config.profile")
  private String env;

  private String viewModel;

  public ModelController() {
    var jsonbParser = JsonbBuilder.create();
    this.viewModel = jsonbParser.toJson(buildModelDto(), ModelDto.class);
  }

  private ModelDto buildModelDto() {
    var model = new ModelDto();
    model.getViewModel().put("oidcUrl", oidcUrl);
    model.getViewModel().put("env", env);
    return model;
  }

  public String getViewModel() {
    return viewModel;
  }
}
