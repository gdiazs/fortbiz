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
package com.github.gdiazs.fortbiz.credentials.repository;

import com.github.gdiazs.fortbiz.entities.CredentialEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface CredentialRepository extends EntityRepository<CredentialEntity, String> {

  @Query(
      "SELECT c FROM UserCredentialEntity uc inner join CredentialEntity c ON c.id = uc.userCredentialPk.credentialId AND uc.userCredentialPk.userId = ?1")
  List<CredentialEntity> findCredentialsByUserId(String userId);

  @Query(
      "SELECT c FROM UserCredentialEntity uc inner join CredentialEntity c ON c.id = uc.userCredentialPk.credentialId AND uc.userCredentialPk.userId = ?1 AND c.id = ?2")
  CredentialEntity findCredentialByUserIdAndCredentialId(String userId, String credentialId);

  @Query(
      "select c from CredentialEntity c join BranchEntity b ON c.id = b.credentialId and b.id  = ?1")
  CredentialEntity findCredentialByBranchId(String branchId);
}
