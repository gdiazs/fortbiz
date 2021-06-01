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
package com.github.gdiazs.fortbiz.hacienda.cabys;

import com.github.gdiazs.fortbiz.hacienda.cabys.dto.CabysProductsMatchedResponse;
import com.github.gdiazs.fortbiz.hacienda.cabys.dto.CabysResponseDto;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CabysService {

  private static Logger LOGGER = Logger.getLogger(CabysService.class.getName());

  private final SolrClient solrClient;

  @Inject
  public CabysService(@Named("cabys") SolrClient solrClient) {
    this.solrClient = solrClient;
  }

  public CabysProductsMatchedResponse searchProductsByDescription(final String searchQuery) {
    final Map<String, String> queryParamMap = new HashMap<>();
    queryParamMap.put("q", "descripcion:" + searchQuery + "~");
    queryParamMap.put("fl", "codigo,descripcion,impuesto");
    queryParamMap.put("rows", "20");
    queryParamMap.put("ident", "on");
    queryParamMap.put("wt", "json");
    MapSolrParams queryParams = new MapSolrParams(queryParamMap);
    CabysProductsMatchedResponse cabysProductsMatchedResponse = null;

    try {
      final QueryResponse response = solrClient.query(queryParams);
      final SolrDocumentList documents = response.getResults();
      if (!documents.isEmpty()) {
        cabysProductsMatchedResponse = new CabysProductsMatchedResponse();
        final List<CabysResponseDto> cabysProducts = new ArrayList<>();
        for (SolrDocument document : documents) {
          final var valuesMap = document.getFieldValueMap();

          final CabysResponseDto cabysResponseDto =
              CabysResponseDto.builder()
                  .code(valuesMap.get("codigo").toString())
                  .description(valuesMap.get("descripcion").toString())
                  .tax(valuesMap.get("impuesto").toString())
                  .build();

          cabysProducts.add(cabysResponseDto);
        }
        cabysProductsMatchedResponse.setProductsMatched(cabysProducts);
      }

    } catch (SolrServerException | IOException e) {
      LOGGER.log(Level.SEVERE, "Error white requesting Solr instance", e.getCause());
    }
    return cabysProductsMatchedResponse;
  }
}
