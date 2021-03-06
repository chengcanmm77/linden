// Copyright 2016 Xiaomi, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.xiaomi.linden.core;

import java.io.IOException;

import com.xiaomi.linden.bql.BQLCompiler;
import com.xiaomi.linden.core.indexing.LindenIndexRequestParser;
import com.xiaomi.linden.core.search.LindenCore;
import com.xiaomi.linden.core.search.LindenCoreImpl;
import com.xiaomi.linden.thrift.common.LindenIndexRequest;
import com.xiaomi.linden.thrift.common.Response;

public class TestLindenCoreBase {
  public LindenCore lindenCore;
  public LindenConfig lindenConfig;
  public BQLCompiler bqlCompiler;

  public TestLindenCoreBase() throws Exception {
    lindenConfig = new LindenConfig().setIndexType(LindenConfig.IndexType.RAM).setClusterUrl("127.0.0.1:2181/test");
    lindenConfig.putToProperties("merge.policy.class", "com.xiaomi.linden.lucene.merge.TieredMergePolicyFactory");
    lindenConfig.putToProperties("merge.policy.segments.per.tier", "10");
    lindenConfig.putToProperties("merge.policy.max.merge.at.once", "10");
    lindenConfig.setPluginPath("./");
    ZooKeeperService.start();
    init();
    lindenCore = new LindenCoreImpl(lindenConfig);
  }

  public void init() throws Exception {
  }

  public Response handleRequest(String content) throws IOException {
    LindenIndexRequest indexRequest = LindenIndexRequestParser.parse(lindenConfig.getSchema(), content);
    return lindenCore.index(indexRequest);
  }
}
