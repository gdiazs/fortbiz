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
package com.github.gdiazs.fortbiz.branches.repositories;

import com.github.gdiazs.fortbiz.entities.BranchEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends EntityRepository<BranchEntity, String> {

  @Query("select b from BranchEntity b where b.credentialId = ?1")
  List<BranchEntity> findAllBranchesByCredentialId(String credentialId);

  @Query(
      "select b from BranchEntity b join UserBranchEntity ub on b.id = ub.userBranchPk.branchId and ub.userBranchPk.userId = ?1")
  List<BranchEntity> findBranchesByUserId(String userId);

  @Query(
      "select count(b) from BranchEntity b join UserBranchEntity ub on b.id = ub.userBranchPk.branchId and ub.userBranchPk.userId = ?1")
  Long countUserBranches(String userId);
}
