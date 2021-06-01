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
package com.github.gdiazs.fortbiz.invoices.repositories.impl;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.github.gdiazs.fortbiz.invoices.repositories.SystemSequenceRepository;

@Singleton
public class SystemSequenceRepositoryImpl implements SystemSequenceRepository {

  private EntityManager entityManager;

  @Inject
  public SystemSequenceRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Long generateNextSecurityCode() {
    final Query query = this.entityManager.createNativeQuery("SELECT NEXTVAL('security_code_seq')");
    BigInteger sequenceSecurityCode = (BigInteger) query.getSingleResult();
    return sequenceSecurityCode.longValue();
  }
}
