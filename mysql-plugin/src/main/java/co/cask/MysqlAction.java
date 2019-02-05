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

package co.cask;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.etl.api.action.Action;
import co.cask.db.batch.action.DBAction;
import co.cask.db.batch.action.QueryConfig;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import javax.annotation.Nullable;

import static co.cask.MysqlConstants.ALLOW_MULTIPLE_QUERIES;
import static co.cask.MysqlConstants.AUTO_RECONNECT;
import static co.cask.MysqlConstants.PLUGIN_NAME;

/**
 * Action that runs Mysql command.
 */
@Plugin(type = Action.PLUGIN_TYPE)
@Name(PLUGIN_NAME)
@Description("Action that runs a mysql command")
public class MysqlAction extends DBAction {

  private static final Logger LOG = LoggerFactory.getLogger(MysqlAction.class);
  private static final String JDBC_PLUGIN_ID = "driver";

  private final MysqlActionConfig config;

  public MysqlAction(MysqlActionConfig config) {
    super(config);
    this.config = config;
  }

  /**
   * Mysql Action Config.
   */
  public static class MysqlActionConfig extends QueryConfig implements MysqlConfig {
    @Name(AUTO_RECONNECT)
    @Description("Should the driver try to re-establish stale and/or dead connections")
    public Boolean autoReconnect;

    @Name(ALLOW_MULTIPLE_QUERIES)
    @Nullable
    @Description("Allow the use of ';' to delimit multiple queries during one statement (true/false)")
    public Boolean allowMultiQueries;

    @Override
    public Map<String, String> getDBSpecificArguments() {
      ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

      if (allowMultiQueries != null) {
        builder.put(ALLOW_MULTIPLE_QUERIES, String.valueOf(allowMultiQueries));
      }

      builder.put(AUTO_RECONNECT, String.valueOf(autoReconnect));

      return builder.build();
    }
  }
}
