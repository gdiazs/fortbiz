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
package com.github.gdiazs.fortbiz.config;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Singleton;

import org.eclipse.microprofile.config.spi.ConfigSource;

@Singleton
public class FortbizConfigSource implements ConfigSource {

  private final String profileActive;
  private final Properties props;

  public FortbizConfigSource() {
    props = new Properties();
    this.profileActive = System.getProperty("mp.config.profile", "");
    try {
      props.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
    } catch (IOException e) {
      throw new FileSystemNotFoundException();
    }
  }

  @Override
  public Map<String, String> getProperties() {
    final Map<String, String> configProps = new HashMap<>();
    for (final String name : props.stringPropertyNames()) {
      configProps.put(name, props.getProperty(name));
    }
    return configProps;
  }

  @Override
  public String getValue(String propertyName) {
    if (this.profileActive.isEmpty()) {
      return this.props.getProperty(propertyName);
    } else {
      return this.props.getProperty("%" + profileActive + "." + propertyName);
    }
  }

  @Override
  public String getName() {
    return FortbizConfigSource.class.getSimpleName();
  }

  @Override
  public int getOrdinal() {
    return 300;
  }
}
