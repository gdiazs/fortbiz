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
package com.github.gdiazs.fortbiz.credentials.dto;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.FormParam;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDto {

  private String id;

  @Multipart("username")
  private String username;

  @Multipart("password")
  private String password;

  @Multipart("identificationNumber")
  private String identificationNumber;

  @Multipart("identificationType")
  private String identificationType;

  @FormParam("activityCode")
  private String activityCode;

  @FormParam("localization")
  private String localization;

  @FormParam("tradeName")
  private String tradeName;

  @FormParam("economicActivity")
  private String economicActivity;

  @FormParam("pin")
  private String pin;

  @Multipart("keystore")
  private ByteArrayOutputStream keystore;

  @FormParam("filename")
  private String fileName;

  @FormParam("brandImage")
  private String brandImage;

  private String updatedAt;

  private String createdAt;

  private String province;

  private String canton;

  private String district;
}
