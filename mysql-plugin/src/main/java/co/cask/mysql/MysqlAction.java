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

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.etl.api.action.Action;
import co.cask.db.batch.action.DBAction;
import co.cask.db.batch.action.QueryConfig;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import javax.annotation.Nullable;

/**
 * Action that runs MySQL command.
 */
@Plugin(type = Action.PLUGIN_TYPE)
@Name(MysqlConstants.PLUGIN_NAME)
@Description("Action that runs a MySQL command")
public class MysqlAction extends DBAction implements MysqlDriverNameProvider {

  private final MysqlActionConfig mysqlActionConfig;

  public MysqlAction(MysqlActionConfig mysqlActionConfig) {
    super(mysqlActionConfig);
    this.mysqlActionConfig = mysqlActionConfig;
  }

  /**
   * Mysql Action Config.
   */
  public static class MysqlActionConfig extends QueryConfig {
    @Name(MysqlConstants.AUTO_RECONNECT)
    @Description("Should the driver try to re-establish stale and/or dead connections")
    public Boolean autoReconnect;

    @Name(MysqlConstants.ALLOW_MULTIPLE_QUERIES)
    @Nullable
    @Description("Allow the use of ';' to delimit multiple queries during one statement (true/false)")
    public Boolean allowMultiQueries;

    @Override
    public Map<String, String> getDBSpecificArguments() {
      ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

      if (allowMultiQueries != null) {
        builder.put(MysqlConstants.ALLOW_MULTIPLE_QUERIES, String.valueOf(allowMultiQueries));
      }

      builder.put(MysqlConstants.AUTO_RECONNECT, String.valueOf(autoReconnect));

      return builder.build();
    }
  }
}
