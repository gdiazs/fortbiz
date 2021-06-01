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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CUSTOMERS")
@Getter
@Setter
public class CustomerEntity extends UUIDEntity {

  @Column(name = "IDENTIFICATION_NUMBER")
  private String identificationNumber;

  @Column(name = "IDENTIFICATION_TYPE")
  private String identificationType;

  @Column(name = "CUSTOMER_NAME")
  private String customerName;

  private String address;

  @Column(name = "FIRST_LAST_NAME")
  private String firstLastName;

  @Column(name = "SECOND_LAST_NAME")
  private String secondLastName;

  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;

  public CustomerEntity() {}
}
