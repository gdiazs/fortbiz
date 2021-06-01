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
package com.github.gdiazs.fortbiz.branches.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.NoResultException;

import com.github.gdiazs.fortbiz.branches.exception.UnLinkBranchCredentialException;
import com.github.gdiazs.fortbiz.branches.repositories.BranchRepository;
import com.github.gdiazs.fortbiz.cashregisters.repositories.CashRegisterRepository;
import com.github.gdiazs.fortbiz.converters.ModelConverter;
import com.github.gdiazs.fortbiz.entities.BranchEntity;
import com.github.gdiazs.fortbiz.entities.CashRegisterEntity;
import com.github.gdiazs.fortbiz.entities.UserBranchEntity;
import com.github.gdiazs.fortbiz.entities.UserBranchPk;
import com.github.gdiazs.fortbiz.invoices.repositories.InvoiceRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import com.github.gdiazs.fortbiz.branches.dto.BranchDto;
import com.github.gdiazs.fortbiz.users.repositories.UserBranchRepository;

@Singleton
public class BranchService {
  private static Logger LOGGER = Logger.getLogger(BranchService.class.getName());

  private final BranchRepository branchRepository;
  private final CashRegisterRepository cashRegisterRepository;
  private final UserBranchRepository userBranchRepository;
  private final InvoiceRepository invoiceRepository;
  private final ModelConverter<BranchDto, BranchEntity> branchConverter;

  @Inject
  public BranchService(
      BranchRepository branchRepository,
      CashRegisterRepository cashRegisterRepository,
      UserBranchRepository userBranchRepository,
      ModelConverter<BranchDto, BranchEntity> branchDtoModelConverter,
      InvoiceRepository invoiceRepository) {

    this.cashRegisterRepository = cashRegisterRepository;
    this.branchRepository = branchRepository;
    this.userBranchRepository = userBranchRepository;
    this.invoiceRepository = invoiceRepository;
    this.branchConverter = branchDtoModelConverter;
  }

  @Transactional
  public BranchDto addBranch(final BranchDto branchDto, final String idUser) {
    final BranchEntity branchEntity = branchConverter.convertTo(branchDto);
    branchEntity.setBranchNumber(this.branchRepository.countUserBranches(idUser).intValue() + 1);

    final BranchEntity savedBranch = this.branchRepository.saveAndFlush(branchEntity);

    final UserBranchEntity userBranchEntity = new UserBranchEntity();
    userBranchEntity.setAccessType("OWNER");

    final UserBranchPk userBranchPk = new UserBranchPk();
    userBranchPk.setBranchId(savedBranch.getId());
    userBranchPk.setUserId(idUser);
    userBranchEntity.setUserBranchPk(userBranchPk);

    this.userBranchRepository.saveAndFlush(userBranchEntity);

    final CashRegisterEntity cashRegisterEntity = new CashRegisterEntity();
    cashRegisterEntity.setBranchId(savedBranch.getId());
    cashRegisterEntity.setCashRegisterNumber(1);

    this.cashRegisterRepository.save(cashRegisterEntity);

    return this.branchConverter.convertFrom(savedBranch);
  }

  @Transactional
  public BranchDto updateBranch(BranchDto branchDto) {
    final var foundBranch = this.branchRepository.findBy(branchDto.getId());
    foundBranch.setName(branchDto.getName());
    foundBranch.setAddress(branchDto.getAddress());
    foundBranch.setDescription(branchDto.getDescription());
    foundBranch.setEmail(branchDto.getEmail());
    foundBranch.setCredentialId(
        branchDto.getCredentialId().isEmpty() ? null : branchDto.getCredentialId());
    final String phoneWithoutFormat = branchDto.getPhone().replace(" ", "").replace("-", "");
    foundBranch.setPhone(Integer.valueOf(phoneWithoutFormat));
    return branchConverter.convertFrom(this.branchRepository.save(foundBranch));
  }

  public List<BranchDto> findBranchesByUserId(String userId) {
    final List<BranchEntity> userBranches = this.branchRepository.findBranchesByUserId(userId);
    return (List<BranchDto>) this.branchConverter.convertAllFrom(userBranches);
  }

  @Transactional
  public void deleteBranchById(String userId, String branchId) {
    final BranchEntity branchEntity = this.branchRepository.findBy(branchId);
    final List<CashRegisterEntity> cashRegisters =
        this.cashRegisterRepository.findAllCashRegisterByBranchId(branchId);
    if (!cashRegisters.isEmpty()) {
      final CashRegisterEntity cashRegisterEntity = cashRegisters.get(0);
      this.cashRegisterRepository.remove(cashRegisterEntity);
    }

    this.userBranchRepository.deleteByUserIdAndBranchId(userId, branchId);
    this.branchRepository.remove(branchEntity);
  }

  @Transactional
  public void unLinkCredential(String credentialId, String userId) {
    List<BranchEntity> branches;
    try {
      branches = branchRepository.findAllBranchesByCredentialId(credentialId);
      branches.forEach(
          (branch) -> {
            var invoice =
                this.invoiceRepository.findInvoicesByBranchIdAndUserId(userId, branch.getId());
            if (branch.getCredentialId() != null && invoice == null || invoice.size() == 0) {
              branch.setCredentialId(null);
              branchRepository.save(branch);
            } else {
              throw new UnLinkBranchCredentialException("branch has invoices");
            }
          });
    } catch (NoResultException noResultException) {
      LOGGER.log(Level.INFO, "branch not found", noResultException);
    }
  }
}
