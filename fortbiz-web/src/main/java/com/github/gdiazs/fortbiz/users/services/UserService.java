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
package com.github.gdiazs.fortbiz.users.services;

import com.github.gdiazs.fortbiz.entities.UserEntity;
import com.github.gdiazs.fortbiz.users.repositories.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
public class UserService {

  private final UserRepository userRepository;

  @Inject
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public UserEntity addUserIfNotExists(String userId, String username, String email) {
    var userFound = userRepository.findBy(userId);
    if (null == userFound) {
      var newUser = new UserEntity();
      newUser.setEmail(email);
      newUser.setUsername(username);
      newUser.setId(userId);
      userFound = this.userRepository.saveAndFlush(newUser);
    }
    return userFound;
  }

  public UserEntity findUserById(String userId) {
    return userRepository.findBy(userId);
  }
}