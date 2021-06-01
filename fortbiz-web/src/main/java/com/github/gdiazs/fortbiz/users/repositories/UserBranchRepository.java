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
package com.github.gdiazs.fortbiz.users.repositories;

import com.github.gdiazs.fortbiz.entities.UserBranchEntity;
import com.github.gdiazs.fortbiz.entities.UserBranchPk;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface UserBranchRepository extends EntityRepository<UserBranchEntity, UserBranchPk> {

  @Query(
      "delete from UserBranchEntity where userBranchPk.userId = ?1 AND userBranchPk.branchId = ?2")
  public void deleteByUserIdAndBranchId(String userId, String branchId);
}
