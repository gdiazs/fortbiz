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
package com.github.gdiazs.fortbiz.invoices.repositories;

import com.github.gdiazs.fortbiz.entities.InvoiceEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.MaxResults;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends EntityRepository<InvoiceEntity, String> {

  @Query(
      "select iin "
          + "from InvoiceEntity iin "
          + " join UserBranchEntity ub on iin.branchId = ub.userBranchPk.branchId "
          + "where ub.userBranchPk.userId = ?1 and iin.branchId = ?2")
  List<InvoiceEntity> findInvoicesByBranchIdAndUserId(String userId, String branchId);

  @Query(
      "select iin "
          + "from InvoiceEntity iin "
          + " join UserBranchEntity ub on iin.branchId = ub.userBranchPk.branchId "
          + "where ub.userBranchPk.userId = ?1 and iin.branchId = ?2 "
          + "order by iin.createdAt desc")
  InvoiceEntity findInvoicesByBranchIdAndUserIdOrderByDate(
      String userId, String branchId, @MaxResults int limit);

  @Query(
      "select count(i.id) "
          + "from InvoiceEntity i  join UserBranchEntity ub  on ub.userBranchPk.branchId = i.branchId  "
          + "where ub.userBranchPk.branchId = ?1 and ub.userBranchPk.userId = ?2")
  Long countInvoicesByBranchIdAndUserId(String branchId, String userId);

  @Query(
      "select i from InvoiceEntity i join UserBranchEntity ub on i.branchId = ub.userBranchPk.branchId and ub.userBranchPk.userId = ?1 and i.invoiceNumber = ?2")
  InvoiceEntity findInvoiceByNumber(String userId, String invoiceNumber);
}
