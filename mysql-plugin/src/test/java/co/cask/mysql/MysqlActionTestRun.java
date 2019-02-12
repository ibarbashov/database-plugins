/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.mysql;

import co.cask.cdap.etl.api.action.Action;
import co.cask.cdap.etl.mock.batch.MockSink;
import co.cask.cdap.etl.mock.batch.MockSource;
import co.cask.cdap.etl.proto.v2.ETLBatchConfig;
import co.cask.cdap.etl.proto.v2.ETLPlugin;
import co.cask.cdap.etl.proto.v2.ETLStage;
import co.cask.cdap.proto.artifact.AppRequest;
import co.cask.cdap.proto.id.ApplicationId;
import co.cask.cdap.proto.id.NamespaceId;
import co.cask.cdap.test.ApplicationManager;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MysqlActionTestRun extends MysqlPluginTestBase {

  @Test
  public void testDBAction() throws Exception {
    // create a table that the action will truncate at the end of the run
    try (Connection connection = createConnection()) {
      try (Statement statement = connection.createStatement()) {
        statement.execute("create table dbActionTest (x int, day varchar(10))");
      }
      try (Statement statement = connection.createStatement()) {
        statement.execute("insert into dbActionTest values (1, '1970-01-01')");
      }
    }

    ETLStage source = new ETLStage("source", MockSource.getPlugin("actionInput"));
    ETLStage sink = new ETLStage("sink", MockSink.getPlugin("actionOutput"));
    ETLStage action = new ETLStage("action", new ETLPlugin(
      UI_NAME,
      Action.PLUGIN_TYPE,
      ImmutableMap.<String, String>builder()
        .putAll(BASE_PROPS)
        .put("autoReconnect", "true")
        .put("query", "delete from dbActionTest where day = '${logicalStartTime(yyyy-MM-dd,0m,UTC)}'")
        .build(),
      null));

    ETLBatchConfig config = ETLBatchConfig.builder()
      .addStage(source)
      .addStage(sink)
      .addStage(action)
      .addConnection(sink.getName(), action.getName())
      .addConnection(source.getName(), sink.getName())
      .build();

    AppRequest<ETLBatchConfig> appRequest = new AppRequest<>(DATAPIPELINE_ARTIFACT, config);
    ApplicationId appId = NamespaceId.DEFAULT.app("actionTest");
    ApplicationManager appManager = deployApplication(appId, appRequest);
    runETLOnce(appManager, ImmutableMap.of("logical.start.time", "0"));

    try (Connection connection = createConnection()) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet results = statement.executeQuery("select * from dbActionTest")) {
          Assert.assertFalse(results.next());
        }
      }
    }
  }
}
