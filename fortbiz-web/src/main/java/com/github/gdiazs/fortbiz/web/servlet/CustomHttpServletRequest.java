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
package com.github.gdiazs.fortbiz.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class CustomHttpServletRequest extends HttpServletRequestWrapper {

  private Map<String, String> customHeaderMap = null;

  public CustomHttpServletRequest(HttpServletRequest request) {
    super(request);
    customHeaderMap = new HashMap<String, String>();
  }

  public void addHeader(String name, String value) {
    customHeaderMap.put(name, value);
  }

  @Override
  public String getParameter(String name) {
    String paramValue = super.getParameter(name);
    if (paramValue == null) {
      paramValue = customHeaderMap.get(name);
    }
    return paramValue;
  }
}