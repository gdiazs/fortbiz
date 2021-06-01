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
package com.github.gdiazs.fortbiz.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CREDENTIALS")
@Setter
@Getter
public class CredentialEntity extends UUIDEntity {

  @Column(name = "IDENTIFICATION_NUMBER")
  private String indentificationNumber;

  @Column(name = "IDENTIFICATION_TYPE")
  private String indentificationType;

  @Column(name = "TRADE_NAME")
  private String tradeName;

  @Column(name = "ACTIVITY_CODE")
  private String activityCode;

  @Column(name = "LOCALIZATION")
  private String localization;

  @Column(name = "ECONOMIC_ACTIVITY")
  private String economicActivity;

  private String username;

  private String password;

  private String pin;

  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  @Column(name = "KEYSTORE")
  private byte[] keystoreBytes;

  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  @Column(name = "BRAND_IMAGE")
  private byte[] brandImage;

  public CredentialEntity() {}
}
