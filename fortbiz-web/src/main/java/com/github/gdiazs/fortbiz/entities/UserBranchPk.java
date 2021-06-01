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

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
public class UserBranchPk implements Serializable {

  private static final long serialVersionUID = 1L;

  public UserBranchPk() {}

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "BRANCH_ID")
  private String branchId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserBranchPk that = (UserBranchPk) o;
    return Objects.equals(userId, that.userId) && Objects.equals(branchId, that.branchId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, branchId);
  }
}
