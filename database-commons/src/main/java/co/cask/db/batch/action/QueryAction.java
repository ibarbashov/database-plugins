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

package co.cask.db.batch.action;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.batch.BatchActionContext;
import co.cask.cdap.etl.api.batch.PostAction;
import co.cask.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Driver;

/**
 * Base class representing database post-action,
 * runs a query after a pipeline run.
 */
@SuppressWarnings("ConstantConditions")
@Plugin(type = PostAction.PLUGIN_TYPE)
@Name("DatabaseQuery")
@Description("Runs a query after a pipeline run.")
public class QueryAction extends PostAction {
  private static final String JDBC_PLUGIN_ID = "driver";
  private static final Logger LOG = LoggerFactory.getLogger(QueryAction.class);
  private final QueryActionConfig config;

  public QueryAction(QueryActionConfig config) {
    this.config = config;
  }

  @Override
  public void run(BatchActionContext batchContext) throws Exception {
    config.validate();

    if (!config.shouldRun(batchContext)) {
      return;
    }

    Class<? extends Driver> driverClass = batchContext.loadPluginClass(JDBC_PLUGIN_ID);
    DBRun executeQuery = new DBRun(config, driverClass);
    executeQuery.run();
  }

  @Override
  public void configurePipeline(PipelineConfigurer pipelineConfigurer) throws IllegalArgumentException {
    config.validate();
    DBUtils.validateJDBCPluginPipeline(pipelineConfigurer, config, JDBC_PLUGIN_ID);
  }
}
