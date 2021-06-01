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
package com.github.gdiazs.fortbiz.branches.converters;

import com.github.gdiazs.fortbiz.branches.dto.BranchDto;
import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.entities.BranchEntity;
import com.github.gdiazs.fortbiz.utils.StringFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BranchConverter implements ModelConverter<BranchDto, BranchEntity> {

  private final StringFormatter stringFormatter;

  @Inject
  public BranchConverter(StringFormatter stringFormatter) {
    this.stringFormatter = stringFormatter;
  }

  @Override
  public BranchEntity convertTo(BranchDto source) {
    final BranchEntity branchEntity = new BranchEntity();
    branchEntity.setName(source.getName());
    final String phoneWithoutFormat = source.getPhone().replace(" ", "").replace("-", "");
    branchEntity.setPhone(Integer.valueOf(phoneWithoutFormat));
    branchEntity.setAddress(source.getAddress());
    branchEntity.setEmail(source.getEmail());
    branchEntity.setDescription(source.getDescription());
    branchEntity.setCredentialId(source.getCredentialId());
    return branchEntity;
  }

  @Override
  public BranchDto convertFrom(BranchEntity source) {
    return BranchDto.builder()
        .id(source.getId())
        .name(source.getName())
        .address(source.getAddress())
        .phone(String.valueOf(source.getPhone()))
        .email(source.getEmail())
        .description(source.getDescription())
        .credentialId(source.getCredentialId())
        .createdAt(stringFormatter.formatToCrDates(source.getCreatedAt()))
        .updatedAt(stringFormatter.formatToCrDates(source.getUpdatedAt()))
        .build();
  }
}
