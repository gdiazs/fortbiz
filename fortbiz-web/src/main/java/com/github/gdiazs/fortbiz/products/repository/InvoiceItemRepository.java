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
package com.github.gdiazs.fortbiz.products.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import com.github.gdiazs.fortbiz.entities.InvoiceItemEntity;
import com.github.gdiazs.fortbiz.entities.InvoiceItemEntityPK;

@Repository
public interface InvoiceItemRepository
    extends EntityRepository<InvoiceItemEntity, InvoiceItemEntityPK> {

  @Query("select ii from InvoiceItemEntity ii where ii.id.invoiceId = ?1")
  List<InvoiceItemEntity> findItemsByInvoiceId(final String invoiceId);

  @Query(
      "select ii from InvoiceItemEntity ii join InvoiceEntity inv on (ii.id.invoiceId = inv.id) "
          + "join BranchEntity b on (inv.branchId = b.id) "
          + "join UserBranchEntity ub on (b.id = ub.userBranchPk.branchId) "
          + "where ii.id.invoiceId = ?1 and ii.id.productId = ?2 and ub.userBranchPk.userId = ?3")
  InvoiceItemEntity findInvoiceItem(
      final String invoiceId, final String productId, final String userId);
}
